package com.supermarket.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ASUS on 2020/2/4.
 */
@Data
public class OrderManageDetal {
    //商品id:
    private int productId;
    private String img;
    private String name;
    //数量：
    private int quantity;
    //总额：
    private Double sumMoney;
    //备注：
    private String beizu;
    //时间：
    private Date createTime;
}
