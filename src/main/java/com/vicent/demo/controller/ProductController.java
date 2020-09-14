package com.vicent.demo.controller;

import com.vicent.demo.entity.Product;
import com.vicent.demo.parameter.ProductQueryParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    private void initDB(){
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Product> replceProduct(@PathVariable("id") String id, @RequestBody Product request){
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if(!productOp.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return ResponseEntity.ok().body(product);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id){
        boolean isRemoved = productDB.removeIf(p -> p.getId().equals(id));

        if(isRemoved){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getProducts(
            @ModelAttribute ProductQueryParameter param){

        String namekeyword = param.getKeyword();
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Comparator<Product> comparator = Objects.nonNull(orderBy) && Objects.nonNull(sortRule)
                ? configureSortComparator(orderBy, sortRule) : (p1, p2) -> 0;

        List<Product> products = productDB.stream()
                .filter(p -> p.getName().toUpperCase().contains(namekeyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }

    private Comparator<Product> configureSortComparator(String orderBy, String sortRule){
        Comparator<Product> comparator = (P1, p2) -> 0;

        if(orderBy.equalsIgnoreCase("price")){
            comparator = Comparator.comparing(Product::getPrice);
        }else if(orderBy.equalsIgnoreCase("name")){
            comparator = Comparator.comparing(Product::getName);
        }

        if(sortRule.equalsIgnoreCase("desc")){
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
