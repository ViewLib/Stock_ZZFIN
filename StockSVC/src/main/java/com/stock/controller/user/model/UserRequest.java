package com.stock.controller.user.model;

/**
 * Created by 杨蕾 on 2017/11/12.
 */
public class UserRequest {
    private int userId;//用户id
    private String moblie;//手机号
    private String nickName;//用户昵称
    private String area;//地区
    private int age;//年龄

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
}
