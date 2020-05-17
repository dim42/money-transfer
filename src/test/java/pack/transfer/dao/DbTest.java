package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import pack.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

import static java.lang.String.format;
import static pack.transfer.util.PropertiesHelper.DB_NAME;

public class DbTest {

    private static final Logger log = LogManager.getLogger();

    private static final String PROP_FILE_NAME = "test_sql.xml";
    private static final PropertiesHelper prop = new PropertiesHelper(DbTest.class, PROP_FILE_NAME, new Properties());
    private static final String dbUrl = prop.get("jdbcUrl");
    private static final String user = prop.get("user");
    private static final String password = prop.get("password");

    @Before
    public void setUp() {
        cleanTestDB(dbUrl, user, password);
    }

    @Ignore
    @Test
    public void testCreateDBInsertData() {
        createTestDB(dbUrl, user, password);
        insertTestData();
    }

    @After
    public void tearDown() {
        cleanTestDB(dbUrl, user, password);
    }

    public static void createTestDB(String dbUrl, String user, String password) {
        getConnectionAndExecute(dbUrl, user, password, (cn) -> {
            try {
                cn.prepareStatement(format("%s %s", prop.get("createSchema"), prop.get(DB_NAME))).execute();
                cn.prepareStatement(prop.getSql("createUsers")).execute();
                cn.prepareStatement(prop.getSql("createAccounts")).execute();
                cn.prepareStatement(prop.getSql("createCurRates")).execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }

    public void insertTestData() {
        insertUsersAndAccounts();
        insertCurRates(dbUrl, user, password);
    }

    private void insertUsersAndAccounts() {
        getConnectionAndExecute(dbUrl, user, password, (cn) -> {
            try {
                int ind = 0;
                PreparedStatement stmt = cn.prepareStatement(prop.getSql("insertUser"));
                stmt.setInt(++ind, 1);
                stmt.setString(++ind, "user1");
                stmt.execute();
                ind = 0;
                stmt.setInt(++ind, 2);
                stmt.setString(++ind, "user2");
                stmt.execute();

                ind = 0;
                stmt = cn.prepareStatement(prop.getSql("insertAccount"));
                stmt.setString(++ind, "1234");
                stmt.setString(++ind, "20");
                stmt.setString(++ind, "RUB");
                stmt.setInt(++ind, 1);
                stmt.setBoolean(++ind, true);
                stmt.setString(++ind, "100");
                stmt.execute();
                ind = 0;
                stmt.setString(++ind, "2222");
                stmt.setString(++ind, "130");
                stmt.setString(++ind, "RUB");
                stmt.setInt(++ind, 2);
                stmt.setBoolean(++ind, true);
                stmt.setString(++ind, "80");
                stmt.execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }

    public static void insertCurRates(String dbUrl, String user, String password) {
        DBI dbi = new DBI(dbUrl, user, password);
        try (Handle h = dbi.open()) {
            String insertCurRate = prop.getSql("insertCurRate");
            h.execute(insertCurRate, 1, "RUB_EUR", "65.9375");
            h.execute(insertCurRate, 2, "EUR_RUB", "0.0156");
            h.execute(insertCurRate, 3, "EUR_GBP", "0.8513");
            h.execute(insertCurRate, 4, "USD_RUB", "61.1696");
            h.execute(insertCurRate, 5, "EUR_USD", "1.0453");
        }
    }

    private static void getConnectionAndExecute(String dbUrl, String user, String password, Consumer<Connection> consumer) {
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            consumer.accept(cn);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void cleanTestDB(String dbUrl, String user, String password) {
        getConnectionAndExecute(dbUrl, user, password, (cn) -> {
            try {
                cn.prepareStatement(prop.get("dropAll")).execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }

    public static void cleanTestDBMysql(String dbUrl, String user, String password) {
        getConnectionAndExecute(dbUrl, user, password, (cn) -> {
            try {
                cn.prepareStatement("TRUNCATE TABLE cur_rates").execute();
                cn.prepareStatement("TRUNCATE TABLE accounts").execute();
                cn.prepareStatement("DELETE FROM users").execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
