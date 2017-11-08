package com.bqr.framework.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@EnableEurekaClient
//@SpringBootApplication
//@EnableHystrixDashboard
@EnableFeignClients
@EnableCircuitBreaker
@ComponentScan({"com.bqr"})
@SpringCloudApplication
public class SampleApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SampleApplication.class, args);
    }
}


