package resolve;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * @Title: TextAreaLogAppender
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2020/3/21 22:18
 * @Version: 1.0
 */
public class TextAreaLogAppender extends AppenderSkeleton {

    private JTextArea logArea;

    public TextAreaLogAppender(JTextArea logArea){
        this.logArea = logArea;
        this.setLayout(new PatternLayout("[%-5p] %d{HH:mm:ss,SSS}: %m%n"));
    }

    @Override
    protected void append(LoggingEvent event) {
        logArea.append(this.getLayout().format(event));
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
