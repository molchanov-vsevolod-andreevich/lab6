import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CacheActor extends AbstractActor {
    private List<String> serversList;

    static Props props() {
        return Props.create(CacheActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ResultPing.class, req -> {
                    String url = req.getUrl();
                    Long result = req.getPing();
                    store.put(url, result);
                })
                .match(CacheActor.GetMessage.class, msg -> {
                    int randServerIdx = new Random().nextInt(serversList.size());
                    String randServer = serversList.get(randServerIdx);
                    System.out.println("Redirect to " + randServer);
                    sender().tell(randServer, self());
                })
                .build();
    }

    static class GetMessage {
    }

}
