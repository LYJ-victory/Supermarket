package com.supermarket.controller.backed;

import com.supermarket.bean.User;
import com.supermarket.common.Const;
import com.supermarket.common.ServerResponse;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by ASUS on 2019/11/12.
 */
@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private UserService userService;

    /**
     * 登录：
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = userService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }


}
