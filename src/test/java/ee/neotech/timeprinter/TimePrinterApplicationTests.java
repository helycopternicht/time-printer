package ee.neotech.timeprinter;

import ee.neotech.timeprinter.entity.DateEntity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pwd;


    @BeforeEach
    public void init() {
        clear();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void run_WhenApplicationStarted_ThereShouldBeRowsInTable() throws Exception {
        runner.run();
        TimeUnit.SECONDS.sleep(2);

		Assert.assertFalse(getAll().isEmpty());
    }

	@Test
	void run_WhenApplicationStarted_ThereShouldBeRowsInRightOrder() throws Exception {
		runner.run();
		TimeUnit.SECONDS.sleep(5);

		List<DateEntity> all = getAll();

		List<DateEntity> sortedById = all.stream()
				.sorted(Comparator.comparing(DateEntity::getId))
				.collect(Collectors.toList());

		List<DateEntity> sortedByTimestamp = all.stream()
				.sorted(Comparator.comparing(DateEntity::getTimestamp))
				.collect(Collectors.toList());

		Assert.assertEquals(sortedById, sortedByTimestamp);
	}

    private void clear() {
        try (Connection connection = DriverManager.getConnection(url, user, pwd);
             PreparedStatement ps = connection.prepareStatement("DELETE FROM times")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<DateEntity> getAll() {
		List<DateEntity> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, pwd);
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
