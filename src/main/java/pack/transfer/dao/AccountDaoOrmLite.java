package pack.transfer.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.AccountDao;
import pack.transfer.api.UserDao;
import pack.transfer.model.Account;
import pack.transfer.model.User;
import pack.transfer.util.PropertiesHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static pack.transfer.model.Account.ACCOUNTS_TABLE;
import static pack.transfer.util.PropertiesHelper.DB_NAME;

public class AccountDaoOrmLite implements AccountDao {
    private static final Logger log = LogManager.getLogger();

    private final PropertiesHelper prop;
    private final UserDao userDao;

    public AccountDaoOrmLite(PropertiesHelper prop, UserDao userDao) {
        this.prop = prop;
        this.userDao = userDao;
    }

    @Override
    public void create(String number, String balance, String currency, Long userId, boolean active, String limit) {
        try (ConnectionSource connectionSource = getJdbcConnectionSource()) {
            Dao<Account, Long> dao = DaoManager.createDao(connectionSource, getTableConfig());
            User user = userDao.findUser(userId);
            int rowsUp = dao.create(new Account(number, balance, currency, user, active, limit));
            log.debug(rowsUp);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findAccount(String number) {
        try (ConnectionSource connectionSource = getJdbcConnectionSource()) {
            Dao<Account, String> dao = DaoManager.createDao(connectionSource, getTableConfig());
            return dao.queryForId(number);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAccountsBalance(Account acc1, Account acc2) {
        try (ConnectionSource connectionSource = getJdbcConnectionSource()) {
            TransactionManager.callInTransaction(connectionSource,
                    (Callable<Void>) () -> {
                        Dao<Account, String> dao = DaoManager.createDao(connectionSource, getTableConfig());
                        dao.update(acc1);
                        dao.update(acc2);
                        return null;
                    });
        } catch (SQLException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private JdbcConnectionSource getJdbcConnectionSource() throws SQLException {
        String dbUrl = prop.get("jdbcUrl");
        String user = prop.get("user");
        String password = prop.get("password");
        return new JdbcConnectionSource(dbUrl, user, password);
    }

    private DatabaseTableConfig<Account> getTableConfig() {
        DatabaseTableConfig<Account> tableConfig = new DatabaseTableConfig<>();
        tableConfig.setDataClass(Account.class);
        tableConfig.setTableName(prop.getSchemaName() + "." + ACCOUNTS_TABLE);
        return tableConfig;
    }
}
