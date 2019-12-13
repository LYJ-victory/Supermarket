package com.supermarket.controller;

import com.supermarket.bean.User;
import com.supermarket.common.Const;
import com.supermarket.common.ResponseCode;
import com.supermarket.common.ServerResponse;
import com.supermarket.service.ProductService;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by ASUS on 2019/11/6.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    /**
     * 商品展示：
     */
    @RequestMapping("list")
    @ResponseBody
    public String getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
//
//        }
//        if(userService.checkAdminRole(user).isSuccess()){
//            //填充业务
//            return productService.getProductList(pageNum,pageSize);
//        }else{
//            return ServerResponse.createByErrorMessage("无权限操作");
//        }
        ServerResponse s = productService.getProductList(pageNum,pageSize);
        System.out.println(s.getData());
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("PageInfo",productService.getProductList(pageNum,pageSize));
        return "charts";
    }

}
