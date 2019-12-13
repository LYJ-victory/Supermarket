package com.supermarket.service.impl;

import com.supermarket.bean.User;
import com.supermarket.common.Const;
import com.supermarket.common.ServerResponse;
import com.supermarket.dao.UserMapper;
import com.supermarket.service.UserService;
import com.supermarket.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ASUS on 2019/11/12.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
