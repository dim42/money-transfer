package pack.transfer.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    private static final Logger log = LogManager.getLogger();

    private final Properties properties;

    public PropertiesHelper(Class<?> clazz, String fileName) {
        properties = getProperties(clazz, fileName);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    private Properties getProperties(Class<?> clazz, String fileName) {
        InputStream in = clazz.getClassLoader().getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            prop.loadFromXML(in);
            return prop;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
