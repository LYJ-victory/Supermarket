package com.supermarket.test;

import java.util.UUID;

/**
 * 生成订单号
 */
public class CreateOrderNo {

    public static void main(String[] args) {
        System.out.println(CreateOrderNo.orderNo());
    }
    /**
     * 生成唯一订单号
     * @return
     */
    public static String orderNo(){
        //最大支持1-9个集群机器部署:
        int machineId = 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数:
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        //         0 代表前面补充0
        //         4 代表长度为4
        //         d 代表参数为正数型
        return  machineId+ String.format("%015d", hashCodeV);
    }
}
