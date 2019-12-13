package com.supermarket.dao;

import com.supermarket.bean.Payinfo;
import com.supermarket.bean.PayinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayinfoMapper {
    long countByExample(PayinfoExample example);

    int deleteByExample(PayinfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Payinfo record);

    int insertSelective(Payinfo record);

    List<Payinfo> selectByExample(PayinfoExample example);

    Payinfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Payinfo record, @Param("example") PayinfoExample example);

    int updateByExample(@Param("record") Payinfo record, @Param("example") PayinfoExample example);

    int updateByPrimaryKeySelective(Payinfo record);

    int updateByPrimaryKey(Payinfo record);
}