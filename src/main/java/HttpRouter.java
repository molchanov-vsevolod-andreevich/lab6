import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import scala.concurrent.Future;

class HttpRouter extends AllDirectives {
    private final ActorRef cacheActor;

    HttpRouter(ActorSystem system) {
        cacheActor = system.actorOf(CacheActor.props(), ZookeeperAppConstants.ROUTE_ACTOR_NAME);
    }

    Route createRoute() {
        return route(
                path(ZookeeperAppConstants.SERVER_POST_PATH, () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackageRequest.class), msg -> {
                                            cacheActor.tell(msg, ActorRef.noSender());
                                            return complete(AkkaAppConstants.START_TEST_MESSAGE);
                                        })))),
                path(ZookeeperAppConstants.SERVER_GET_PATH, () ->
                        get(() ->
                                parameter(AkkaAppConstants.PACKAGE_ID_PARAMETER_NAME, (packageId) ->
                                {
                                    Future<Object> res = Patterns.ask(cacheActor, new StoreActor.GetMessage(packageId), AkkaAppConstants.TIMEOUT);
                                    return completeOKWithFuture(res, Jackson.marshaller());
                                }))));
    }
}
