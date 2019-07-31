package base;

import org.junit.Test;

public class CommonLoggerTest {
    @Test
    public void testLog(){
        CommonLogger.logger.info("这是一条日志");
    }
}
