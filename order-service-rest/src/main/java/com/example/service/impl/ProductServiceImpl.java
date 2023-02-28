package com.example.service.impl;

import com.example.pojo.Product;
import com.example.service.IProductService;
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
  @Cacheable(cacheNames = "orderService:product:list")
  @Override
  public List<Product> selectProductList() {
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
  @Override
  public List<Product> selectProductListByIds(List<Integer> ids) {
    System.out.println("-----orderService-----selectProductListByIds-----");
    StringBuffer sb = new StringBuffer();
    ids.forEach(id -> sb.append("id=" + id + "&"));
    return restTemplate.getForObject("http://product-service/product/listByIds?" + sb.toString(), List.class);
  }

  /**
   * 功能描述: 根据主键查询商品
   *
   * @param id
   * @return
   */
  @Cacheable(cacheNames = "orderService:product:single", key = "#id")
  @Override
  public Product selectProductById(Integer id) {
    return restTemplate.getForObject("http://product-service/product/" + id, Product.class);
  }

}
