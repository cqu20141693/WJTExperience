package com.wcc.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.joda.time.DateTime;

public interface DateFormatter {
    List<DateFormatter> supportFormatter = new ArrayList(Arrays.asList(new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}"), "yyyyMMdd"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"), "yyyy-MM-dd"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}"), "yyyy/MM/dd"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日"), "yyyy年MM月dd日"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]日"), "yyyy年M月d日"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]日"), "yyyy年MM月d日"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]{2}日"), "yyyy年M月dd日"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]"), "yyyy-M-d"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]"), "yyyy-MM-d"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2}"), "yyyy-M-dd"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]"), "yyyy/M/d"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]"), "yyyy/MM/d"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2}"), "yyyy/M/dd"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-d HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-M-d H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-d HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-dd HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy-M-dd H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-MM-d H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/dd HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/d HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/M/d H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/d HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/dd HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy/M/dd H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/MM/d H:m:s"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd HH:mm:ssZ"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd'T'HH:mm:ssZ"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日[0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日HH时mm分ss秒"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日 [0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日 HH时mm分ss秒"), new DefaultDateFormatter(Pattern.compile("[0-9]{2}:[0-9]{2}:[0-9]{2}"), "HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyyMMdd HH:mm:ss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMddHHmmss"), new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMdd HHmmss"), new SampleJDKDateFormatter((str) -> {
        return str.contains("年") && str.contains("CST") && str.split("[ ]").length == 6;
    }, () -> {
        return new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'", Locale.CHINESE);
    }), new SampleJDKDateFormatter((str) -> {
        return str.contains("年") && str.contains("GMT") && str.split("[ ]").length == 6;
    }, () -> {
        return new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'", Locale.CHINESE);
    }), new SampleJDKDateFormatter((str) -> {
        return str.contains("CST") && str.split("[ ]").length == 6;
    }, () -> {
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
    }), new SampleJDKDateFormatter((str) -> {
        return str.contains("GMT") && str.split("[ ]").length == 6;
    }, () -> {
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
    }), new SampleJDKDateFormatter((str) -> {
        return str.endsWith("AM") || str.endsWith("PM") && str.split("[ ]").length == 5;
    }, () -> {
        return new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
    })));

    boolean support(String var1);

    Date format(String var1);

    String toString(Date var1);

    String getPattern();

    static Date fromString(String dateString) {
        DateFormatter formatter = getFormatter(dateString);
        return formatter != null ? formatter.format(dateString) : null;
    }

    static Date fromString(String dateString, String pattern) {
        try {
            return (new SimpleDateFormat(pattern)).parse(dateString);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    static String toString(Date date, String format) {
        if (null == date) {
            return null;
        } else {
            Iterator var2 = supportFormatter.iterator();

            DateFormatter formatter;
            do {
                if (!var2.hasNext()) {
                    return (new DateTime(date)).toString(format);
                }

                formatter = (DateFormatter)var2.next();
            } while(!formatter.getPattern().equals(format));

            return formatter.toString(date);
        }
    }

    static boolean isSupport(String dateString) {
        return dateString != null && dateString.length() >= 4 && supportFormatter.parallelStream().anyMatch((formatter) -> {
            return formatter.support(dateString);
        });
    }

    static DateFormatter getFormatter(String dateString) {
        if (dateString != null && dateString.length() >= 4) {
            Iterator var1 = supportFormatter.iterator();

            DateFormatter formatter;
            do {
                if (!var1.hasNext()) {
                    return null;
                }

                formatter = (DateFormatter)var1.next();
            } while(!formatter.support(dateString));

            return formatter;
        } else {
            return null;
        }
    }
}
