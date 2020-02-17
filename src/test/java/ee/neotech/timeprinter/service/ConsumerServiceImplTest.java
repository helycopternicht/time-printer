package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import ee.neotech.timeprinter.service.impl.ConsumerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class ConsumerServiceImplTest {

    private TimeQueue queue;
    private DateEntityRepository repository;
    private ConsumerService consumerService;

    @Before
    public void init() {
        queue = Mockito.mock(TimeQueue.class);
        repository = Mockito.mock(DateEntityRepository.class);
        consumerService = new ConsumerServiceImpl(queue, repository);
    }

    @Test
    public void run_ShouldTakeTimestampFromQueueAndPassItToService() throws InterruptedException {
        // arrange
        Timestamp timestamp = new Timestamp(1000);
        DateEntity entity = new DateEntity(null, timestamp);

        when(queue.pop()).thenReturn(timestamp);

        // act
        Thread thread = new Thread(consumerService);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(100);
        thread.interrupt();

        // assert
        verify(queue, atLeastOnce()).pop();
        verify(repository, atLeastOnce()).save(eq(entity));
    }
}
