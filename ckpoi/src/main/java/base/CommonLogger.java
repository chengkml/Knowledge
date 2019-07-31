package base;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.net.URL;

public class CommonLogger {

    static{
        URL url = CommonLogger.class.getClassLoader().getResource("");
        PropertyConfigurator.configure(url.getPath()+"/config/log4j.properties");
    }

    public static Logger logger = Logger.getLogger(CommonLogger.class);
}
