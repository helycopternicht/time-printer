package ee.neotech.timeprinter.database;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();
}
