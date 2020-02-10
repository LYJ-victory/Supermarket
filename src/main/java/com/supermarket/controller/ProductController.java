package com.supermarket.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.supermarket.bean.Product;
import com.supermarket.bean.User;
import com.supermarket.bean.vo.UpProduct;
import com.supermarket.common.Const;
import com.supermarket.common.ServerResponse;
import com.supermarket.service.ProductService;
import com.supermarket.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.plugin.javascript.navig.Array;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
     * @param model
     * @param session
     * @param pageNum:当前第几页
     * @param pageSize：一页有几条数据
     * @return：第pageNum页的pageSize数据
     */
    @RequestMapping("list")
    public String getList(Model model,HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //只有管理员和销售员可以访问商品管理
        if(user == null || !(user.getRole() == 1 || user.getRole() == 2)){
            return "login";
        }
        PageInfo result =  productService.getProductList(pageNum,pageSize);
        model.addAttribute("pageInfo",result);

        return "charts";
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @GetMapping("/deleteProduct/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable("id") Integer id){

        String result = productService.deleteProductById(id);

        return result;

    }

    /**
     * 新增/修改商品
     * @param img
     * @param name
     * @param quantity
     * @param price
     * @param des
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/uploadProduct")
    public String uploadProduct( MultipartFile img,
                                 @RequestParam(value = "productId",required = false)String productId,
                                @RequestParam(value="name")String name,
                                @RequestParam(value = "quantity")String quantity,
                                @RequestParam(value = "price")String price,
                                 @RequestParam(value = "status",defaultValue = "0")String status,
                                @RequestParam(value = "des")String des, Model model, HttpServletRequest request) {
        String result ="";
        if(StringUtils.isEmpty(productId)){
// productId为空，新增商品：
            String checkResult = preCheck(img,name,quantity,price,des);
            if(Const.FAILED.equals(checkResult)){
                return "";
            }
            //插入到数据库：
             result = productService.addProduct(name,quantity,price,des,checkResult,status);
            if(Const.SUCCESS.equals(result)){
                request.getSession().setAttribute(Const.MESSAGE,result);
                return "redirect:/manage/product/list";
            }
            return result;

        }
// productId不为空，修改商品:
        System.out.println(img.getSize());
        if(img.getSize() == 0){
            //没有更新图片：
            String oldImg = productService.selectImgById(productId);
            result=productService.updateProduct(productId,oldImg,name,quantity,price,des,status);
        }else{
            //更新了图片：
            result=productService.updateProduct(productId,preCheck(img,name,quantity,price,des),name,quantity,price,des,status);
        }
        if(Const.SUCCESS.equals(result)){
            return "redirect:/manage/product/list";
        }
        return result;
    }

    /**
     * 检查参数是否正确
     * @param img
     * @param name
     * @param quantity
     * @param price
     * @param des
     * @return 返回修改上传的图片路径 或者 failed
     */
    private String preCheck(MultipartFile img,String name, String quantity, String price, String des) {
        //1.判断是否有上传图片和其他值是否非空：
        if (img.isEmpty()|| StringUtils.isEmpty(name)||StringUtils.isEmpty(quantity)||StringUtils.isEmpty(price)) {
            System.out.println("没有上传商品图片");
            return "failed";
        }
        // 文件名:
        String fileName = img.getOriginalFilename();
        // 后缀名:
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 上传后的路径:
        String filePath = "D://Supermarket//src//main//resources//static//images//";
        // 新文件名：
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            img.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String image = fileName;

        return image;
    }

    /**
     * 查看某种商品
     * @param lookProductId
     * @return 图片、商品名称、商品描述
     */
    @GetMapping("lookProduct/{lookProductId}")
    @ResponseBody
    public ServerResponse lookProduct(@PathVariable("lookProductId")String lookProductId){
        if(StringUtils.isEmpty(lookProductId)){
            return ServerResponse.createByErrorMessage("商品Id不能为空");
        }
        Product product = productService.selectLookProdduct(lookProductId);
        Map productMap = Maps.newHashMap();
        productMap.put("lookProduct",product);
        return ServerResponse.createBySuccess(productMap);
    }

    /**
     * 展示要进货的信息到模态框：
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("jinhuoProduct/{productId}")
    public String jinhuoProduct(@PathVariable("productId")String productId,Model model){
        if(StringUtils.isEmpty(productId)){
            return Const.FAILED;
        }
        Product product = productService.selectLookProdduct(productId);
        model.addAttribute("jinhuoProductInfo",product);
        return "charts::productJinhuo_type";
    }

    /**
     * 确定进货
     * @param productId
     * @param jinhuonumber
     * @return
     */
    @PostMapping("queDinJinHuoProduct")
    @ResponseBody
    public String queDinJinHuoProduct(@RequestParam String productId,@RequestParam String jinhuonumber){

       String result =  productService.quedingJinHuoByProductId(productId,jinhuonumber);
        return result;
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @RequestMapping("/deleteMoreProduct")
    @ResponseBody
    public String deleteMoreProduct(@RequestBody List<Integer> ids){

        if(ids == null || ids.size() == 0){
            return "请先勾选商品";
        }

        String result = productService.deleteMoreProductByIds(ids);

        return result;
    }


}
