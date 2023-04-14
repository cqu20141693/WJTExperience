package com.witeam.logback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
    private static final Logger debugLogger = LoggerFactory.getLogger("com.witeam.log.debugComponent");
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        while (true){
            Thread.sleep(500);
            log.info("import org.springframework.boot.autoconfigure.SpringBootApplication");
            log.debug("import org.springframework.boot.SpringApplication");
            debugLogger.info("import org.slf4j.LoggerFactory");
            debugLogger.debug("import org.slf4j.Logger");
        }

    }
}
