package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import ee.neotech.timeprinter.service.impl.DateEntityServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class DateEntityServiceImplTest {

    private DateEntityRepository repository;
    private DateEntityService service;

    @Before
    public void init() {
        repository = Mockito.mock(DateEntityRepository.class);
        service = new DateEntityServiceImpl(repository);
    }

    @Test
    public void save_Should_pass_entity_to_repository() {
        DateEntity entity = new DateEntity(null, new Timestamp(1000));

        service.save(entity);

        Mockito.verify(repository).save(entity);
    }

    @Test
    public void findAll_Should_return_list_of_entities_from_repository() {
        List<DateEntity> dateEntities = Collections.singletonList(new DateEntity(null, new Timestamp(1000)));
        Mockito.when(repository.findAll()).thenReturn(dateEntities);

        List<DateEntity> actualList = service.findAll();

        Assert.assertEquals(dateEntities, actualList);
    }
}
