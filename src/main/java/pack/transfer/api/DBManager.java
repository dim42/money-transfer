package pack.transfer.api;

import java.sql.Connection;

public interface DBManager {
    String PROP_FILE_NAME = "sql.xml";
    String SCHEMA = "%SCHEMA%";

    Connection getConnection();

    String getSql(String sqlName);
}
