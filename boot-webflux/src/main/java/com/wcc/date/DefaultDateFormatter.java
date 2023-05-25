package com.wcc.date;


import java.util.Date;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DefaultDateFormatter implements DateFormatter {
    private final DateTimeFormatter formatter;
    private final Predicate<String> predicate;
    private String formatterString;

    public DefaultDateFormatter(Pattern formatPattern, String formatter) {
        this((str) -> {
            return formatPattern.matcher(str).matches();
        }, DateTimeFormat.forPattern(formatter));
        this.formatterString = formatter;
    }

    public DefaultDateFormatter(Predicate<String> predicate, DateTimeFormatter formatter) {
        this.predicate = predicate;
        this.formatter = formatter;
    }

    public boolean support(String str) {
        return this.predicate.test(str);
    }

    public Date format(String str) {
        return this.formatter.parseDateTime(str).toDate();
    }

    public String getPattern() {
        return this.formatterString;
    }

    public String toString(Date date) {
        return (new DateTime(date)).toString(this.getPattern());
    }
}
