package test.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.DBManager;
import test.transfer.api.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LogManager.getLogger();
    private final DBManager db;

    public UserDaoImpl(DBManager db) {
        this.db = db;
    }

    @Override
    public void createUser(Long id, String name) {
        try (Connection cn = db.getConnection()) {
            PreparedStatement stmt = cn.prepareStatement(db.getSql("insertUser"));
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
