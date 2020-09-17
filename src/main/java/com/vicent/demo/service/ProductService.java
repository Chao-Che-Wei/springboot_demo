package com.vicent.demo.service;

import com.vicent.demo.converter.ProductConverter;
import com.vicent.demo.entity.Product;
import com.vicent.demo.entity.ProductRequest;
import com.vicent.demo.exception.ConflictException;
import com.vicent.demo.exception.NotFoundException;
import com.vicent.demo.parameter.ProductQueryParameter;
import com.vicent.demo.repository.MockProductDAO;
import com.vicent.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product createProduct(ProductRequest request) {

        Product product = ProductConverter.toProduct(request);
        return repository.insert(product);
    }

    public Product replaceProduct(String id, ProductRequest request) {

        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        return repository.save(newProduct       );
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String namekeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = configureSort(param.getOrderBy(), param.getSortRule());

        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, namekeyword, sort);
    }

    private Sort configureSort(String orderBy, String sortRule) {

        Sort sort = Sort.unsorted();
        if(Objects.nonNull(orderBy) && Objects.nonNull(sort)){
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = new Sort(direction, orderBy);
        }

        return sort;
    }
}
