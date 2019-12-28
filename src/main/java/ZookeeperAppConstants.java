import java.time.Duration;

class ZookeeperAppConstants {
    // Actors constants
    static final String ACTOR_SYSTEM_NAME = "routes";
    static final String CACHE_ACTOR_NAME = "cacheActor";

    // Server constants
    static final String HOST = "localhost";

    // Zookeeper constants
    static final int ZOOKEEPER_PORT = 2181;

    // Messages
    static final String START_MESSAGE = "start!";
    static final String START_SERVER_MESSAGE = "Server online at http://localhost:8080/\nPress RETURN to stop...\n";
    static final String NOT_ENOUGH_ARGS_ERROR_MESSAGE = "Not enough arguments. Run program with argument [serverPort]";

    // HTTP query parameters
    static final String URL_PARAMETER_NAME = "url";
    static final String COUNT_PARAMETER_NAME = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int SERVER_PORT_IDX_IN_ARGS = 0;
}
