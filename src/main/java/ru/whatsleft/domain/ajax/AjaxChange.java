package ru.whatsleft.domain.ajax;

public class AjaxChange {

    private Long productId;
    private int changeAmount;

    public AjaxChange() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }
}

