package com.bqr.framework.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleWebApplication
{
    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(SampleWebApplication.class);
        application.run(args);
    }
}


