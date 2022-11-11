import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloLogback {

    private static final Logger logger = LoggerFactory.getLogger(HelloLogback.class);

    public static void main( String[] args ) {
        logger.info("Hello from Logback, {}", HelloLogback.class.getSimpleName());
    }
}
