package com.angsi.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: BillZhuang
 * @Date:
 */
@SpringBootApplication(scanBasePackages = { "com.angsi" },exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class ShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopApplication.class, args);
  }
}
