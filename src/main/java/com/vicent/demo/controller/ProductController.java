package com.vicent.demo.controller;

import com.vicent.demo.entity.Product;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") String id){
        Product product = new Product();
        product.setId(id);
        product.setName("test");
        product.setPrice(200);

        return product;
    }
}
