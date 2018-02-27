package ru.whatsleft.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {

    public static LocalDateTime createLocalDateTime(Instant instant, String timeZone) {
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of(timeZone));
        return ldt;
    }
}
