package com.supermarket.controller.backed;

import com.github.pagehelper.PageInfo;
import com.supermarket.bean.User;
import com.supermarket.common.Const;
import com.supermarket.common.ResponseCode;
import com.supermarket.common.ServerResponse;
import com.supermarket.service.OrderService;
import com.supermarket.service.ProductService;
import com.supermarket.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ASUS on 2019/11/12.
 */
@Controller
public class UserManageController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/")
    public String index(HttpSession session){
        //说明用户在session有效期内没有注销退出，免密码登录直接进入主页
        if(session.getAttribute(Const.CURRENT_USER)!=null){
            //更新session的key值时间：
            session.setAttribute(Const.CURRENT_USER,session.getAttribute(Const.CURRENT_USER));
            session.setAttribute(Const.CURRENT_ROLE,session.getAttribute(Const.CURRENT_ROLE));
            return "index";
        }
        return "login";

    }
    /**
     * 登录：
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/manage/user/login")
    public String login(String username, String password, HttpSession session,Model model){
        ServerResponse<User> response = userService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == 0){
                return "personLogin";
            }
            session.setAttribute(Const.CURRENT_USER,user);
            session.setAttribute(Const.CURRENT_ROLE,user.getRole());
            //管理员登录后跳转到首页:
            if(user.getRole()== Const.Role.ROLE_ADMIN){
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
            //销售员登录后跳转到商品管理列表：
            if(user.getRole() == Const.Role.ROLE_SALES){
                return "redirect:/manage/product/list";
            }
            //送货员登录后跳转到已接订单列表:
            if(user.getRole() == Const.Role.ROLE_DELIVERY){
                return "redirect:/orderManage";
            }
            //普通用户跳转到普通用户的登录界面
            if(user.getRole() == Const.Role.ROLE_CUSTOMER){
                return "/manage/user/logOut";
            }
        }
        model.addAttribute(Const.MESSAGE,"密码错误，权限不足");
        return "login";
    }

    /**
     * 普通用户进行注册
     * @param username
     * @param password
     * @param password2
     * @param model
     * @return
     */
    @PostMapping("/manage/user/register")
    public String register(String username,String password,String password2,Model model){
        System.out.println(username+password+password2);
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)||StringUtils.isEmpty(password2)){
            model.addAttribute(Const.MESSAGE,"请输入有效数据");
            return "register";
        }
        if(!password.equals(password2)){
            model.addAttribute(Const.MESSAGE,"两次填写的密码不一致");
            return "register";
        }
        //到数据库进行添加：
        String result = userService.registerUser(username,password);

        if(Const.FAILED.equals(result)){
            model.addAttribute(Const.MESSAGE,result);
            return "register";
        }
        model.addAttribute(Const.MESSAGE,result);
        return "login";
    }

    @RequestMapping("/manage/user/logOut")
    public String logOut(HttpSession session){
        System.out.println("注销");
        Integer role= (Integer) session.getAttribute(Const.CURRENT_ROLE);
        if(role == null || role == 0){
            session.removeAttribute(Const.CURRENT_USER);
            session.removeAttribute(Const.CURRENT_ROLE);
            return "personLogin";
        }
        session.removeAttribute(Const.CURRENT_USER);
        session.removeAttribute(Const.CURRENT_ROLE);
        return "login";
    }
    /**
     * 用户列表：
     */
    @RequestMapping("/manage/user/list")
    public String getList(Model model, HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            model.addAttribute("mssage","用户未登录,请登录管理员");
            return "login";
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //填充业务
            PageInfo result = userService.getUserList(pageNum,pageSize);
            model.addAttribute("pageInfo",result);
            System.out.println(result.getNavigatepageNums());
            return "personManage";
        }else{
            model.addAttribute("mssage","无权限操作");
            return "login";
        }

    }

    /**
     * 新增用户
     * @param productId：为空时为新增用户，非空为修改用户
     * @param username
     * @param password
     * @param role
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/manage/user/uploadPerson")
    public String uploadPerson(
                                 @RequestParam(value = "productId",required = false)String productId,
                                 @RequestParam(value="username",required = false)String username,
                                 @RequestParam(value = "password",required = false)String password,
                                 @RequestParam(value = "role",required = false)String role, Model model, HttpServletRequest request) {

            //插入到数据库：
            String result = userService.insertUser(productId,username,password,role);
            if(Const.SUCCESS.equals(result)){
                request.getSession().setAttribute(Const.MESSAGE,result);
                return "redirect:/manage/user/list";
            }
            return result;

    }

    /**
     * 修改用户
     * @param personId
     * @param username
     * @param password
     * @param role
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/manage/user/updatePerson")
    public String updatePerson(
            @RequestParam(value = "personId",required = false)String personId,
            @RequestParam(value="username",required = false)String username,
            @RequestParam(value = "password",required = false)String password,
            @RequestParam(value = "role",required = false)String role, Model model, HttpServletRequest request) {


        String result = userService.updatePerson(personId,username,password,role);

        if(Const.SUCCESS.equals(result)){
            return "redirect:/manage/user/list";
        }
        return result;

    }



    /**
     * 获得会员列表：
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/manage/personList")
    public String PersonList(Model model, HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){

        PageInfo result  = userService.selectAllPerson(pageNum,pageSize);
        model.addAttribute("pageInfo",result);
        return "forms";
    }

    /**
     * 批量删除人员
     * @param ids
     * @return
     */
    @RequestMapping("/manage/user/deleteMoreProduct")
    @ResponseBody
    public String deleteMoreProduct(@RequestBody List<Integer> ids){

        if(ids == null || ids.size() == 0){
            return "请先勾选要批量删除的人员";
        }

        String result = userService.deleteMoreProductByIds(ids);

        return result;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @GetMapping("/manage/user/deletePerson/{userId}")
    @ResponseBody
    public String deleteProduct(@PathVariable("userId") Integer userId){

        String result = userService.deletePersonById(userId);

        return result;

    }


}
