package pack.transfer.api;

import java.sql.Connection;

public interface DBManager {
    String PROP_FILE_NAME = "sql.xml";
    String DB_DRIVER_CLASS_NAME = "dbClass";
    String SCHEMA = "%SCHEMA%";
    String DB_NAME = "dbName";

    Connection getConnection();

    String getSql(String sqlName);
}
