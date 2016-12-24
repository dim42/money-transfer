package pack.transfer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pack.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

import static java.lang.String.format;

public class DbTest {

    private static final Logger log = LogManager.getLogger();

    private static final String PROP_FILE_NAME = "test_sql.xml";
    private static final String DB_NAME = "dbName";
    private static final String SCHEMA = "%SCHEMA%";
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";
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
        getConnectionAndExecute((cn, dbName) -> {
            try {
                cn.prepareStatement(format("%s %s", prop.get("createSchema"), dbName)).execute();
                cn.prepareStatement(prop.get("createUsers").replace(SCHEMA, dbName)).execute();
                cn.prepareStatement(prop.get("createAccounts").replace(SCHEMA, dbName)).execute();
                cn.prepareStatement(prop.get("createCurRates").replace(SCHEMA, dbName)).execute();
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
        getConnectionAndExecute((cn, dbName) -> {
            try {
                int ind = 0;
                PreparedStatement stmt = cn.prepareStatement(prop.get("insertUser").replace(SCHEMA, dbName));
                stmt.setInt(++ind, 1);
                stmt.setString(++ind, "user1");
                stmt.execute();
                ind = 0;
                stmt.setInt(++ind, 2);
                stmt.setString(++ind, "user2");
                stmt.execute();

                ind = 0;
                stmt = cn.prepareStatement(prop.get("insertAccount").replace(SCHEMA, dbName));
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
        getConnectionAndExecute((cn, dbName) -> {
            try {
                int ind = 0;
                PreparedStatement stmt = cn.prepareStatement(prop.get("insertCurRate").replace(SCHEMA, dbName));
                stmt.setInt(++ind, 1);
                stmt.setString(++ind, "RUB_EUR");
                stmt.setString(++ind, "65.9375");
                stmt.execute();
                ind = 0;
                stmt.setInt(++ind, 2);
                stmt.setString(++ind, "EUR_RUB");
                stmt.setString(++ind, "0.0156");
                stmt.execute();
                ind = 0;
                stmt.setInt(++ind, 3);
                stmt.setString(++ind, "EUR_GBP");
                stmt.setString(++ind, "0.8513");
                stmt.execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }

    private static void getConnectionAndExecute(BiConsumer<Connection, String> consumer) {
        String dbClass = prop.get(DB_DRIVER_CLASS_NAME);
        classForName(dbClass);
        String dbName = prop.get(DB_NAME);
        String dbUrl = prop.get("dbUrl") + dbName;
        String user = prop.get("user");
        String password = prop.get("password");
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            consumer.accept(cn, dbName);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static void classForName(String dbClass) {
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void cleanTestDB() {
        getConnectionAndExecute((cn, dbName) -> {
            try {
                cn.prepareStatement(prop.get("dropAll")).execute();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
