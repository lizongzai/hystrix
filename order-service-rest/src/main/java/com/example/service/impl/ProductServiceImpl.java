package com.example.service.impl;

import com.example.pojo.Product;
import com.example.service.IProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lizongzai
 * @since 2023-02-23
 */
@Service
public class ProductServiceImpl implements IProductService {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private LoadBalancerClient loadBalancerClient; //Ribbon负载均衡器

  /**
   * 功能描述: 查询商品列表
   *
   * @return
   */
  // 声明需要服务容错的方法
  // 线程池隔离
  @HystrixCommand(groupKey = "order-productService-listPool",// 服务名称，相同名称使用同一个线程池
      commandKey = "selectProductList",// 接口名称，默认为方法名
      threadPoolKey = "order-productService-listPool",// 线程池名称，相同名称使用同一个线程池
      commandProperties = {
          // 超时时间，默认 1000ms
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
      },
      threadPoolProperties = {
          // 线程池大小
          @HystrixProperty(name = "coreSize", value = "6"),
          // 队列等待阈值(最大队列长度，默认 -1)
          @HystrixProperty(name = "maxQueueSize", value = "100"),
          // 线程存活时间，默认 1min
          @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
          // 超出队列等待阈值执行拒绝策略
          @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100")
      }, fallbackMethod = "selectProductListFallback") // 托底数据
  @Cacheable(cacheNames = "orderService:product:list")
  @Override
  public List<Product> selectProductList() {
    System.out.println(
        Thread.currentThread().getName() + "-----orderService-----selectProductList-----");
    return restTemplate.exchange("http://product-service/product/list",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<Product>>() {
        }).getBody();
  }

  /**
   * 功能描述: 根据多个主键查询商品
   *
   * @param ids
   * @return
   */
  @Cacheable(cacheNames = "orderService:product:listByIds")
  @Override
  public List<Product> selectProductListByIds(List<Integer> ids) {
    System.out.println(
        Thread.currentThread().getName() + "-----orderService-----selectProductListByIds-----");
    StringBuffer sb = new StringBuffer();
    ids.forEach(id -> sb.append("id=" + id + "&"));
    return restTemplate.getForObject("http://product-service/product/listByIds?" + sb.toString(),
        List.class);
  }

  /**
   * 功能描述: 根据主键查询商品
   *
   * @param id
   * @return
   */
  // 声明需要服务容错的方法
  // 线程池隔离
  @HystrixCommand(groupKey = "order-productService-singlePool",// 服务名称，相同名称使用同一个线程池
      commandKey = "selectProductById",// 接口名称，默认为方法名
      threadPoolKey = "order-productService-singlePool",// 线程池名称，相同名称使用同一个线程池
      commandProperties = {
          // 超时时间，默认 1000ms
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
              value = "5000")
      },
      threadPoolProperties = {
          // 线程池大小
          @HystrixProperty(name = "coreSize", value = "3"),
          // 队列等待阈值(最大队列长度，默认 -1)
          @HystrixProperty(name = "maxQueueSize", value = "100"),
          // 线程存活时间，默认 1min
          @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
          // 超出队列等待阈值执行拒绝策略
          @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100")
      })
  @Cacheable(cacheNames = "orderService:product:single", key = "#id")
  @Override
  public Product selectProductById(Integer id) {
    System.out.println(
        Thread.currentThread().getName() + "-----orderService-----selectProductById-----");
    return restTemplate.getForObject("http://product-service/product/" + id, Product.class);
  }


  // 托底数据
  private List<Product> selectProductListFallback() {
    System.out.println("-----selectProductListFallback-----");
    return Arrays.asList(
        new Product(1, "托底数据-华为手机", 1, 5800D),
        new Product(2, "托底数据-联想笔记本", 1, 6888D),
        new Product(3, "托底数据-小米平板", 5, 2020D)
    );
  }

}
