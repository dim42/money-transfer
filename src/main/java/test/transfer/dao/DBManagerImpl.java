package test.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.DBManager;
import test.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagerImpl implements DBManager {

    private static final Logger log = LogManager.getLogger();
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";
    private final PropertiesHelper prop;
    private final String dbName;
    private final String dbUrl;
    private final String user;
    private final String password;

    public DBManagerImpl(String propFileName) {
        prop = new PropertiesHelper(getClass(), propFileName);
        String dbClass = prop.get(DB_DRIVER_CLASS_NAME);
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        dbName = prop.get("dbName");
        dbUrl = prop.get("dbUrl") + dbName;
        user = prop.get("user");
        password = prop.get("password");
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
        return prop.get(sqlName).replace(SCHEMA, dbName);
    }
}
