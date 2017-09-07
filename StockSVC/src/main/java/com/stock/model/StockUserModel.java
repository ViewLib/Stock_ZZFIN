package com.stock.model;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
public class StockUserModel {

    public int mUserId;//用户id
    public String mMoblie;//手机号
    public String mClientId;//clinetId
    public String mNickName;//用户昵称
    public String mArea;//地区
    public int mAge;//年龄
    public Date mCreateTime;//注册时间

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
