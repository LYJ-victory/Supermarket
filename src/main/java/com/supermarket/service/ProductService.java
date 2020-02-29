package com.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Product;

import java.util.List;

/**
 * Created by ASUS on 2019/12/13.
 */
public interface ProductService {
    PageInfo getProductList(int pageNum, int pageSize);

    String deleteProductById(Integer id);

    String addProduct(String name, String quantity, String price, String desc, String image,String status);

    Product selectLookProdduct(String lookProductId);

    String updateProduct(String productId, String checkResult, String name, String quantity, String price, String des,String status);

    String selectImgById(String productId);

    PageInfo getPersonProductList(int pageNum, int pageSize);

    List<Product> orderFromProductIds(String[] ids);

    int countProductSum();

    String deleteMoreProductByIds(List<Integer> ids);

    String quedingJinHuoByProductId(String productId, String jinhuonumber);

    PageInfo selectByProductName(int pageNum, int pageSize, String searchProductName);
}
