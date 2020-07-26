package com.angsi.shop.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author billzhuang
 * @since
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger2",value = {"enable"},havingValue = "true")
public class SwaggerConfig {
    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 详细定制
                .apiInfo(apiInfo("1.0.0"))
                .select()
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.angsi.shop.web.rest"))
                // 扫描所有
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo(String version) {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                .title("店铺接口文档")
                .version(version)
                .build();
    }
}
