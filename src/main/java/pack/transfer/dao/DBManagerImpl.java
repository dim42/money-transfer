package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.DBManager;
import pack.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagerImpl implements DBManager {

    private static final Logger log = LogManager.getLogger();
    private final PropertiesHelper prop;
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
        String dbName = prop.get(DB_NAME);
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
        return prop.getSql(sqlName);
    }
}
