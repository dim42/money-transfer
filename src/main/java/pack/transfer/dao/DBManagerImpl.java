package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.DBManager;
import pack.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static pack.transfer.util.PropertiesHelper.DB_NAME;

public class DBManagerImpl implements DBManager {
    private static final Logger log = LogManager.getLogger();

    private final PropertiesHelper prop;

    public DBManagerImpl(PropertiesHelper prop) {
        this.prop = prop;
    }

    @Override
    public Connection getConnection() {
        try {
            String dbUrl = prop.get("dbUrl") + prop.get(DB_NAME);
            String user = prop.get("user");
            String password = prop.get("password");
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
