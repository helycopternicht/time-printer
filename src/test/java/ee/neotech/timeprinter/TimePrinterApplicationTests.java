package ee.neotech.timeprinter;

import ee.neotech.timeprinter.config.PropertyHolder;
import ee.neotech.timeprinter.entity.DateEntity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class TimePrinterApplicationTests {

    @Autowired
    private CommandLineRunner runner;

    @Autowired
    private PropertyHolder propertyHolder;

    @BeforeEach
    void init() {
        clear();
    }

    @Test
    void run_WhenApplicationWorkSomeTime_ShouldBeRowsInTable() throws Exception {
        runner.run();
        TimeUnit.SECONDS.sleep(1);

		Assert.assertFalse(getAll().isEmpty());
    }

	@Test
	void run_WhenApplicationStarted_ShouldBeRowsInRightOrder() throws Exception {
		runner.run();
		TimeUnit.SECONDS.sleep(5);

		List<DateEntity> original = getAll();
		List<DateEntity> sortedByTimestamp = original.stream()
				.sorted(Comparator.comparing(DateEntity::getTimestamp))
				.collect(Collectors.toList());

		Assert.assertEquals(original, sortedByTimestamp);
	}

    private void clear() {
        try (Connection connection = DriverManager.getConnection(propertyHolder.getUrl(), propertyHolder.getUser(), propertyHolder.getPwd());
             PreparedStatement ps = connection.prepareStatement("DELETE FROM times")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<DateEntity> getAll() {
		List<DateEntity> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(propertyHolder.getUrl(), propertyHolder.getUser(), propertyHolder.getPwd());
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM times");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
				list.add(new DateEntity(rs.getLong(1), rs.getTimestamp(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
