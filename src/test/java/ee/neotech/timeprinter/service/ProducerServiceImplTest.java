package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.service.impl.ProducerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;

public class ProducerServiceImplTest {

    private ProducerService producerService;
    private TimeQueue queue;

    @Before
    public void init() {
        queue = Mockito.mock(TimeQueue.class);
        producerService = new ProducerServiceImpl(queue);
    }

    @Test
    public void run_Should_push_timestamp_to_queue_only_once() {
        producerService.run();
        Mockito.verify(queue, Mockito.times(1)).push(Mockito.any(Timestamp.class));
    }
}
