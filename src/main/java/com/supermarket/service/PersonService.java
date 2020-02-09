package com.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.UserAddress;

/**
 * Created by ASUS on 2020/2/2.
 */
public interface PersonService {
    String updatePersonAddress(Integer id, String username, String phone, String address);

    UserAddress getPersonAddress(Integer id);

    String insertPersonAddress(Integer id, String username, String phone, String address);

    PageInfo findAllMyOrders(int pageNum, int pageSize, Integer id);
}
