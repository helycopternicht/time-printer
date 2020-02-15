package ee.neotech.timeprinter.database.impl;

import ee.neotech.timeprinter.database.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.concurrent.TimeUnit;

@Service
public class MonoConnectionPool implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonoConnectionPool.class);

    private static final int DATABASE_RECONNECTION_INTERVAL_SEC = 5;
    private static final int CONNECTION_VALIDATE_TIMEOUT_SEC = 1;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pwd;

    private Connection connection;

    @Override
    public Connection getConnection() {
        boolean success = false;
        while (!success) {
            try {
                if (connection != null && connection.isValid(CONNECTION_VALIDATE_TIMEOUT_SEC)) {
                    return connection;
                }
                tryToClose(connection);
                connection = DriverManager.getConnection(url, user, pwd);
                success = true;
                LOGGER.info("Connection successful established");
            } catch (SQLTimeoutException e) {
                LOGGER.error("Connection timeout exceeded. Try to reconnect", e);
                waitForTimeout();
            } catch (SQLException e) {
                LOGGER.error("Database is not reachable at the moment. Try to reconnect", e);
                waitForTimeout();
            }
        }
        return connection;
    }

    private void waitForTimeout() {
        try {
            TimeUnit.SECONDS.sleep(DATABASE_RECONNECTION_INTERVAL_SEC);
        } catch (InterruptedException e) {
            LOGGER.error("Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private void tryToClose(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            connection = null;
            LOGGER.error("Failed to close connection", e);
        }
    }
}
