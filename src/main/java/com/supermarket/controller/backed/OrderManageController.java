package com.supermarket.controller.backed;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.TbOrder;
import com.supermarket.bean.User;
import com.supermarket.bean.UserAddress;
import com.supermarket.bean.vo.OrderManageDetal;
import com.supermarket.bean.vo.OrderManageVO;
import com.supermarket.common.Const;
import com.supermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by ASUS on 2020/2/3.
 */
@Controller
public class OrderManageController {
    @Autowired
    private OrderService orderService;


    /**
     * 订单管理分页展示：
     * @param model
     * @param session
     * @param pageNum:当前第几页
     * @param pageSize：一页有几条数据
     * @return：第pageNum页的pageSize数据
     */
    @RequestMapping("orderManage")
    public String orderManage(Model model, HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //只有管理员和送货员可以访问商品管理
        if(user == null||!(user.getRole() == 1 || user.getRole() == 3)){
            return "login";
        }
        PageInfo result =  orderService.getAllOrder(pageNum,pageSize);
        model.addAttribute("pageInfo",result);
        return "tables";
    }

    /**
     * 查看用户订单详情
     * @param session
     * @param model
     * @param orderNo
     * @return
     */
    @RequestMapping("orderManageDetail/{orderNo}")
    public  String orderManageDetail(HttpSession session,Model model,@PathVariable("orderNo") String orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录：
        if(user == null){
            return "login";
        }
        //订单备注：
        TbOrder myOrder = orderService.getBeiZhu(orderNo);
        String  beizu = myOrder.getBeizu();
        model.addAttribute("beizu",beizu);
        //收货信息：
        UserAddress userAddress = orderService.getOrderAddressInfo(orderNo);
        model.addAttribute("userAddress",userAddress);
        model.addAttribute("orderNo",orderNo);
        List<OrderManageDetal> orderProductList;

        if(user.getRole() == 1 || user.getRole() == 3){
            //管理员和送货员看所有人的所有订单：
           orderProductList = orderService.getProductListByOrderId(orderNo,null);
            //订单时间：
            model.addAttribute("createTime",orderProductList.get(0).getCreateTime());
            model.addAttribute("orderProductList",orderProductList);
            return "orderManageDetail";
        }else{
            //用户看自己的订单：
            orderProductList = orderService.getProductListByOrderId(orderNo,user.getId());
            //订单时间：
            model.addAttribute("createTime",orderProductList.get(0).getCreateTime());
            model.addAttribute("orderProductList",orderProductList);
            model.addAttribute("orderDetailSumPrice",myOrder.getSumprice());
            //thymeleaf局部异步刷新：
            return "myOrders::orderProduct_type";
        }

    }





    /**
     * thyme leaf局部刷新获得的订单商品详情数据：
     * @param session
     * @param model
     * @param
     * @return
     */
//    @RequestMapping("orderManageDetail/{orderNo}")
//    public  String orderManageDetail(HttpSession session,Model model,@PathVariable("orderNo") String orderNo){
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        //判断用户是否登录：
//        if(user == null || !(user.getRole() == 1 || user.getRole() == 3)){
//            return "login";
//        }
//        List<OrderManageDetal> orderProductList = orderService.getProductListByOrderId(orderNo);
//        model.addAttribute("orderProductList",orderProductList);
//        //订单备注：
//        String  beizu = orderService.getBeiZhu(orderNo);
//        model.addAttribute("beizu",beizu);
//        //收货信息：
//        UserAddress userAddress = orderService.getOrderAddressInfo(orderNo);
//        model.addAttribute("userAddress",userAddress);
//        return "tables::orderProduct_type";
//    }


    /**
     * 修改订单状态：是否发货
     * @param session
     * @param orderNo
     * @return
     */
    @GetMapping("updateOrderStatus/{orderNo}")
    @ResponseBody
    public String updateOrderStatus(HttpSession session,@PathVariable("orderNo")String orderNo){

        String result = orderService.updateOrderStatus(orderNo);
        return result;
    }

}
