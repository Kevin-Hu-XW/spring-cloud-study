package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author Admin
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(AccountApp.class,args);
    }
}