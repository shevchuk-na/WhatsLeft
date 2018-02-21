package ru.whatsleft.service;

import ru.whatsleft.domain.Product;

public interface ProductService {

    Product findById(Long id);

    Product save(Product product);

    Product findByName(String name);
}
