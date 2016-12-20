package test.transfer.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.String.format;

public class Dao {

    private static final String PROP_FILE_NAME = "sql.xml";
    private static final String DB_DRIVER_CLASS_NAME = "dbClass";
    private static final String DB_NAME = "dbName";
    private static final String SCHEMA = "%SCHEMA%";
    private static final Properties prop = getProperties();

    private static Properties getProperties() {
        Properties prop = new Properties();
        InputStream in = Dao.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        try {
            prop.loadFromXML(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    public static void main(String[] args) throws Exception {
        new Dao().upAccBal();
//        db();
    }

    public void upAccBal() throws Exception {
        String dbClass = prop.getProperty(DB_DRIVER_CLASS_NAME);
        String dbName = prop.getProperty(DB_NAME);
        String dbUrl = prop.getProperty("dbUrl") + dbName;
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        String accNum = "1234";
        String balance = "100.46";
        upAccBal(dbClass, dbUrl, dbName, user, password, accNum, balance);
    }

    public static void upAccBal(String dbClass, String dbUrl, String dbName, String user, String password, String accNum, String balance) throws Exception {
        Class.forName(dbClass);
        try (Connection cn = DriverManager.getConnection(dbUrl, user, password)) {
            PreparedStatement upAccBal = cn.prepareStatement(prop.getProperty("updateAccountBalance").replace(SCHEMA, dbName));
            upAccBal.setString(1, balance);
            upAccBal.setString(2, accNum);
            upAccBal.execute();
        }
    }

    public static void db() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String user = "user";
        String password = "";
        Class.forName(org.h2.Driver.class.getName());
        String dbUrl = "jdbc:h2:~/mt_test";
//        connection = DriverManager.getConnection(dbUrl, user, password);
        connection = DriverManager.getConnection(dbUrl, "sa", "");
//        statement = connection.createStatement();
//        String select="SELECT * ";
        String select = "show databases;";
        statement = connection.prepareStatement(select);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnAmount = rsmd.getColumnCount();
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= columnAmount; i++) {
            result.append(format("%1$10s",
                    rsmd.getColumnLabel(i)));
            if (i != columnAmount)
                result.append("   ");
        }
        result.append("\n");
        while (rs.next()) {
            int i = 1;
            while (true) {
                try {
                    String str = format("%1$10s", rs.getString(i++));
                    if (i != 2) {
                        result.append("   ");
                    }
                    result.append(str);
                } catch (Exception e) {
                    result.append("\n");
                    break;
                }
            }
        }
//        statement.executeUpdate(creDelUptArea.getText().trim());
        System.out.println(result);
    }
}
