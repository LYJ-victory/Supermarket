package com.supermarket.mapper;

import com.supermarket.bean.TbOrder;
import com.supermarket.bean.TbOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TbOrderMapper {
    long countByExample(TbOrderExample example);

    int deleteByExample(TbOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbOrder record);

    int insertSelective(TbOrder record);

    List<TbOrder> selectByExample(TbOrderExample example);

    TbOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    int updateByExample(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    int updateByPrimaryKeySelective(TbOrder record);

    int updateByPrimaryKey(TbOrder record);

    @Select("select * from tb_order order by id asc")
    List<TbOrder> selectList();
    @Select("select count(*) from tb_order")
    int countAll();
    @Select("select * from tb_order where order_no like  '%${searchyOrderNo}%'  ")
    List<TbOrder> searchOrderByOrderNo(String searchyOrderNo);
}