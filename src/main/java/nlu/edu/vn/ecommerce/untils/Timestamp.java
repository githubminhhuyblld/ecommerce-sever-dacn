package nlu.edu.vn.ecommerce.untils;
import java.time.*;

public class Timestamp {
    public long getTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return now.toInstant(ZoneOffset.of("+07:00")).toEpochMilli();
    }

    public static long convertLocalDateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
