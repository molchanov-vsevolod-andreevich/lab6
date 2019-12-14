import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class ZookeeperApp {

    public static void main(String[] args) throws KeeperException, InterruptedException {
        test();
//        System.out.println(AkkaStreamsAppConstants.START_MESSAGE);
//
//        ActorSystem system = ActorSystem.create(AkkaStreamsAppConstants.ACTOR_SYSTEM_NAME);
//
//        final Http http = Http.get(system);
//        final ActorMaterializer materializer = ActorMaterializer.create(system);
//
//        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
//
//        HttpRouter instance = new HttpRouter(system);
//
//        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = instance.createRouteFlow(asyncHttpClient, materializer);
//        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
//                routeFlow,
//                ConnectHttp.toHost(AkkaStreamsAppConstants.HOST, AkkaStreamsAppConstants.PORT),
//                materializer
//        );
//        System.out.println(AkkaStreamsAppConstants.START_SERVER_MESSAGE);
//        System.in.read();
//        binding
//                .thenCompose(ServerBinding::unbind)
//                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    void test() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zoo = new ZooKeeper("127.0.0.1:2181", 3000, (Watcher) this);
        zoo.create("/servers/s",
                "data".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
        List<String> servers = zoo.getChildren("/servers", (Watcher) this);
        for (String s : servers) {
            byte[] data = zoo.getData("/servers/" + s, false, null);
            System.out.println("server " + s + " data=" + new String(data));
        }
    }

}
