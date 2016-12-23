package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.AccountDao;
import pack.transfer.api.DBManager;
import pack.transfer.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

public class AccountDaoImpl implements AccountDao {

    private static final Logger log = LogManager.getLogger();
    private final DBManager db;

    public AccountDaoImpl(DBManager db) {
        this.db = db;
    }

    @Override
    public void create(String number, String balance, String currency, Long userId, boolean active, String limit) {
        try (Connection cn = db.getConnection()) {
            PreparedStatement stmt = cn.prepareStatement(db.getSql("insertAccount"));
            int ind = 0;
            stmt.setString(++ind, number);
            stmt.setString(++ind, balance);
            stmt.setString(++ind, currency);
            stmt.setLong(++ind, userId);
            stmt.setBoolean(++ind, active);
            stmt.setString(++ind, limit);
            stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findAccount(String number) {
        try (Connection cn = db.getConnection()) {
            PreparedStatement stmt = cn.prepareStatement(db.getSql("findAccount"));
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            boolean first = rs.first();
            if (!first) {
                throw new RuntimeException(format("Account (%s) is not found", number));
            }
            return new Account(rs.getString("num"), rs.getString("balance"), "currency", rs.getLong("user_id"), rs.getBoolean("active"), rs.getString("lim"));
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAccountsBalance(String accNum1, String balance1, String accNum2, String balance2) {
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false);
            PreparedStatement stmt = cn.prepareStatement(db.getSql("updateAccountBalance"));
            stmt.setString(1, balance1);
            stmt.setString(2, accNum1);
            stmt.execute();
            stmt.setString(1, balance2);
            stmt.setString(2, accNum2);
            stmt.execute();
            cn.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
