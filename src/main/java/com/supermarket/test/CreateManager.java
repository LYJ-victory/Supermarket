package com.supermarket.test;

import org.springframework.util.DigestUtils;

/**
 * Created by ASUS on 2020/1/14.
 */
public class CreateManager {
    public static void main(String[] args) {
        //spring自带的md5加密：
        String md5Password = DigestUtils.md5DigestAsHex("1".getBytes());
        System.out.println("管理员原密码：1");
        System.out.println("管理员密码："+md5Password);
    }
}
