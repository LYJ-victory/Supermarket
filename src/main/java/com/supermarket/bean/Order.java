package com.supermarket.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer productId;

    private BigDecimal sumprice;

    private Date createTime;

    private String beizu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
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

    public BigDecimal getSumprice() {
        return sumprice;
    }

    public void setSumprice(BigDecimal sumprice) {
        this.sumprice = sumprice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBeizu() {
        return beizu;
    }

    public void setBeizu(String beizu) {
        this.beizu = beizu == null ? null : beizu.trim();
    }
}