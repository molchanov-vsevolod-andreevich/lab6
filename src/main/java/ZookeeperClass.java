import akka.actor.ActorRef;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperClass implements Watcher {
    private ZooKeeper zooKeeper;
    private ActorRef cacheActor;

    public ZookeeperClass(ActorRef cacheActor) throws IOException {
        this.cacheActor = cacheActor;
        zooKeeper = new ZooKeeper(
                ZookeeperAppConstants.ZOOKEEPER_SERVER + ":" + ZookeeperAppConstants.ZOOKEEPER_PORT,
                ZookeeperAppConstants.ZOOKEEPER_SESSION_TIMEOUT,
                this
        );
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState())
    }
}
