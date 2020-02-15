package ee.neotech.timeprinter.service.impl;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.service.ConsumerService;
import ee.neotech.timeprinter.service.DateEntityService;
import ee.neotech.timeprinter.service.TimeQueue;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final TimeQueue queue;
    private final DateEntityService dateEntityService;

    public ConsumerServiceImpl(TimeQueue queue, DateEntityService dateEntityService) {
        this.queue = queue;
        this.dateEntityService = dateEntityService;
    }

    private void consume() throws InterruptedException {
        Timestamp timestamp = queue.pop();
        dateEntityService.save(new DateEntity(null, timestamp));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                consume();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
