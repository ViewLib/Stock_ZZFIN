package com.xt.lxl.stock.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public abstract class ServiceResponse {
    public int resultCode = 200;
    public String resultMessage = "";

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
