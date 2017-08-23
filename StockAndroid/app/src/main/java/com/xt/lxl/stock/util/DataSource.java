package com.xt.lxl.stock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.model.StockViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class DataSource {

    public static void initStockList(List<StockViewModel> mStockList) {
        StockViewModel viewModel1 = new StockViewModel();
        StockViewModel viewModel2 = new StockViewModel();
        mStockList.add(viewModel1);
        mStockList.add(viewModel2);

        viewModel1.mStockName = "汉得信息";
        viewModel1.mStockPirce = "10.26";
        viewModel1.mStockCode = "300170";
        viewModel1.mStockChange = -0.0182;

        viewModel2.mStockName = "朗新科技";
        viewModel2.mStockPirce = "10.55";
        viewModel2.mStockCode = "300682";
        viewModel2.mStockChange = 0.1001;

        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
    }

    public static List<String> getSaveStockCodeList(Context context) {
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        String codeListStr = codeList.getString(StockConfig.STOCK_SAVE_DATA_NAME, "");
        if (StringUtil.emptyOrNull(codeListStr)) {
            return new ArrayList<>();
        }
        if (codeListStr.endsWith(",")) {
            String substring = codeListStr.substring(0, codeListStr.length() - 1);
            codeListStr = substring;
        }
        String[] split = codeListStr.split(",");
        return Arrays.asList(split);
    }

    public static boolean addStockCode(Context context, String code) {
        List<String> list = new ArrayList<>();
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        String codeListStr = codeList.getString(StockConfig.STOCK_SAVE_DATA_NAME, "");
        if (!StringUtil.emptyOrNull(codeListStr)) {
            if (codeListStr.endsWith(",")) {
                codeListStr = codeListStr.substring(0, codeListStr.length() - 1);
            }
            String[] split = codeListStr.split(",");
            list.addAll(Arrays.asList(split));
        }
        list.add(code);
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(",");
        }
        return codeList.edit().putString(StockConfig.STOCK_SAVE_DATA_NAME, builder.toString()).commit();
    }

}
