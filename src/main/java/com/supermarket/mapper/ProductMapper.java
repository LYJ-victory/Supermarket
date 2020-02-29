package com.supermarket.mapper;

import com.supermarket.bean.Product;
import com.supermarket.bean.ProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ProductMapper {
    long countByExample(ProductExample example);

    int deleteByExample(ProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);


    @Select("select * from product order by price desc ")
    List<Product> selectList();

//    SELECT * FROM (SELECT * FROM product GROUP BY `status`) as product ORDER BY price DESC
    @Select("select count(*) from product")
    int countAll();

    /**
     * 模糊查询商品按名称
     * @param searchProductName
     * @return
     */
    @Select("select * from product where name like  '%${searchProductName}%'  ")
    List<Product> selectByProductName(String searchProductName);
}