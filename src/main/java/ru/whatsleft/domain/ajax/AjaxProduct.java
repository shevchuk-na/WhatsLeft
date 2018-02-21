package ru.whatsleft.domain.ajax;

import ru.whatsleft.domain.Product;

public class AjaxProduct {

    private Long id;
    private String name;
    private int amount;
    private int defaultChange;

    public AjaxProduct(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.amount = product.getAmount();
        this.defaultChange = product.getDefaultChange();
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDefaultChange() {
        return defaultChange;
    }

    public void setDefaultChange(int defaultChange) {
        this.defaultChange = defaultChange;
    }
}
