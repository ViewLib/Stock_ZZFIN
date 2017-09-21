package com.xt.lxl.stock.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public abstract class ServiceRequest {
    public int versionCode = 1;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
