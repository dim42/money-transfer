package pack.transfer.api;

import java.sql.Connection;

public interface DBManager {
    Connection getConnection();

    String getSql(String sqlName);
}
