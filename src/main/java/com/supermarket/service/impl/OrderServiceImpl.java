package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.bean.*;
import com.supermarket.bean.vo.OrderManageDetal;
import com.supermarket.bean.vo.OrderManageVO;
import com.supermarket.common.Const;
import com.supermarket.mapper.*;
import com.supermarket.service.OrderService;
import com.supermarket.test.CreateOrderNo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ASUS on 2020/2/3.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    /**
     * 支付成功后生成订单
     * @param userId
     * @param beizu
     * @return
     */
    @Transactional
    @Override
    public String createPersonOrder(Integer userId, List<Product> productList, String beizu, String sumPrice) {

        try{
            //获取时间：
            Date date = new Date();

            TbOrder tbOrder = new TbOrder();
            tbOrder.setUserId(userId);
            tbOrder.setSumprice(new BigDecimal(sumPrice));
            tbOrder.setStatus(new Byte("0"));
            //java的data对应数据库datetime：
            tbOrder.setCreateTime(date);
            if (!StringUtils.isEmpty(beizu)){
                tbOrder.setBeizu(beizu);
            }else{
                tbOrder.setBeizu("无");
            }
            //订单号：
            tbOrder.setOrderNo(Long.valueOf(CreateOrderNo.orderNo()));

            String productIds="";
            for (Product product : productList) {
                productIds = productIds+product.getId()+"%";
                //设置购物车的checked为1表示已经结算，用户的购物车不再显示:
                final CartExample cartExample = new CartExample();
                final CartExample.Criteria criteria = cartExample.createCriteria();
                criteria.andProductIdEqualTo(product.getId());
                criteria.andUserIdEqualTo(userId);
                Cart cart = new Cart();
                cart.setChecked(1);
                cartMapper.updateByExampleSelective(cart,cartExample);
            }
            //商品id号用%进行分割：
            tbOrder.setProductId(productIds);

            tbOrderMapper.insert(tbOrder);

        }catch (Exception e){
            return Const.FAILED;
        }

        return Const.SUCCESS;
    }


    /**
     * 获得订单列表带分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo getAllOrder(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<OrderManageVO> orderManageVOList = new ArrayList<>();
        List<TbOrder> tbOrderList = tbOrderMapper.selectList();
        for (TbOrder tbOrder : tbOrderList) {
            User user = userMapper.selectByPrimaryKey(tbOrder.getUserId());
            OrderManageVO orderManageVO = new OrderManageVO();
            orderManageVO.setTbOrder(tbOrder);
            orderManageVO.setUsername(user.getUsername());

            orderManageVOList.add(orderManageVO);
        }
        PageInfo pageResult = new PageInfo(orderManageVOList);
        return pageResult;
    }

    /**
     * 订单备注：显示在子表格处
     * @param orderNo
     * @return
     */
    @Override
    public TbOrder getBeiZhu(String orderNo) {
        final TbOrderExample tbOrderExample = new TbOrderExample();
        final TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andOrderNoEqualTo(Long.valueOf(orderNo));
        final List<TbOrder> tbOrderList = tbOrderMapper.selectByExample(tbOrderExample);
        return tbOrderList.get(0);
    }

    /**
     * 收货地址信息
     * @param orderNo
     * @return
     */
    @Override
    public UserAddress getOrderAddressInfo(String orderNo) {
        final TbOrderExample tbOrderExample = new TbOrderExample();
        final TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andOrderNoEqualTo(Long.valueOf(orderNo));
        final List<TbOrder> tbOrderList = tbOrderMapper.selectByExample(tbOrderExample);
        final Integer userId = tbOrderList.get(0).getUserId();
        final UserAddressExample userAddressExample = new UserAddressExample();
        final UserAddressExample.Criteria criteria1 = userAddressExample.createCriteria();
        criteria1.andUseridEqualTo(userId);
        final List<UserAddress> userAddresses = userAddressMapper.selectByExample(userAddressExample);
        return userAddresses.get(0);
    }

    /**
     * 根据订单编号模糊查询
     * @param pageNum
     * @param pageSize
     * @param searchyOrderNo
     * @return
     */
    @Override
    public PageInfo searchOrderByOrderNo(int pageNum, int pageSize, String searchyOrderNo) {

        PageHelper.startPage(pageNum,pageSize);
        List<OrderManageVO> orderManageVOList = new ArrayList<>();
        List<TbOrder> tbOrderList = tbOrderMapper.searchOrderByOrderNo(searchyOrderNo);
        for (TbOrder tbOrder : tbOrderList) {
            User user = userMapper.selectByPrimaryKey(tbOrder.getUserId());
            OrderManageVO orderManageVO = new OrderManageVO();
            orderManageVO.setTbOrder(tbOrder);
            orderManageVO.setUsername(user.getUsername());

            orderManageVOList.add(orderManageVO);
        }
        PageInfo pageResult = new PageInfo(orderManageVOList);
        return pageResult;
    }

    /**
     * 获得订单商品详情列表：
     * @param orderId
     * @return
     */
    @Override
    public List<OrderManageDetal> getProductListByOrderId(String orderId,Integer userId) {
        List<OrderManageDetal> resultList = new ArrayList<>();
        //得到订单商品的id:
        TbOrderExample tbOrderExample = new TbOrderExample();
        final TbOrderExample.Criteria criteria1 = tbOrderExample.createCriteria();
        criteria1.andOrderNoEqualTo(Long.valueOf(orderId));
        //用户查看自己的订单详情：
        if(userId != null){
            criteria1.andUserIdEqualTo(userId);
        }
        final List<TbOrder> tbOrderList = tbOrderMapper.selectByExample(tbOrderExample);
        final String productIds = tbOrderList.get(0).getProductId();
        //根据%分割出订单商品id:
        String[] productArrray = productIds.split("\\%");
        for (String s : productArrray) {
            OrderManageDetal orderManageDetal = new OrderManageDetal();
            //到购物车表里面去查找该商品数量等，购物车表的checked：0表示未结算，1：表示结算
            CartExample cartExample = new CartExample();
            final CartExample.Criteria criteria = cartExample.createCriteria();
            criteria.andUserIdEqualTo(tbOrderList.get(0).getUserId());
            criteria.andProductIdEqualTo(Integer.valueOf(s));
            final List<Cart> carts = cartMapper.selectByExample(cartExample);
            orderManageDetal.setQuantity(carts.get(0).getQuantity());
            orderManageDetal.setProductId(Integer.parseInt(s));
            final Product product = productMapper.selectByPrimaryKey(Integer.valueOf(s));
            orderManageDetal.setImg(product.getImg());
            orderManageDetal.setSumMoney(carts.get(0).getCartSumprice());
            orderManageDetal.setName(product.getName());
            //订单时间：
            orderManageDetal.setCreateTime(tbOrderList.get(0).getCreateTime());
            resultList.add(orderManageDetal);
        }

        return resultList;
    }

    /**
     * 修改订单状态：
     * @param orderNo
     * @return
     */
    @Transactional
    @Override
    public String updateOrderStatus(String orderNo) {
       try{
           final TbOrderExample tbOrderExample = new TbOrderExample();
           final TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
           criteria.andOrderNoEqualTo(Long.valueOf(orderNo));
           TbOrder tbOrder = new TbOrder();
           tbOrder.setStatus(new Byte("1"));
           tbOrderMapper.updateByExampleSelective(tbOrder,tbOrderExample);
       }catch (Exception e){
           return Const.FAILED;
       }
        return Const.SUCCESS;
    }

    /**
     * 订单总条数
     * @return
     */
    @Override
    public int countOrderSum() {
        int sum = tbOrderMapper.countAll();
        return sum;
    }

    /**
     * 已发货的算入总营业额
     * @return
     */
    @Override
    public BigDecimal countMoney() {
        BigDecimal moneySum = new BigDecimal(0);
        final TbOrderExample tbOrderExample = new TbOrderExample();
        final TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andStatusEqualTo(new Byte("1"));
        final List<TbOrder> tbOrderList = tbOrderMapper.selectByExample(tbOrderExample);
        if(tbOrderList != null && tbOrderList.size()>0){
            for (TbOrder tbOrder : tbOrderList) {
                moneySum = moneySum.add(tbOrder.getSumprice());
            }
        }
        return moneySum;
    }


    /**
     * 格式化处理时间
     * @param date
     * @return
     * @throws ParseException
     */
    public static String MyDataFormat(String date) throws ParseException {

//        String date = "Thu Aug 27 18:05:49 CST 2015";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

        System.out.println(formatDate);
        return formatDate;
    }

}
