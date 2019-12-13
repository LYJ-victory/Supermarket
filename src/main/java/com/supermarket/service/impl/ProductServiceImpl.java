package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.supermarket.bean.Product;
import com.supermarket.common.ServerResponse;
import com.supermarket.dao.ProductMapper;
import com.supermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ASUS on 2019/12/13.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse getProductList(int pageNum, int pageSize) {
        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productList);
        return ServerResponse.createBySuccess(pageResult);
    }
}
