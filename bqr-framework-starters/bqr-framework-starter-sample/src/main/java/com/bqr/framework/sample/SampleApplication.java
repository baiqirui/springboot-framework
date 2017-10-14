package com.bqr.framework.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bqr")
public class SampleApplication
{
    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(SampleApplication.class);
        application.run(args);
    }
}


