package nlu.edu.vn.ecommerce.untils;

import nlu.edu.vn.ecommerce.base.BaseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Timestamp {
    public long getTime() {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

}
