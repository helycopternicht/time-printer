package ee.neotech.timeprinter;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import ee.neotech.timeprinter.service.ConsumerService;
import ee.neotech.timeprinter.service.FormatterService;
import ee.neotech.timeprinter.service.ProducerService;
import ee.neotech.timeprinter.service.impl.ConsumerServiceImpl;
import ee.neotech.timeprinter.service.impl.ProducerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class TimePrinterApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimePrinterApplication.class);
    private static final int PRODUCING_INTERVAL_SEC = 1;
    private static final int PRODUCING_INITIAL_DELAY_SEC = 0;

    private final ProducerService producerService;
    private final ConsumerService consumerService;
    private final FormatterService formatterService;
    private final DateEntityRepository repository;

    @Autowired
    public TimePrinterApplication(ProducerServiceImpl producer,
                                  ConsumerServiceImpl consumer,
                                  FormatterService formatterService,
                                  DateEntityRepository repository) {
        this.producerService = producer;
        this.consumerService = consumer;
        this.formatterService = formatterService;
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TimePrinterApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            LOGGER.info("Application started in write mode");
            runApplication();
        } else if (args.length == 1 && "-p".equalsIgnoreCase(args[0])) {
            LOGGER.info("Application started in print mode");
            print();
        } else {
            LOGGER.info("Invalid argument. Can't start application");
        }
    }

    private void runApplication() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(producerService, PRODUCING_INITIAL_DELAY_SEC, PRODUCING_INTERVAL_SEC, TimeUnit.SECONDS);
        executor.execute(consumerService);
    }

    private void print() {
        List<DateEntity> all = repository.findAll();
        System.out.println("All results");
        System.out.println("-------------------------------------------");
        System.out.println(formatterService.getStringRepresentation(all));
    }
}
