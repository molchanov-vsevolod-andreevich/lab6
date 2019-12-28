import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperClass {
    private ZooKeeper zooKeeper;
    private ActorRef cacheActor;

    public ZookeeperClass(ActorRef cacheActor) {
        this.cacheActor = cacheActor;
        zooKeeper = new ZooKeeper(ZookeeperAppConstants.ZOOKEEPER_SERVER + ":" + ZookeeperAppConstants.ZOOKEEPER_PORT,
                )
    }
}
