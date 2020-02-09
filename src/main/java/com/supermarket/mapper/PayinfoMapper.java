package com.supermarket.mapper;

import com.supermarket.bean.Payinfo;
import com.supermarket.bean.PayinfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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