import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    @Test
    public void givenLogHierarchy_MessagesFiltered() {
        ch.qos.logback.classic.Logger parentLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.logback");
        parentLogger.setLevel(Level.INFO);

        // ch.qos.logback.classic.Logger 와 달리 org.slf4j.Logger 는 setLevel() 을 할 수 없다.
        Logger childLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.logback.tests");

        //  Error > Warn > Info > Debug > Trace
        parentLogger.warn("This message is logged because WARN > INFO.");

        // This request is disabled, because DEBUG < INFO.
        parentLogger.debug("This message is not logged because DEBUG < INFO.");

        childLogger.info("INFO == INFO");

        childLogger.debug("DEBUG < INFO");
    }

    @Test
    public void givenRootLevel_MessagesFiltered() {
        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.logback");
        logger.debug("Hi there!");

        Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        logger.debug("This message is logged because DEBUG == DEBUG.");

        rootLogger.setLevel(Level.ERROR);

        logger.warn("This message is not logged because WARN < ERROR.");

        logger.error("This is logged.");
    }

    /**
     * 성능을 위해서 String concatenation (+) 대신 parameterized messages 를 사용한다.
     */
    @Test
    public void givenParameters_ValuesLogged() {
        Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LogbackTest.class);

        String message = "This is a String";
        Integer zero = 0;

        try {
            logger.debug("Logging message: {}", message);
            logger.debug("Going to divide {} by {}", 42, zero);
            int result = 42 / zero;
        } catch (Exception e) {
            // When an Exception is passed as the last argument to a logging method,
            // Logback will print the stack trace for us.
            logger.error("Error dividing {} by {} ", 42, zero, e);
        }
    }

    /**
     * src/main/resources/logback.xml 내 설정에 따라 아래 로그 내역이 달라진다.
     */
    @Test
    public void givenConfig_MessageFiltered() {
        Logger foobar = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.foobar");
        Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.logback");
        Logger testsLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("kr.cloudscape.logback.tests");

        foobar.debug("This is logged from foobar");
        logger.debug("This is not logged from logger");
        logger.info("This is logged from logger");
        testsLogger.info("This is not logged from tests");
        testsLogger.warn("This is logged from tests");
    }
}
