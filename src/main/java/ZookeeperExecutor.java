import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperExecutor implements Watcher {
    private ZooKeeper zoo;
    private ActorRef cacheActor;

    public ZookeeperExecutor(ActorRef cacheActor, int serverPort) throws IOException, KeeperException, InterruptedException {
        this.cacheActor = cacheActor;
        zoo = new ZooKeeper(
                ZookeeperAppConstants.ZOOKEEPER_SERVER + ":" + ZookeeperAppConstants.ZOOKEEPER_PORT,
                ZookeeperAppConstants.ZOOKEEPER_SESSION_TIMEOUT,
                this
        );

        zoo.create(
                ZookeeperAppConstants.SERVER_NODE,
                Integer.toString(serverPort).getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            List<String> serversNodes = zoo.getChildren(ZookeeperAppConstants.SERVERS_NODE, this);

            List<String> serversList = new ArrayList<>();

            for (String s : serversNodes) {
                byte[] data = zoo.getData(ZookeeperAppConstants.SERVERS_NODES_PATH + s, false, null);
                
            }

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
