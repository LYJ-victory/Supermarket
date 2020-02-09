package com.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Cart;
import com.supermarket.bean.Product;
import com.supermarket.bean.TbOrder;
import com.supermarket.bean.UserAddress;
import com.supermarket.bean.vo.OrderManageDetal;
import com.supermarket.bean.vo.OrderManageVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ASUS on 2020/2/3.
 */
public interface OrderService {
    String createPersonOrder(Integer id, List<Product> productList, String beizu, String sumPrice);


    List<OrderManageDetal> getProductListByOrderId(String orderId,Integer userId );

    String updateOrderStatus(String orderNo);

    int countOrderSum();

    BigDecimal countMoney();

    PageInfo getAllOrder(int pageNum, int pageSize);

    TbOrder getBeiZhu(String orderNo);

    UserAddress getOrderAddressInfo(String orderNo);
}
