package com.stock.model.model;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
public class StockUserModel {

    private int userId;//用户id
    private String moblie;//手机号
    private String clientId;//clinetId
    private String nickName;//用户昵称
    private String area;//地区
    private int age;//年龄
    private String createTime;//注册时间

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
