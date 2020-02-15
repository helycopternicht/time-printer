package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.service.impl.FormatterServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class FormatterServiceImplTest {

    private final FormatterService service = new FormatterServiceImpl();

    @Test
    public void getStringRepresentation_Should_return_formatted_string() {
        List<DateEntity> entities = Collections.singletonList(new DateEntity(1L, new Timestamp(1000)));
        String actualResult = service.getStringRepresentation(entities);

        Assert.assertEquals("{id: 1, time: 06:00:01}\n", actualResult);
    }
}
