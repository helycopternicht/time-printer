package ee.neotech.timeprinter.service.impl;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.repository.DateEntityRepository;
import ee.neotech.timeprinter.service.DateEntityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DateEntityServiceImpl implements DateEntityService {

    private final DateEntityRepository repository;

    public DateEntityServiceImpl(DateEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DateEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(DateEntity entity) {
        repository.save(entity);
    }
}
