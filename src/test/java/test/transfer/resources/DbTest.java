package test.transfer.resources;

import org.junit.Test;
import test.transfer.util.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static java.lang.String.format;

public class DbTest {

    private static final String PROP_FILE_NAME = "test_sql.xml";
    private static final String DB_NAME = "dbName";
    private static final String SCHEMA = "%SCHEMA%";
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";
    private static final PropertiesHelper prop = new PropertiesHelper(DbTest.class, PROP_FILE_NAME);

    @Test
    public void testTransfer() throws Exception {
        String dbClass = prop.get(DB_DRIVER_CLASS_NAME);
        String dbName = prop.get(DB_NAME);
        String dbUrl = prop.get("dbUrl") + dbName;
        String user = prop.get("user");
        String password = prop.get("password");
        createTestDB(dbClass, dbUrl, dbName, user, password);
    }

    public static void createTestDB(String dbClass, String dbUrl, String dbName, String user, String password) throws Exception {
        Class.forName(dbClass);
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            cn.prepareStatement(prop.get("dropAll")).execute();

            int ind = 0;
            cn.prepareStatement(format("%s %s", prop.get("createSchema"), dbName)).execute();
            cn.prepareStatement(prop.get("createUsers").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertUsers = cn.prepareStatement(prop.get("insertUsers").replace(SCHEMA, dbName));
            insertUsers.setInt(++ind, 1);
            insertUsers.setString(++ind, "user1");
            insertUsers.execute();
            ind = 0;
            insertUsers.setInt(++ind, 2);
            insertUsers.setString(++ind, "user2");
            insertUsers.execute();

            ind = 0;
            cn.prepareStatement(prop.get("createAccounts").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertAccounts = cn.prepareStatement(prop.get("insertAccounts").replace(SCHEMA, dbName));
            insertAccounts.setString(++ind, "1234");
            insertAccounts.setString(++ind, "20");
            insertAccounts.setInt(++ind, 1);
            insertAccounts.setBoolean(++ind, true);
            insertAccounts.setString(++ind, "100");
            insertAccounts.execute();
            ind = 0;
            insertAccounts.setString(++ind, "2222");
            insertAccounts.setString(++ind, "130");
            insertAccounts.setInt(++ind, 2);
            insertAccounts.setBoolean(++ind, true);
            insertAccounts.setString(++ind, "80");
            insertAccounts.execute();

            ind = 0;
            cn.prepareStatement(prop.get("createCurRates").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertCurRates = cn.prepareStatement(prop.get("insertCurRates").replace(SCHEMA, dbName));
            insertCurRates.setInt(++ind, 1);
            insertCurRates.setString(++ind, "RUB_EUR");
            insertCurRates.setString(++ind, "65.21");
            insertCurRates.execute();
            ind = 0;
            insertCurRates.setInt(++ind, 2);
            insertCurRates.setString(++ind, "EUR_GBP");
            insertCurRates.setString(++ind, "0.84");
            insertCurRates.execute();
        }
    }

    private static void cleanTestDB(String dbClass, String dbUrl, String dbName, String user, String password) throws Exception {
        Class.forName(dbClass);
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            cn.prepareStatement(prop.get("dropAll")).execute();
        }
    }
}
