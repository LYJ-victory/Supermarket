package com.supermarket.service;

import com.supermarket.common.ServerResponse;

/**
 * Created by ASUS on 2019/12/13.
 */
public interface ProductService {
    ServerResponse getProductList(int pageNum, int pageSize);
}
