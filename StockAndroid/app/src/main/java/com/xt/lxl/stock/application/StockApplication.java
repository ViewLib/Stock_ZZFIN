package com.xt.lxl.stock.application;

import com.mob.MobApplication;
import com.tencent.bugly.Bugly;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockApplication extends MobApplication {

    static StockApplication instance;

    public StockApplication() {
    }

    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "62ccdd2222", false);
        instance = this;
    }

    public static StockApplication getInstance() {
        return instance;
    }

}
