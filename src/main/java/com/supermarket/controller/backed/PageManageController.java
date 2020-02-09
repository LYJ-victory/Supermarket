package com.supermarket.controller.backed;

import com.supermarket.bean.Product;
import com.supermarket.bean.User;
import com.supermarket.service.OrderService;
import com.supermarket.service.ProductService;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * Created by ASUS on 2020/1/15.
 */
@Controller
public class PageManageController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(Model model)
    {
        //查询商品总数、订单总数、会员总数、总营业额
        int productSum = productService.countProductSum();
        int orderSum = orderService.countOrderSum();
        int personSum = userService.countAll();
        BigDecimal moneySum = orderService.countMoney();
        model.addAttribute("productSum",productSum);
        model.addAttribute("orderSum",orderSum);
        model.addAttribute("personSum",personSum);
        model.addAttribute("moneySum",moneySum);
        return "index";
    }
    @RequestMapping("myOrders")
    public String myOrders(){
        return "myOrders";
    }

    @RequestMapping("tables")
    public String tables(){return "tables";}
    @RequestMapping("forms")
    public String forms(){
        return "forms";
    }
    @RequestMapping("shopOrder")
    public String order(){
        return "shopOrder";
    }
    @RequestMapping("register")
    public String register(){
        return "register";
    }
    @RequestMapping("addProduct")
    public String addProduuct(){return "addProduct";}
    @RequestMapping("addPerson")
    public String addPerson(){return "addPerson";}
    @RequestMapping("shopCart")
    public String shopCart(){
        return "shopCart";
    }
    @RequestMapping("orderManageDetail")
    public String orderManageDetail(){
        return "orderManageDetail";
    }
    @RequestMapping("personLogin")
    public String personLogin(){
        return "personLogin";
    }
    /**
     * 查看某件商品，在修改前把查到的商品的信息展示到界面上：
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("updateProduct/{id}")
    public String updateProduct(@PathVariable("id")String id, Model model){
        final Product product = productService.selectLookProdduct(id);
        model.addAttribute("updateProduct",product);
        return "updateProduct";
    }

    /**
     * 查看某个人，在修改前把查到的人的信息展示到界面上：
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("updatePerson/{id}")
    public String updatePerson(@PathVariable("id")String id, Model model){
        final User product = userService.selectLookPerson(id);
        model.addAttribute("updateProduct",product);
        return "updatePerson";
    }

}
