package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.transfer.util.PropertiesHelper;

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
        getConnectionAndExecute((cn, dbName) -> {
            try {
                int ind = 0;
                PreparedStatement pstmt = cn.prepareStatement(prop.get("insertUsers").replace(SCHEMA, dbName));
                pstmt.setInt(++ind, 1);
                pstmt.setString(++ind, "user1");
                pstmt.execute();
                ind = 0;
                pstmt.setInt(++ind, 2);
                pstmt.setString(++ind, "user2");
                pstmt.execute();

                ind = 0;
                pstmt = cn.prepareStatement(prop.get("insertAccounts").replace(SCHEMA, dbName));
                pstmt.setString(++ind, "1234");
                pstmt.setString(++ind, "20");
                pstmt.setInt(++ind, 1);
                pstmt.setBoolean(++ind, true);
                pstmt.setString(++ind, "100");
                pstmt.execute();
                ind = 0;
                pstmt.setString(++ind, "2222");
                pstmt.setString(++ind, "130");
                pstmt.setInt(++ind, 2);
                pstmt.setBoolean(++ind, true);
                pstmt.setString(++ind, "80");
                pstmt.execute();

                ind = 0;
                pstmt = cn.prepareStatement(prop.get("insertCurRates").replace(SCHEMA, dbName));
                pstmt.setInt(++ind, 1);
                pstmt.setString(++ind, "RUB_EUR");
                pstmt.setString(++ind, "65.21");
                pstmt.execute();
                ind = 0;
                pstmt.setInt(++ind, 2);
                pstmt.setString(++ind, "EUR_GBP");
                pstmt.setString(++ind, "0.84");
                pstmt.execute();
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
