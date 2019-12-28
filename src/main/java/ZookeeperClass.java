import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class ZookeeperClass implements Watcher {
    private ZooKeeper zoo;
    private ActorRef cacheActor;

    public ZookeeperClass(ActorRef cacheActor) throws IOException, KeeperException, InterruptedException {
        this.cacheActor = cacheActor;
        zoo = new ZooKeeper(
                ZookeeperAppConstants.ZOOKEEPER_SERVER + ":" + ZookeeperAppConstants.ZOOKEEPER_PORT,
                ZookeeperAppConstants.ZOOKEEPER_SESSION_TIMEOUT,
                this
        );

        zoo.create(
                ZookeeperAppConstants.SERVERS_NODE,
                "data".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        List<String> servers = zoo.getChildren("/servers", this);
    }
}
