package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.service.impl.ConsumerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class ConsumerServiceImplTest {

    private TimeQueue queue;
    private DateEntityService service;
    private ConsumerService consumerService;

    @Before
    public void init() {
        queue = Mockito.mock(TimeQueue.class);
        service = Mockito.mock(DateEntityService.class);
        consumerService = new ConsumerServiceImpl(queue, service);
    }

    @Test
    public void run_Should_take_timestamp_from_queue_and_pass_it_to_service() throws InterruptedException {
        // arrange
        Timestamp timestamp = new Timestamp(1000);
        DateEntity entity = new DateEntity(null, timestamp);

        when(queue.pop()).thenReturn(timestamp);

        // act
        Thread thread = new Thread(consumerService);
        thread.start();
        TimeUnit.SECONDS.sleep(1);

        // assert
        verify(queue, atLeastOnce()).pop();
        verify(service, atLeastOnce()).save(eq(entity));
        thread.interrupt();
    }
}
