package com.wcc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class HelloWorld {

   private static Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    public static void main(String[] args) {

        System.out.println("hello world");
        logger.info("hello world");
        logger.warn("hello {},I am {}","world","wu xin");
        System.out.println();
    }
}
