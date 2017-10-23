package com.xt.lxl.stock.model.model;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
public class StockUserModel extends StockBaseModel {

    public int userId;//用户id
    public String moblie;//手机号
    public String clientId;//clinetId
    public String nickName;//用户昵称
    public String area;//地区
    public int age;//年龄
    public Date createTime;//注册时间

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
