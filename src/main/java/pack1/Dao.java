package pack1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Dao {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
//        Statement statement = null;
        PreparedStatement statement = null;
        String user = "user";
        String password = "";
        Class.forName(org.h2.Driver.class.getName());
        String url = "jdbc:h2:~/test";
//        connection = DriverManager.getConnection(url, user, password);
        connection = DriverManager.getConnection(url);
//        statement = connection.createStatement();
//        String select="SELECT * ";
        String select = "show databases;";
        statement = connection.prepareStatement(select);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnAmount = rsmd.getColumnCount();
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= columnAmount; i++) {
            result.append(String.format("%1$10s",
                    rsmd.getColumnLabel(i)));
            if (i != columnAmount)
                result.append("   ");
        }
        result.append("\n");
        while (rs.next()) {
            int i = 1;
            while (true) {
                try {
                    String str = String.format("%1$10s", rs.getString(i++));
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
