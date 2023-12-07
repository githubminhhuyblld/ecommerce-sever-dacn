package nlu.edu.vn.ecommerce.untils;
import java.time.*;
import java.util.Date;

public class Timestamp {
    public long getTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return now.toInstant(ZoneOffset.of("+07:00")).toEpochMilli();
    }

    public long convertLocalDateToTimestamp(LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        return Date.from(instant).getTime();
    }

}
