package com.example;

import com.netflix.loadbalancer.RandomRule;
import feign.Logger.Level;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@EnableFeignClients
@SpringBootApplication
@MapperScan("com.example.mapper")
public class ServiceConsumerApplication {

//  @Bean
//  public Level getLog() {
//    return Level.FULL;
//  }

  @Bean
  public RandomRule randomRule() {
    return new RandomRule();
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(ServiceConsumerApplication.class,args);
  }

}
