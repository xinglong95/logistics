package com.yusen.logistics.base;

/**
 * Created by Administrator on 2017/6/1. 接口API
 */
public class APIConfig {

    public static final String IP = "http://120.27.125.113:8081";// app接口

    public class User{
        public static final String Login = IP +"/api/Login.ashx";// app接口
    }
    public class ShangPin{
        public static final String AddShangPin = IP +"/api/Product.ashx";// app接口
        public static final String getShangPin = IP +"/api/Waybill.ashx";// app接口

    }

}