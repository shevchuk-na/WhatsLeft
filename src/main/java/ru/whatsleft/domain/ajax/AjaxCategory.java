package ru.whatsleft.domain.ajax;

import ru.whatsleft.domain.Category;
import ru.whatsleft.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class AjaxCategory {

    private Long id;
    private String name;
    private List<AjaxProduct> products;

    public AjaxCategory(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.products = new ArrayList<>();
        for (Product product : category.getProducts()) {
            AjaxProduct ajaxProduct = new AjaxProduct(product);
            this.products.add(ajaxProduct);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AjaxProduct> getProducts() {
        return products;
    }

    public void setProducts(List<AjaxProduct> products) {
        this.products = products;
    }
}
