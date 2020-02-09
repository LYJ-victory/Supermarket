package com.supermarket.controller.backed;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Product;
import com.supermarket.bean.User;
import com.supermarket.bean.UserAddress;
import com.supermarket.common.Const;
import com.supermarket.common.ServerResponse;
import com.supermarket.service.OrderService;
import com.supermarket.service.PersonService;
import com.supermarket.service.ProductService;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by ASUS on 2020/1/20.
 */
@Controller
@RequestMapping("/person")
public class PersonManageController {
    @Autowired
    private ProductService productService;
    @Autowired
    private PersonService personService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password, HttpSession session,Model model){
        ServerResponse<User> response = userService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            session.setAttribute(Const.CURRENT_USER,user);
            session.setAttribute(Const.CURRENT_ROLE,user.getRole());
            return Const.SUCCESS;
        }
        return Const.FAILED;
    }

    @RequestMapping("list")
    public String getList(Model model, HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        PageInfo result =  productService.getPersonProductList(pageNum,pageSize);
        model.addAttribute("pageInfo",result);

        return "supermarket";
    }

    /**
     * 修改收货地址1
     * @param username
     * @param phone
     * @param address
     * @param session
     * @return
     */
    @PostMapping("/personInformationUpdate")
    @ResponseBody
    public String personInformation(String username,String phone,String address,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        String result = personService.updatePersonAddress(user.getId(),username,phone,address);
        if(Const.SUCCESS.equals(result)){
            //个人收货地址：
            UserAddress userAddress = personService.getPersonAddress(user.getId());
            session.setAttribute("userAddress",userAddress);
        }
        return result;
    }

    /**
     * 新增收货地址
     * @param username
     * @param phone
     * @param address
     * @param session
     * @return
     */
    @PostMapping("/personInformationAdd")
    @ResponseBody
    public String personInformationAdd(String username,String phone,String address,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        String result = personService.insertPersonAddress(user.getId(),username,phone,address);
        //成功的话顺便展示收货信息
        if(Const.SUCCESS.equals(result)){
            //个人收货地址：
            UserAddress userAddress = personService.getPersonAddress(user.getId());
            session.setAttribute("userAddress",userAddress);
        }
        return result;
    }

    /**
     * 支付后生成的订单
     * @param beizu
     * @return
     */
    @PostMapping("/pay")
    @ResponseBody
    public String pay(String beizu,String sumPrice,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        List<Product> productList = (List<Product>) session.getAttribute("order_products");

        String result  = orderService.createPersonOrder(user.getId(),productList,beizu,sumPrice);

        return result;
    }

    /**
     * 我的订单列表
     * @param session
     * @return
     */
    @RequestMapping("myOrder")
    public String myOrder(Model model,HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        PageInfo result =  personService.findAllMyOrders(pageNum,pageSize,user.getId());
        model.addAttribute("pageInfo",result);
        return "myOrders";
    }

}
