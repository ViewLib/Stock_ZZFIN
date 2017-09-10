package com.stock.config;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public class Config {

    //定义的参数

    /**
     * 用户注册
     * http://localhost:5389/register?moblie=86_17863333330&clientId=32001000010001
     * http://115.159.31.128:8090/zzfin/api/register?moblie=86_17863333333&clientId=32001000010001
     */
    public String REGISTER = "http://localhost:8080/zzfin/api/register?moblie=86_17863333330&clientId=32001000010001";

    /**
     * 用户补全
     *
     * http://115.159.31.128:8090/zzfin/api/completion?userid=10000003&moblie=86_17863333330&nickname=祥磊&area=上海&age=27
     */
    public String COMPLETION = "http://localhost:8080/zzfin/api/completion?userid=10000002&moblie=86_17863333330&nickname=哈哈&area=山东日照&age=28";
}
