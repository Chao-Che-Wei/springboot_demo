package com.vicent.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "products")
public class Product {
    private String id;

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @Min(value = 0, message = "Price should be positive or 0.")
    private int price;

    public Product(){
    }

    public Product(String id, String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
