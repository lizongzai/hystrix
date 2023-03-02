package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableHystrixDashboard // 开启数据监控注解
@EnableHystrix  // 开启熔断器注解 2 选 1，@EnableHystrix 封装了 @EnableCircuitBreaker
@EnableTurbine // 开启数据监控注解
@SpringBootApplication
public class HystrixTurbine {

  public static void main(String[] args) {
    SpringApplication.run(HystrixTurbine.class,args);
  }

}
