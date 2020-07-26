package com.angsi.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties(prefix = "shop",ignoreUnknownFields = false)
@Data
@RefreshScope
@Configuration
public class ApplicationProperties {
    private String bigShopId;
}
