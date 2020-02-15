package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;

import java.util.List;

public interface DateEntityService {
    List<DateEntity> findAll();
    void save(DateEntity entity);
}
