import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.util.concurrent.CompletionStage;

public class ZookeeperApp {

    public static void main(String[] args) {
        System.out.println(ZookeeperAppConstants.START_MESSAGE);

        int serverPort;

        if (args.length < 1) {
            System.err.println();
            return;
        } else {
            serverPort = Integer.parseInt(args[ZookeeperAppConstants.SERVER_PORT_IDX]);
        }

        ActorSystem system = ActorSystem.create(ZookeeperAppConstants.ACTOR_SYSTEM_NAME);

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        HttpRouter instance = new HttpRouter(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(http).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(ZookeeperAppConstants.HOST, ZookeeperAppConstants.PORT),
                materializer
        );
        System.out.println(ZookeeperAppConstants.START_SERVER_MESSAGE);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }
}
