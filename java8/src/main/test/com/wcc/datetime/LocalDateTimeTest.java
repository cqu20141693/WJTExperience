package com.wcc.datetime;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest {

    @Test
    public void testFormat() {
        LocalDateTime currHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime preHour = currHour.minusHours(1);
        long epochMilli = currHour.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Timestamp currHourStamp = new Timestamp(epochMilli);
        Timestamp preHourStamp = new Timestamp(preHour.toInstant(ZoneOffset.of("+8")).toEpochMilli());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = formatter.format(currHour);
        System.out.println(format);
        LocalDateTime parse1 = LocalDateTime.parse(format,formatter);
        long epochMilli1 = parse1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        boolean x = epochMilli == epochMilli1;
        System.out.println(x);
    }
}
