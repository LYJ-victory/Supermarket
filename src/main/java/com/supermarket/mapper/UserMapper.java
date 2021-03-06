package com.supermarket.mapper;

import com.supermarket.bean.Product;
import com.supermarket.bean.User;
import com.supermarket.bean.UserExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //自定义：
    @Select("select count(1) from tb_user where username = #{username}")
    int checkUsername(String username);

    @Select("select * from tb_user where username = #{username} and password = #{password}")
    User selectLogin(@Param("username") String username, @Param("password")String password);

    @Select("select * from tb_user order by id asc")
    List<User> selectList();
}