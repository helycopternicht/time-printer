package ee.neotech.timeprinter.service;

import ee.neotech.timeprinter.entity.DateEntity;

import java.util.List;

public interface FormatterService {
    String getStringRepresentation(List<DateEntity> dateEntities);
}
