package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Product;
import com.supermarket.bean.ProductExample;
import com.supermarket.common.Const;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ASUS on 2019/12/13.
 *
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     *管理员的商品展示：可看售卖中的和下架的：
     * @param pageNum:第几页
     * @param pageSize：每页的大小
     * @return
     */
    @Override
    public PageInfo getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        PageInfo pageResult = new PageInfo(productList);
        return pageResult;
    }

    /**
     * 展示有库存的商品：
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo getPersonProductList(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //找出上架的商品：
        ProductExample productExample = new ProductExample();
        final ProductExample.Criteria criteria = productExample.createCriteria();
        //按照该条件进行查询：
        criteria.andStatusEqualTo(new Byte("0"));
        List<Product> productList = productMapper.selectByExample(productExample);
        PageInfo pageResult = new PageInfo(productList);
        return pageResult;
    }

    /**
     * 这个暂时没有用到，用cartServiceImpl里面的
     * 订单列表：
     * @param ids
     * @return
     */
    @Override
    public List<Product> orderFromProductIds(String[] ids) {

        List<Product> productList = new ArrayList<>();

        List<Integer>  idsList = Arrays.stream(ids).map(x -> Integer.valueOf(x)).collect(Collectors.toList());

        idsList.forEach(x -> {
            final Product product = productMapper.selectByPrimaryKey(x);
            productList.add(product);

        });
        return productList;
    }

    /**
     * 商品总数
     * @return
     */
    @Override
    public int countProductSum() {
        int sum = productMapper.countAll();
        return sum;
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @Override
    public String deleteMoreProductByIds(List<Integer> ids) {

        try{
            for (int i = 0; i < ids.size(); i++) {
                productMapper.deleteByPrimaryKey(ids.get(i));
            }
        }catch (Exception e){
            return Const.FAILED;
        }

        return Const.SUCCESS;
    }

    /**
     * 确定进货
     * @param productId
     * @param jinhuonumber
     * @return
     */
    @Transactional
    @Override
    public String quedingJinHuoByProductId(String productId, String jinhuonumber) {
       try{
           final Product product  = productMapper.selectByPrimaryKey(Integer.valueOf(productId));
           final Byte status = product.getStatus();
           //修改商品为上架：
           if(status.equals(new Byte("1"))){
               product.setStatus(new Byte("0"));
           }
           product.setQuantity(product.getQuantity()+Integer.valueOf(jinhuonumber));
           productMapper.updateByPrimaryKeySelective(product);
       }catch(Exception e){
           return Const.FAILED;
       }
        return Const.SUCCESS;
    }

    @Transactional
    @Override
    public String deleteProductById(Integer id) {

        try{
            productMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            return "failed";
        }
        return "success";
    }

    /**
     * 新增商品
     * @param name
     * @param quantity
     * @param price
     * @param des
     * @param image
     * @return
     */
    @Transactional
    @Override
    public String addProduct(String name, String quantity, String price, String des, String image,String status) {
        //1.查找商品名是否重复：
        ProductExample example  = new ProductExample();
        final ProductExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Product> list = productMapper.selectByExample(example);
        if(list.size()>0){
            return "商品名称重复";
        }
        Product addProduct = new Product();
        addProduct.setName(name);
        if(!StringUtils.isEmpty(des)){
            addProduct.setDes(des);
        }
        addProduct.setQuantity(Integer.valueOf(quantity));
        addProduct.setStatus(Byte.valueOf(status));
        //与金钱有关的数据类型：在数据库中为decimal，在Java中为BigDecimal：
        addProduct.setPrice(new BigDecimal(price));
        addProduct.setImg(image);
        //2.插入到数据库：
        try{
            productMapper.insert(addProduct);
        }catch (Exception e){
            return "新增商品失败";
        }
        return "success";
    }

    /**
     * 查看某件商品：
     * @param lookProductId
     * @return
     */
    @Override
    public Product selectLookProdduct(String lookProductId) {
        try {
            final Product lookProduct = productMapper.selectByPrimaryKey(Integer.valueOf(lookProductId));
            return lookProduct;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 修改商品
     * @param productId
     * @param checkResult
     * @param name
     * @param quantity
     * @param price
     * @param des
     * @return
     */
    @Transactional
    @Override
    public String updateProduct(String productId, String checkResult, String name, String quantity, String price, String des,String status) {
        try{
            Product product  = new Product();
            product.setName(name);
            product.setQuantity(Integer.valueOf(quantity));
            product.setPrice(new BigDecimal(price));
            product.setDes(des);
            product.setImg(checkResult);
            product.setStatus(Byte.valueOf(status));

            ProductExample productExample = new ProductExample();
            final ProductExample.Criteria criteria = productExample.createCriteria();
            criteria.andIdEqualTo(Integer.valueOf(productId));

            productMapper.updateByExampleSelective(product,productExample);
            return Const.SUCCESS;
        }catch (Exception e){
            return Const.FAILED;
        }
    }

    @Override
    public String selectImgById(String productId) {
        final Product product = productMapper.selectByPrimaryKey(Integer.valueOf(productId));

        return product.getImg();
    }

}
