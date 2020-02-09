package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.bean.TbOrder;
import com.supermarket.bean.TbOrderExample;
import com.supermarket.bean.UserAddress;
import com.supermarket.bean.UserAddressExample;
import com.supermarket.common.Const;
import com.supermarket.mapper.TbOrderMapper;
import com.supermarket.mapper.UserAddressMapper;
import com.supermarket.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ASUS on 2020/2/2.
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private TbOrderMapper tbOrderMapper;

    /**
     * 修改用户收货地址
     * @param userId
     * @param username
     * @param phone
     * @param address
     * @return
     */
    @Override
    public String updatePersonAddress(Integer userId, String username, String phone, String address) {

       try{
           //找到这条记录：
           UserAddressExample userAddressExample = new UserAddressExample();
           final UserAddressExample.Criteria criteria = userAddressExample.createCriteria();
           criteria.andUseridEqualTo(userId);
           final List<UserAddress> userAddresses = userAddressMapper.selectByExample(userAddressExample);
           final UserAddress userAddress = userAddresses.get(0);
           //进行修改：
            userAddress.setUsername(username);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
           userAddressMapper.updateByPrimaryKeySelective(userAddress);

        }catch (Exception e){
           return Const.FAILED;
       }
        return Const.SUCCESS;
    }

    /**
     *用户的收货地址信息，有就在结算页面展示，没有就不展示
     * @param userId
     * @return
     */
    @Override
    public UserAddress getPersonAddress(Integer userId) {
        UserAddressExample userAddressExample = new UserAddressExample();
        final UserAddressExample.Criteria criteria = userAddressExample.createCriteria();
        criteria.andUseridEqualTo(userId);
        final List<UserAddress> userAddresses = userAddressMapper.selectByExample(userAddressExample);
        if(userAddresses == null || userAddresses.size() == 0){
            return null;
        }
        return userAddresses.get(0);
    }

    /**
     * 新增收货地址
     * @param id
     * @param username
     * @param phone
     * @param address
     * @return
     */
    @Override
    public String insertPersonAddress(Integer id, String username, String phone, String address) {
        try{
            UserAddress userAddress = new UserAddress();
            userAddress.setUserid(id);
            userAddress.setUsername(username);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
            userAddressMapper.insertSelective(userAddress);

        }catch (Exception e){
            return Const.FAILED;
        }
        return Const.SUCCESS;
    }

    /**
     * 我的订单列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public PageInfo findAllMyOrders(int pageNum, int pageSize, Integer userId) {

        PageHelper.startPage(pageNum,pageSize);

        TbOrderExample tbOrderExample = new TbOrderExample();
        final TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);

        final List<TbOrder> tbOrderList = tbOrderMapper.selectByExample(tbOrderExample);

        PageInfo pageResult = new PageInfo(tbOrderList);
        return pageResult;
    }
}
