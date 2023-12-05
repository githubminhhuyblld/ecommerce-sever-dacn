package nlu.edu.vn.ecommerce.untils;

import nlu.edu.vn.ecommerce.base.BaseEntity;

import java.time.*;

public class Timestamp {
    public long getTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Instant instant = now.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

    public static long convertLocalDateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
