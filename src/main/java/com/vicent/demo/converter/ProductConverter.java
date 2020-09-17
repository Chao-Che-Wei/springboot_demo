package com.vicent.demo.converter;

import com.vicent.demo.entity.Product;
import com.vicent.demo.entity.ProductRequest;

public class ProductConverter {

    private ProductConverter(){
    }

    public static Product toProduct(ProductRequest request){
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}
