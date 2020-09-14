package com.vicent.demo.controller;

import com.vicent.demo.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    private void initDB(){
        productDB.add(new Product("B0001", "testB0001", 380));
        productDB.add(new Product("B0002", "testB0002", 420));
        productDB.add(new Product("B0003", "testB0003", 250));
        productDB.add(new Product("B0004", "testB0004", 450));
        productDB.add(new Product("B0005", "testB0005", 330));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if(!productOp.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product request){
        boolean isIdDuplicated = productDB.stream().anyMatch(p -> p.getId().equals(request.getId()));

        if(isIdDuplicated){
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }
}