package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CreditProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditProjectApplication.class, args);
    }

}
