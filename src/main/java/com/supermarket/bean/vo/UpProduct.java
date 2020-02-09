package com.supermarket.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by ASUS on 2020/1/15.
 */
@Data
public class UpProduct {

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private String img;

    private String desc;
}
