package com.supermarket.controller.backed;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Product;
import com.supermarket.bean.User;
import com.supermarket.bean.UserAddress;
import com.supermarket.common.Const;
import com.supermarket.service.CartService;
import com.supermarket.service.PersonService;
import com.supermarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ASUS on 2020/1/20.
 */
@Controller
public class ShopManageController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PersonService personService;

    /**
     * 添加商品到购物车
     * @param productId
     * @return
     */
    @GetMapping("/addShop/{productId}")
    @ResponseBody
    public String addShop(@PathVariable("productId") Integer productId, HttpSession session,Model model){

        User user = (User)session.getAttribute(Const.CURRENT_USER);

        String result = cartService.addShopProduct(productId,user.getId());
        if(Const.SUCCESS.equals(result)){
            final Product product = productService.selectLookProdduct(productId.toString());
            return product.getPrice().toString();
        }
        return result;

    }

    /**
     * 从购物车减少商品：
     * @param productId
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/reduceShop/{productId}")
    @ResponseBody
    public String reduceShop(@PathVariable("productId") Integer productId, HttpSession session,Model model){

        User user = (User)session.getAttribute(Const.CURRENT_USER);

        String result = cartService.reducceShopProduct(productId,user.getId());

        return result;

    }

    /**
     * 从购物车删除该商品
     * @param productId
     * @return
     */
    @GetMapping("/delFromShopCart/{productId}")
    @ResponseBody
    public String delFromShopCart(@PathVariable("productId")Integer productId,HttpSession session){
        if(StringUtils.isEmpty(productId.toString())){
            return Const.FAILED;
        }
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        String result = cartService.delFromShopCart(productId,user.getId());

        return result;
    }

    /**
     * 购物车列表：
     * @param model
     * @param session
     * @param pageNum:当前第几页
     * @param pageSize：一页有几条数据
     * @return：第pageNum页的pageSize数据
     */
    @RequestMapping("/shop/list")
    public String getList(Model model, HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return "login";
        }
        PageInfo result =  cartService.getProductList(pageNum,pageSize,user.getId());
        model.addAttribute("pageInfo",result);
        return "shopCart";
    }

    /**
     * 跳转到结算页面
     * @param ids:已经确认要购买的商品的id
     * @param model
     * @return
     */
    @RequestMapping("/payShopCart")
    @ResponseBody
    public String payShopCart(@RequestParam("ids") String[] ids,Model model,HttpSession session){
        if(ids == null || ids.length == 0){
            return "shopCart";
        }
        //跳到结算页面：
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        List<Product> cartOrderList = cartService.orderFromProductIds(ids,user.getId());
        session.setAttribute("order_products",cartOrderList);
        BigDecimal sumPrice = new BigDecimal("0");
        //计算总价：
        for (Product product : cartOrderList) {
            sumPrice = sumPrice.add(product.getPrice());
        }
        session.setAttribute("sumPrice",sumPrice);
        //个人收货地址：
        UserAddress userAddress = personService.getPersonAddress(user.getId());
        session.setAttribute("userAddress",userAddress);

        return "success";

    }

}
