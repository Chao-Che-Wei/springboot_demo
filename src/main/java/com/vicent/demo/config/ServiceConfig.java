package com.vicent.demo.config;

import com.vicent.demo.repository.ProductRepository;
import com.vicent.demo.service.MailService;
import com.vicent.demo.service.ProductService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ServiceConfig {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ProductService productService(ProductRepository repository, MailService mailService){
        System.out.println("Create product service.");
        return new ProductService(repository, mailService);
    }
}
