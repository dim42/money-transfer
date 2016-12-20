package test.transfer.resources;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import static java.lang.String.format;

public class DbTest {

    private static final String PROP_FILE_NAME = "test_sql.xml";
    private static final String DB_NAME = "dbName";
    private static final String SCHEMA = "%SCHEMA%";
    private static final Properties prop = getProperties();
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";

    private static Properties getProperties() {
        Properties prop = new Properties();
        InputStream in = DbTest.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        try {
            prop.loadFromXML(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    @Test
    public void testTransfer() throws Exception {
        String dbClass = prop.getProperty(DB_DRIVER_CLASS_NAME);
        String dbName = prop.getProperty(DB_NAME);
        String dbUrl = prop.getProperty("dbUrl") + dbName;
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        createTestDB(dbClass, dbUrl, dbName, user, password);
    }

    public static void createTestDB(String dbClass, String dbUrl, String dbName, String user, String password) throws Exception {
        Class.forName(dbClass);
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            cn.prepareStatement(prop.getProperty("dropAll")).execute();

            int ind = 0;
            cn.prepareStatement(format("%s %s", prop.getProperty("createSchema"), dbName)).execute();
            cn.prepareStatement(prop.getProperty("createUsers").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertUsers = cn.prepareStatement(prop.getProperty("insertUsers").replace(SCHEMA, dbName));
            insertUsers.setInt(++ind, 1);
            insertUsers.setString(++ind, "user1");
            insertUsers.execute();
            ind = 0;
            insertUsers.setInt(++ind, 2);
            insertUsers.setString(++ind, "user2");
            insertUsers.execute();

            ind = 0;
            cn.prepareStatement(prop.getProperty("createAccounts").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertAccounts = cn.prepareStatement(prop.getProperty("insertAccounts").replace(SCHEMA, dbName));
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
            cn.prepareStatement(prop.getProperty("createCurRates").replace(SCHEMA, dbName)).execute();
            PreparedStatement insertCurRates = cn.prepareStatement(prop.getProperty("insertCurRates").replace(SCHEMA, dbName));
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
            cn.prepareStatement(prop.getProperty("dropAll")).execute();
        }
    }
}
