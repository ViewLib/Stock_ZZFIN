package com.stock.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public class ServiceResponse {
    public int resultCode = 0;//返回结果值
    public String resultMessage = "";//结果描述
    public String result = "";//返回结果，json形式

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
