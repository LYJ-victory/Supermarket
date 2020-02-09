package com.supermarket.bean.vo;

import com.supermarket.bean.Product;
import com.supermarket.bean.TbOrder;
import lombok.Data;

import java.util.List;

/**
 * Created by ASUS on 2020/2/4.
 */
@Data
public class OrderManageVO {
    private TbOrder tbOrder;
    private String username;
    private List<Product>  productList;
}
