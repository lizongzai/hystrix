package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@MapperScan("com.example.mapper")
public class OrderServiceFeignApplication {

//  @Bean
//  public Logger.Level getLog() {
//    return Level.FULL;
//  }
//
//  @Bean
//  public RandomRule randomRule() {
//    return new RandomRule();
//  }

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceFeignApplication.class, args);
  }
}
