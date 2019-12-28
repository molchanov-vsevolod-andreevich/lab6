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
            get(() ->
                    parameter(ZookeeperAppConstants.URL_PARAMETER_NAME, (url) ->
                            parameter(ZookeeperAppConstants.COUNT_PARAMETER_NAME, (count) ->
                                {
                                    int c = Integer.parseInt(count);
                                    if (c != 0) {
                                        String newUrl = 
                                    }
                                }))));
    }
}
