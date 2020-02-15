package ee.neotech.timeprinter.repository;

import ee.neotech.timeprinter.entity.DateEntity;

import java.util.List;

public interface DateEntityRepository {
    void save(DateEntity entity);
    List<DateEntity> findAll();
}
