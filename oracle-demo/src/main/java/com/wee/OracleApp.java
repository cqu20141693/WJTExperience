package com.wee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.wee.oracle.mapper"})
public class OracleApp {
    public static void main(String[] args) {
        SpringApplication.run(OracleApp.class, args);
    }
}
