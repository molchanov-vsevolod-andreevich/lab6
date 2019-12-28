import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.*;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;
import scala.concurrent.Future;

import java.util.concurrent.CompletionStage;

class HttpRouter extends AllDirectives {
    private final ActorRef cacheActor;

    HttpRouter(ActorSystem system) {
        cacheActor = system.actorOf(CacheActor.props(), ZookeeperAppConstants.CACHE_ACTOR_NAME);
    }

    Route createRoute(Http http) {
        return route(
            get(() ->
                    parameter(ZookeeperAppConstants.URL_PARAMETER_NAME, (url) ->
                            parameter(ZookeeperAppConstants.COUNT_PARAMETER_NAME, (count) ->
                                {
                                    int redirectCount = Integer.parseInt(count);
                                    if (redirectCount != 0) {
                                        return completeOKWithFuture(curlUrl(http, url));
                                    } else {
                                        return completeOKWithFuture(redirect(http, url, redirectCount));
                                    }
                                }))));
    }

    private CompletionStage<HttpResponse> curlUrl(Http http, String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private CompletionStage<HttpResponse> redirect(Http http, String url, int count) {
        return Patterns.ask(cacheActor, new CacheActor.GetRandomServer(), ZookeeperAppConstants.TIMEOUT)
                .thenCompose(randServer -> )
                .thenCompose(randServer -> {
                    String redirectUrl = Uri.create(randServer)
                            .query(Query.create(
                                    Pair.create(ZookeeperAppConstants.URL_PARAMETER_NAME, url),
                                    Pair.create(ZookeeperAppConstants.COUNT_PARAMETER_NAME, Integer.toString(count - 1))
                            ))
                            .toString();
                    return curlUrl(http, redirectUrl);
                })
    }
}
