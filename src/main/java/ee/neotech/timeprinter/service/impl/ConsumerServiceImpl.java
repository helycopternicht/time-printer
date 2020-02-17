package ee.neotech.timeprinter.service.impl;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import ee.neotech.timeprinter.service.ConsumerService;
import ee.neotech.timeprinter.service.TimeQueue;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final TimeQueue queue;
    private final DateEntityRepository repository;

    public ConsumerServiceImpl(TimeQueue queue, DateEntityRepository repository) {
        this.queue = queue;
        this.repository = repository;
    }

    private void consume() throws InterruptedException {
        Timestamp timestamp = queue.pop();
        repository.save(new DateEntity(null, timestamp));
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
