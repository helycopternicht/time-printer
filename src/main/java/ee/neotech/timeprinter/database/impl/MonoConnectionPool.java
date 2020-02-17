package ee.neotech.timeprinter.database.impl;

import ee.neotech.timeprinter.config.PropertyHolder;
import ee.neotech.timeprinter.database.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.concurrent.TimeUnit;

@Service
public class MonoConnectionPool implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonoConnectionPool.class);

    private final PropertyHolder propertyHolder;
    private Connection connection;

    public MonoConnectionPool(PropertyHolder propertyHolder) {
        this.propertyHolder = propertyHolder;
    }

    @Override
    public Connection getConnection() {
        boolean success = false;
        while (!success) {
            try {
                if (connection != null && connection.isValid(propertyHolder.getValidateTimeout())) {
                    return connection;
                }
                tryToClose(connection);
                connection = DriverManager.getConnection(propertyHolder.getUrl(), propertyHolder.getUser(), propertyHolder.getPwd());
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
            TimeUnit.SECONDS.sleep(propertyHolder.getReconnectionInterval());
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
