package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import pack.transfer.util.PropertiesHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

import static java.lang.String.format;
import static pack.transfer.util.PropertiesHelper.DB_NAME;

public class DbTest {

    private static final Logger log = LogManager.getLogger();

    private static final String PROP_FILE_NAME = "test_sql.xml";
    private static final PropertiesHelper prop = new PropertiesHelper(DbTest.class, PROP_FILE_NAME);

    @Before
    public void setUp() throws Exception {
        cleanTestDB();
    }

    @Ignore
    @Test
    public void testCreateDBInsertData() throws Exception {
        createTestDB();
        insertTestData();
    }

    @After
    public void tearDown() {
        cleanTestDB();
    }

    public static void createTestDB() {
        getConnectionAndExecute((cn) -> {
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

    public static void insertTestData() {
        insertUsersAndAccounts();
        insertCurRates();
    }

    private static void insertUsersAndAccounts() {
        getConnectionAndExecute((cn) -> {
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

    public static void insertCurRates() {
        String dbUrl = prop.get("dbUrl") + prop.get(DB_NAME);
        String user = prop.get("user");
        String password = prop.get("password");
        DataSource ds = JdbcConnectionPool.create(dbUrl, user, password);
        DBI dbi = new DBI(ds);
        try (Handle h = dbi.open()) {
            String insertCurRate = prop.getSql("insertCurRate");
            h.execute(insertCurRate, 1, "RUB_EUR", "65.9375");
            h.execute(insertCurRate, 2, "EUR_RUB", "0.0156");
            h.execute(insertCurRate, 3, "EUR_GBP", "0.8513");
        }
    }

    private static void getConnectionAndExecute(Consumer<Connection> consumer) {
        String dbUrl = prop.get("dbUrl") + prop.get(DB_NAME);
        String user = prop.get("user");
        String password = prop.get("password");
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            consumer.accept(cn);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void cleanTestDB() {
        getConnectionAndExecute((cn) -> {
            try {
                cn.prepareStatement(prop.get("dropAll")).execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
