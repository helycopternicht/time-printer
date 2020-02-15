package ee.neotech.timeprinter.repository.impl;

import ee.neotech.timeprinter.database.ConnectionPool;
import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateEntityRepositoryImpl implements DateEntityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateEntityRepositoryImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO times(date_time) VALUES(?)";
    private static final String SELECT_ALL_QUERY = "SELECT id, date_time FROM times";

    private final ConnectionPool connectionPool;

    public DateEntityRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void save(DateEntity entity) {
        boolean success = false;
        Connection connection = connectionPool.getConnection();
        while (!success) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
                ps.setTimestamp(1, entity.getTimestamp());
                ps.executeUpdate();
                success = true;
            } catch (SQLException e) {
                LOGGER.error("Error while execution query. Trying to retry", e);
                connection = connectionPool.getConnection();
            }
        }
    }

    @Override
    public List<DateEntity> findAll() {
        boolean success = false;
        final List<DateEntity> dateEntities = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        while (!success) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_QUERY);
                ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    dateEntities.add(new DateEntity(resultSet.getLong(1), resultSet.getTimestamp(2)));
                }
                success = true;
            } catch (SQLException e) {
                LOGGER.error("Error while execution query. Trying to retry", e);
                connection = connectionPool.getConnection();
            }
        }
        return dateEntities;
    }
}
