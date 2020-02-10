package com.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.User;
import com.supermarket.common.ServerResponse;

import java.util.List;

/**
 * Created by ASUS on 2019/11/12.
 */
public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse checkAdminRole(User user);

    PageInfo getUserList(int pageNum, int pageSize);

    String insertUser(String productId, String username, String password, String role);

    User selectLookPerson(String id);

    String registerUser(String username, String password);

    int countAll();

    PageInfo selectAllPerson(int pageNum, int pageSize);

    String deleteMoreProductByIds(List<Integer> ids);

    String updatePerson(String productId, String username, String password, String role);

    String deletePersonById(Integer userId);
}
