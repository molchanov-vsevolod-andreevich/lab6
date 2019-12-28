import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CacheActor extends AbstractActor {
    private String[] serversList;

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
                    String url = msg.getUrl();
                    Long result = store.get(url);
                    sender().tell(new ResultPing(url, result), self());
                })
                .build();
    }

    static class GetMessage {
    }

}
