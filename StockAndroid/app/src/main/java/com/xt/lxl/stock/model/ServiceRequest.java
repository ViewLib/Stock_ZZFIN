package com.xt.lxl.stock.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public abstract class ServiceRequest implements Serializable {
    public int versionCode = 1;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
