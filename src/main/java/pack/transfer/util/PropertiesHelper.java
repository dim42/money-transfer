package pack.transfer.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    private static final Logger log = LogManager.getLogger();

    private static final String SCHEMA = "%SCHEMA%";
    public static final String DB_DRIVER_CLASS_NAME = "dbClass";
    public static final String DB_NAME = "dbName";

    private final Properties properties;
    private final String schemaName;

    public PropertiesHelper(Class<?> clazz, String fileName, Properties prop) {
        properties = loadProperties(clazz, fileName);
        properties.setProperty("jdbcUrl", get("dbUrl") + get(DB_NAME));
        prop.forEach((k, v) -> properties.setProperty((String) k, (String) v));
        String dbClass = get(DB_DRIVER_CLASS_NAME);
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        schemaName = get(DB_NAME);
    }

    public String getSql(String sqlName) {
        return get(sqlName).replace(SCHEMA, schemaName);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String getSchemaName() {
        return schemaName;
    }

    private Properties loadProperties(Class<?> clazz, String fileName) {
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

    public Properties getProperties() {
        return properties;
    }
}
