package com.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.bean.User;
import com.supermarket.bean.UserExample;
import com.supermarket.common.Const;
import com.supermarket.common.ServerResponse;
import com.supermarket.mapper.UserMapper;
import com.supermarket.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

/**
 * Created by ASUS on 2019/11/12.
 *
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //使用springframek自带的md5加密:
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        try{
            User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);

        }catch (Exception e){
            return ServerResponse.createByErrorMessage("密码错误");
        }
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 返回用户列表：
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo getUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //列出管理层的用户：
        UserExample userExample = new UserExample();
        final UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andRoleBetween(1,3);
        List<User> productList = userMapper.selectByExample(userExample);

        PageInfo pageResult = new PageInfo(productList);
        return pageResult;
    }

    /**
     * 新增和修改用户
     * @param userId
     * @param username
     * @param password
     * @param role
     * @return
     */
    @Override
    public String insertUser(String userId, String username, String password, String role) {

        if(StringUtils.isEmpty(userId)){
            //id为空则为新增：
            if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)||StringUtils.isEmpty(role)){
                return Const.FAILED;
            }
            try {
                User user =new User(null,username,DigestUtils.md5DigestAsHex(password.getBytes()),Integer.valueOf(role));
                userMapper.insert(user);
            }catch (Exception e){
                return Const.FAILED;
            }
        }else{
            //id不为空为修改：
           try{
               if(!StringUtils.isEmpty(password)){
                   password=DigestUtils.md5DigestAsHex(password.getBytes());
               }
               User user =new User(Integer.valueOf(userId),username,password,Integer.valueOf(role));
               userMapper.updateByPrimaryKey(user);
           }catch (Exception e){
               return Const.FAILED;
           }
        }
        return Const.SUCCESS;
    }

    /**
     * 查看某个人：
     * @param id
     * @return
     */
    @Override
    public User selectLookPerson(String id) {
        try {
            final User user = userMapper.selectByPrimaryKey(Integer.valueOf(id));
            return user;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 注册：
     * 检查用户名是否重复
     * 检查密码是否一致
     * @param username
     * @param password
     * @return
     */
    @Override
    public String registerUser(String username, String password) {
       try{

           int resultCount = userMapper.checkUsername(username);
           if(resultCount > 0 ){
               return Const.FAILED;
           }
           //密码进行MD5加密：
           String enco_password = DigestUtils.md5DigestAsHex(password.getBytes());
           User user = new User(null,username,enco_password,0);
           userMapper.insert(user);
       }catch (Exception e){
           return Const.FAILED;
       }
        return Const.SUCCESS;
    }

    /**
     * 获得会员总数
     * @return
     */
    @Override
    public int countAll() {
        final UserExample userExample = new UserExample();
        final UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andRoleEqualTo(0);
        final long l = userMapper.countByExample(userExample);
        return Math.toIntExact(l);
    }

    /**
     * 获得会员列表
     * @return
     */
    @Override
    public List<User> selectAllPerson() {
        UserExample userExample = new UserExample();
        final UserExample.Criteria criteria = userExample.createCriteria();
//        criteria.andRoleGreaterThan(3);
        criteria.andRoleEqualTo(0);
        final List<User> userList = userMapper.selectByExample(userExample);

        return userList;
    }

    /**
     * 批量删除人员：
     * @param ids
     * @return
     */
    @Override
    public String deleteMoreProductByIds(List<Integer> ids) {

        try{
            for (int i = 0; i < ids.size(); i++) {
                userMapper.deleteByPrimaryKey(ids.get(i));
            }
        }catch (Exception e){
            return Const.FAILED;
        }

        return Const.SUCCESS;
    }

    /**
     * 修改用户
     * @param personId
     * @param username
     * @param password
     * @param role
     * @return
     */
    @Override
    public String updatePerson(String personId, String username, String password, String role) {
            try{
                final UserExample userExample = new UserExample();
                final UserExample.Criteria criteria = userExample.createCriteria();
                criteria.andIdEqualTo(Integer.valueOf(personId));
                final User user = new User();
                user.setUsername(username);
                user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
                user.setRole(Integer.valueOf(role));
                userMapper.updateByExampleSelective(user,userExample);
            }catch (Exception e){
                return Const.FAILED;
            }
            return Const.SUCCESS;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public String deletePersonById(Integer userId) {

        try{
            userMapper.deleteByPrimaryKey(userId);
        }catch (Exception e){
            return Const.FAILED;
        }

        return Const.SUCCESS;
    }
}
