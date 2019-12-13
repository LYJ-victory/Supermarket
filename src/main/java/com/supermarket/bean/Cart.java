package com.supermarket.bean;

public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer checked;

    private Integer quantity;

    private Double cartSumprice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getCartSumprice() {
        return cartSumprice;
    }

    public void setCartSumprice(Double cartSumprice) {
        this.cartSumprice = cartSumprice;
    }
}