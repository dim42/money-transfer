package test.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.AccountDao;
import test.transfer.api.DBManager;
import test.transfer.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDaoImpl implements AccountDao {

    private static final Logger log = LogManager.getLogger();
    private final DBManager db;

    public AccountDaoImpl(DBManager db) {
        this.db = db;
    }

    public static void main(String[] args) throws Exception {
        DBManager db = new DBManagerImpl(DBManager.PROP_FILE_NAME);
        new AccountDaoImpl(db).updateAccountsBalance("1234", "100.46", "4444", "200");
    }

    @Override
    public Account findAccount(String accNum) {
        try (Connection cn = db.getConnection()) {
            PreparedStatement upAccBal = cn.prepareStatement(db.getSql("findAccount"));
            upAccBal.setString(1, accNum);
            ResultSet rs = upAccBal.executeQuery();
            boolean first = rs.first();
            if (!first) {
                throw new RuntimeException("Account is not found for num=" + accNum);
            }
            Account account = new Account(rs.getString("num"), rs.getString("balance"), "currency", rs.getLong("user_id"), rs.getBoolean("active"), rs
                    .getString("lim"));
            return account;
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
