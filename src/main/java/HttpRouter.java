import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.asynchttpclient.AsyncHttpClient;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class HttpRouter {
    ActorRef cacheActor;

    public HttpRouter( ActorSystem system) {
        cacheActor = system.actorOf(CacheActor.props(), AkkaStreamsAppConstants.CACHE_ACTOR_NAME);
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> createRouteFlow(AsyncHttpClient asyncHttpClient, ActorMaterializer materializer) {
        Sink<TestPing, CompletionStage<Long>> testSink = createSink(asyncHttpClient);

        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query requestQuery = req.getUri().query();
                    String url = requestQuery.getOrElse(AkkaStreamsAppConstants.TEST_URL_KEY, "");
                    Integer count = Integer.parseInt(requestQuery.getOrElse(AkkaStreamsAppConstants.COUNT_KEY, "-1"));
                    return new TestPing(url, count);
                })
                .mapAsync(AkkaStreamsAppConstants.PARALLELISM, testPing ->
                        Patterns.ask(cacheActor, new CacheActor.GetMessage(testPing.getUrl()), AkkaStreamsAppConstants.TIMEOUT)
                                .thenCompose(req -> {
                                    ResultPing res = (ResultPing) req;
                                    if (res.getPing() != null) {
                                        return CompletableFuture.completedFuture(res);
                                    } else {
                                        return Source.from(Collections.singletonList(testPing))
                                                .toMat(testSink, Keep.right())
                                                .run(materializer)
                                                .thenApply(time -> new ResultPing(testPing.getUrl(), time / testPing.getCount() / AkkaStreamsAppConstants.ONE_SECOND_IN_NANO_SECONDS));
                                    }
                                }))
                .map(res -> {
                    cacheActor.tell(res, ActorRef.noSender());
                    return HttpResponse.create()
                            .withEntity(res.getUrl() + " " + res.getPing());
                });
    }

    public Sink<TestPing, CompletionStage<Long>> createSink(AsyncHttpClient asyncHttpClient) {
        Sink<Long, CompletionStage<Long>> fold = Sink.fold(0L, Long::sum);

        return Flow.<TestPing>create()
                .mapConcat(testPing -> Collections.nCopies(testPing.getCount(), testPing.getUrl()))
                .mapAsync(AkkaStreamsAppConstants.PARALLELISM, url -> {
                    long startTime = System.nanoTime();
                    return asyncHttpClient
                            .prepareGet(url)
                            .execute()
                            .toCompletableFuture()
                            .thenApply(response -> System.nanoTime() - startTime);
                })
                .toMat(fold, Keep.right());
    }
}
