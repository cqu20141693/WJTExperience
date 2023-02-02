package com.wcc.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CSVDemo {
    Map<String, String> AUTHOR_BOOK_MAP = new HashMap<String, String>() {
        {
            put("Dan Simmons", "Hyperion");
            put("Douglas Adams", "The Hitchhiker's Guide to the Galaxy");
            put("null",null);
        }
    };
    String[] HEADERS = {"author", "title"};


    public File getResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        // 寻找classpath下的文件
        URL url = classLoader.getResource(fileName);
        assert url != null;
        return new File(url.getFile());
    }

    @Test
    public void givenCSVFile_whenRead_thenContentsAsExpected() throws IOException {
        Reader in = new FileReader(getResourceFile("book.csv"));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(in);
        for (CSVRecord record : records) {
            String author = record.get("author");
            String title = record.get("title");
            assertEquals(AUTHOR_BOOK_MAP.get(author), title);
        }
    }


    @Test
    public void createCSVFile() throws IOException {
        FileWriter out = new FileWriter("book_new.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            for (Map.Entry<String, String> entry : AUTHOR_BOOK_MAP.entrySet()) {
                String author = entry.getKey();
                String title = entry.getValue();
                printer.printRecord(author, title);
            }
        }
    }
    @Test
    public void CSVNull() throws IOException {


        String[] reportHeader= new String[]{"设备ID",  "单位", "采集时间"};
        CSVFormat format = CSVFormat.newFormat(',').withQuote(null)
                .withRecordSeparator("\r\n").withIgnoreEmptyLines(true);
        format.withHeader(reportHeader);

        FileWriter out = new FileWriter("book_new.csv");
        try (CSVPrinter printer = new CSVPrinter(out,format)) {
            printer.printRecord("1","千克", LocalDateTime.now());
            printer.printRecord("2",null, LocalDateTime.now());
        }
    }
}
