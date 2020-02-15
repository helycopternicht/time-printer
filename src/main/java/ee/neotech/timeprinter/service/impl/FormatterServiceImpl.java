package ee.neotech.timeprinter.service.impl;

import ee.neotech.timeprinter.entity.DateEntity;
import ee.neotech.timeprinter.service.FormatterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatterServiceImpl implements FormatterService {

    @Override
    public String getStringRepresentation(List<DateEntity> dateEntities) {
        final StringBuilder sb = new StringBuilder();
        for (DateEntity dateEntity : dateEntities) {
            sb.append(String.format("{id: %d, time: %tT}\n", dateEntity.getId(), dateEntity.getTimestamp()));
        }
        return sb.toString();
    }
}
