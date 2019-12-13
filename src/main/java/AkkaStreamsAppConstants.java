import java.time.Duration;

class AkkaStreamsAppConstants {
    // Actors Constants
    static final String ACTOR_SYSTEM_NAME = "routes";
    static final String CACHE_ACTOR_NAME = "cacheActor";

    // Server Constants
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Messages
    static final String START_MESSAGE = "start!";
    static final String START_SERVER_MESSAGE = "Server online at http://localhost:8080/\nPress RETURN to stop...\n";

    // HTTP Request and Response constants
    static final String TEST_URL_KEY = "testUrl";
    static final String COUNT_KEY = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int PARALLELISM = 1;
    static final long ONE_SECOND_IN_NANO_SECONDS = 1_000_000L;
}
