package test.transfer.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.dao.api.AccountDao;
import test.transfer.dao.api.DBManager;
import test.transfer.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static test.transfer.dao.impl.DBManagerImpl.PROP_FILE_NAME;

public class AccountDaoImpl implements AccountDao {

    private static final Logger log = LogManager.getLogger();
    private final DBManager db;

    public AccountDaoImpl(DBManager db) {
        this.db = db;
    }

    public static void main(String[] args) throws Exception {
        DBManager db = new DBManagerImpl(PROP_FILE_NAME);
        new AccountDaoImpl(db).upAccBals("1234", "100.46", "4444", "200");
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
            Account account = new Account(rs.getString("num"));
            return account;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upAccBals(String accNum1, String balance1, String accNum2, String balance2) {
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false);
            PreparedStatement upAccBal = cn.prepareStatement(db.getSql("updateAccountBalance"));
            upAccBal.setString(1, balance1);
            upAccBal.setString(2, accNum1);
            upAccBal.execute();
            upAccBal.setString(1, balance2);
            upAccBal.setString(2, accNum2);
            upAccBal.execute();
            cn.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
