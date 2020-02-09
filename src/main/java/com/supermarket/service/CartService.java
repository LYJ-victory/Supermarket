package com.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Cart;
import com.supermarket.bean.Product;

import java.util.List;

/**
 * Created by ASUS on 2020/1/20.
 */
public interface CartService {
    String addShopProduct(Integer productId,Integer userId);

    PageInfo getProductList(int pageNum, int pageSize,int uesrId);

    String reducceShopProduct(Integer productId, Integer id);

    String delFromShopCart(Integer productId, Integer id);

    List<Product> orderFromProductIds(String[] ids, Integer id);
}
