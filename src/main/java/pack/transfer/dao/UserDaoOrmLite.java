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

import static pack.transfer.api.DBManager.DB_DRIVER_CLASS_NAME;
import static pack.transfer.api.DBManager.DB_NAME;
import static pack.transfer.model.User.USERS_TABLE;

public class UserDaoOrmLite implements UserDao {
    private static final Logger log = LogManager.getLogger();

    private final String schemaName;
    private final String dbUrl;
    private final String user;
    private final String password;

    public UserDaoOrmLite(String propFileName) {
        PropertiesHelper prop = new PropertiesHelper(getClass(), propFileName);
        String dbClass = prop.get(DB_DRIVER_CLASS_NAME);
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        schemaName = prop.get(DB_NAME);
        dbUrl = prop.get("dbUrl") + schemaName;
        user = prop.get("user");
        password = prop.get("password");
    }

    @Override
    public void createUser(Long id, String name) {
        try (ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl, user, password)) {
            Dao<User, Long> dao = DaoManager.createDao(connectionSource, getTableConfig());
            int rowsUp = dao.create(new User(id, name));
            log.debug(rowsUp);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private DatabaseTableConfig<User> getTableConfig() {
        DatabaseTableConfig<User> tableConfig = new DatabaseTableConfig<>();
        tableConfig.setDataClass(User.class);
        tableConfig.setTableName(schemaName + "." + USERS_TABLE);
        return tableConfig;
    }
}
