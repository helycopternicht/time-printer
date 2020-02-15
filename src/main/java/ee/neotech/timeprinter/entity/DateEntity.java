package ee.neotech.timeprinter.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class DateEntity {
    private Long id;
    private Timestamp timestamp;

    public DateEntity(Long id, Timestamp timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateEntity)) return false;
        DateEntity that = (DateEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimestamp());
    }
}
