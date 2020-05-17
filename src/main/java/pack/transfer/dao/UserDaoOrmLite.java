package pack.transfer.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.UserDao;
import pack.transfer.model.User;
import pack.transfer.util.PropertiesHelper;

import java.io.IOException;
import java.sql.SQLException;

import static pack.transfer.model.User.USERS_TABLE;
import static pack.transfer.util.PropertiesHelper.DB_NAME;

public class UserDaoOrmLite implements UserDao {
    private static final Logger log = LogManager.getLogger();

    private final PropertiesHelper prop;

    public UserDaoOrmLite(PropertiesHelper prop) {
        this.prop = prop;
    }

    @Override
    public void createUser(Long id, String name) {
        try (ConnectionSource connectionSource = getJdbcConnectionSource()) {
            Dao<User, Long> dao = DaoManager.createDao(connectionSource, getTableConfig());
            int rowsUp = dao.create(new User(id, name));
            log.debug(rowsUp);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(Long userId) {
        try (ConnectionSource connectionSource = getJdbcConnectionSource()) {
            Dao<User, Long> dao = DaoManager.createDao(connectionSource, getTableConfig());
            return dao.queryForId(userId);
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

    private DatabaseTableConfig<User> getTableConfig() {
        DatabaseTableConfig<User> tableConfig = new DatabaseTableConfig<>();
        tableConfig.setDataClass(User.class);
        tableConfig.setTableName(prop.getSchemaName() + "." + USERS_TABLE);
        return tableConfig;
    }
}
