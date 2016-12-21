package test.transfer.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.dao.api.DBManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManagerImpl implements DBManager {

    private static final Logger log = LogManager.getLogger();
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";
    private final Properties prop;
    private final String dbName;
    private final String dbUrl;
    private final String user;
    private final String password;

    public DBManagerImpl(String propFileName) {
        prop = getProperties(propFileName);
        String dbClass = prop.getProperty(DB_DRIVER_CLASS_NAME);
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        dbName = prop.getProperty("dbName");
        dbUrl = prop.getProperty("dbUrl") + dbName;
        user = prop.getProperty("user");
        password = prop.getProperty("password");
    }

    private static Properties getProperties(String fileName) {
        InputStream in = AccountDaoImpl.class.getClassLoader().getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            prop.loadFromXML(in);
            return prop;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String sqlName) {
        return prop.getProperty(sqlName).replace(SCHEMA, dbName);
    }
}
