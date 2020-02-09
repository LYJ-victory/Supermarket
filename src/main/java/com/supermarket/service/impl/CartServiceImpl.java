package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.bean.Cart;
import com.supermarket.bean.CartExample;
import com.supermarket.bean.Product;
import com.supermarket.bean.ProductExample;
import com.supermarket.common.Const;
import com.supermarket.mapper.CartMapper;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ASUS on 2020/1/20.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加商品到购物车：
     * 先查看比较当前库存够不够，够的话该商品数量-1，购物车中该商品的数量+1，同时计算该商品的总价
     * @param productId
     * @return
     */
    @Transactional
    @Override
    public String addShopProduct(Integer productId,Integer userId) {
        try{
            final Product product = productMapper.selectByPrimaryKey(productId);
            //还有库存，商品数量-1：
            //Byte类型进行比较：
            System.out.println(product.getStatus().equals(new Byte("0")));
            if(product.getStatus().equals(new Byte("0"))){
                //修改商品的库存量：
                product.setQuantity(product.getQuantity()-1);
                //设置库存已经用完：
                if(product.getQuantity() == 0){
                    product.setStatus(new Byte("1"));
                }
                productMapper.updateByPrimaryKeySelective(product);
                //TODO
                //库存用完，销售员要知道商品变为下架要去进货：
                //看购物车是否有该类商品且还没有结算的，没有的话就进行插入，有的话修改该商品在购物车中的数量和总价：
                CartExample cartExample = new CartExample();
                final CartExample.Criteria criteria = cartExample.createCriteria();
                criteria.andCheckedEqualTo(0);
                final List<Cart> cartlist = cartMapper.selectByExample(cartExample);
                if (cartlist.size()>0){
                    for (Cart cart : cartlist) {
                        if(cart.getProductId().equals(productId)){
                            //数量+1：
                            cart.setQuantity(cart.getQuantity()+1);
                            //总价+1：
                            BigDecimal sum = new BigDecimal(Double.toString(cart.getCartSumprice()));
                            cart.setCartSumprice(sum.add(product.getPrice()).doubleValue());
                            //只对有更新的值进行修改：
                            cartMapper.updateByPrimaryKeySelective(cart);
                            return Const.SUCCESS;
                        }
                    }
                }
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setQuantity(1);
                cart.setCartSumprice(product.getPrice().doubleValue());
                cartMapper.insertSelective(cart);
                return Const.SUCCESS;
            }
        }catch (Exception e){
            return Const.FAILED;
        }

        //库存不足：
        return Const.FAILED;
    }

    /**
     * 从购物车减少商品
     * 1.在商品列表里该商品的数量+1
     * 2.从购物车列表里面找到该商品，数量-1
     * @param productId
     * @param id
     * @return
     */
    @Transactional
    @Override
    public String reducceShopProduct(Integer productId, Integer id) {
        try{
            final Product product = productMapper.selectByPrimaryKey(productId);
            final List<Cart> cartlist = cartMapper.selectList();
            //改变库存状态：
            if(product.getStatus().equals(new Byte("1"))){
                product.setStatus(new Byte("0"));
            }
            product.setQuantity(product.getQuantity()+1);
            productMapper.updateByPrimaryKeySelective(product);
            if (cartlist.size()>0){
                for (Cart cart : cartlist) {
                    if(cart.getProductId().equals(productId)){
                        //数量-1：
                        cart.setQuantity(cart.getQuantity()-1);
                        //总价-1：
                        BigDecimal sum = new BigDecimal(Double.toString(cart.getCartSumprice()));
                        cart.setCartSumprice(sum.subtract(product.getPrice()).doubleValue());
                        //只对有更新的值进行修改：
                        cartMapper.updateByPrimaryKeySelective(cart);
                        return Const.SUCCESS;
                    }
                }
            }
        }catch (Exception e){
            return Const.FAILED;
        }
        return Const.SUCCESS;
    }

    /**
     * 从购物车删除该商品
     * @param productId
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public String delFromShopCart(Integer productId, Integer userId) {
        try{
            //找到该用户的某类商品：
            final CartExample cartExample = new CartExample();
            final CartExample.Criteria criteria = cartExample.createCriteria();
            criteria.andProductIdEqualTo(productId);
            criteria.andUserIdEqualTo(userId);
            final List<Cart> carts = cartMapper.selectByExample(cartExample);
            //添加回商品列表，恢复数量，判断商品的状态：
            carts.forEach(x -> {
                final Product product = productMapper.selectByPrimaryKey(productId);
                if(product.getStatus().equals(new Byte("1"))){
                    //上架：
                    product.setStatus(new Byte("0"));
                }
                product.setQuantity(product.getQuantity()+x.getQuantity());
                //修改商品的信息：
                productMapper.updateByPrimaryKeySelective(product);
            });
            //购物车删除该商品：
            cartMapper.deleteByExample(cartExample);
        }catch (Exception e){
            return Const.FAILED;
        }
        return Const.SUCCESS;
    }


    /**
     * 订单列表
     * @param ids
     * @return
     */
    @Override
    public List<Product> orderFromProductIds(String[] ids, Integer userId) {
        List<Product> productList = new ArrayList<>();
        List<Integer>  idsList = Arrays.stream(ids).map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        //找到这个用户：
        CartExample cartExample = new CartExample();
        final CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        final List<Cart> carts = cartMapper.selectByExample(cartExample);
        //找到这个用户要结算的：
        for (Integer integer : idsList) {
            for(int i=0;i<carts.size();i++){
                if(integer.equals(carts.get(i).getProductId())){
                    Product product = productMapper.selectByPrimaryKey(integer);
                    product.setQuantity(carts.get(i).getQuantity());
                    product.setPrice(BigDecimal.valueOf(carts.get(i).getCartSumprice()));
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    /**
     * 购物车列表：
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo getProductList(int pageNum, int pageSize,int uesrId) {
        PageHelper.startPage(pageNum,pageSize);

        CartExample cartExample = new CartExample();
        final CartExample.Criteria criteria = cartExample.createCriteria();
        //查找该用户的购物车列表：
        criteria.andUserIdEqualTo(uesrId);
        //未结算的商品：
        criteria.andCheckedEqualTo(0);
        List<Cart> cartList = cartMapper.selectByExample(cartExample);
        List<Product> productList = new ArrayList<>();

        for (Cart cart : cartList) {
            //获得图片、名称：
            final Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            //设置商品的价格为总价，数量为购买的该商品数量：
            product.setPrice(new BigDecimal(cart.getCartSumprice()));
            product.setQuantity(cart.getQuantity());
            product.setStatus(cart.getChecked().byteValue());
            productList.add(product);
        }

        PageInfo pageResult = new PageInfo(productList);

        if (productList.size() == 0){
            pageResult.setList(null);
        }
        return pageResult;
    }

}
