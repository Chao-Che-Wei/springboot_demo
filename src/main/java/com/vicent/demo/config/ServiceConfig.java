package com.vicent.demo.config;

import com.vicent.demo.repository.ProductRepository;
import com.vicent.demo.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public ProductService productService(ProductRepository repository){
        return new ProductService(repository);
    }
}
