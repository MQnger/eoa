package com.xxxx.server;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("com.xxxx.server.mapper")
public class EoaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EoaServerApplication.class, args);
    }

}
