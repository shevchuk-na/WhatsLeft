package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findById(Long id);

    Product findByName(String name);
}
