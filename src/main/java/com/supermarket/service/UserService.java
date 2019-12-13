package com.supermarket.service;

import com.supermarket.bean.User;
import com.supermarket.common.ServerResponse;

/**
 * Created by ASUS on 2019/11/12.
 */
public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse checkAdminRole(User user);
}
