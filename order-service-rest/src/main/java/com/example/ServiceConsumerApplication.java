package com.example;

import com.netflix.loadbalancer.RandomRule;
import feign.Logger.Level;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@EnableHystrix // 开启熔断器注解 2 选 1，@EnableHystrix 封装了 @EnableCircuitBreaker
//@EnableCaching // 开启缓存注解
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
    SpringApplication.run(ServiceConsumerApplication.class, args);
  }

}
