package com.wcc.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.joda.time.DateTime;

public class SampleJDKDateFormatter implements DateFormatter {
    Predicate<String> predicate;
    Supplier<SimpleDateFormat> formatSupplier;

    public SampleJDKDateFormatter(Predicate<String> predicate, Supplier<SimpleDateFormat> formatSupplier) {
        this.predicate = predicate;
        this.formatSupplier = formatSupplier;
    }

    public boolean support(String str) {
        return this.predicate.test(str);
    }

    public Date format(String str) {
        try {
            return this.formatSupplier.get().parse(str);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String getPattern() {
        return this.formatSupplier.get().toPattern();
    }

    public String toString(Date date) {
        return (new DateTime(date)).toString(this.getPattern());
    }
}
