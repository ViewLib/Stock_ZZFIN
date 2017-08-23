package com.xt.lxl.stock.sender;

import android.util.Log;

import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.util.IOHelper;
import com.xt.lxl.stock.util.StringUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockSender {


    //线程调用
    public static StockViewModel requestStockModelByCode(String code) {
        StringBuilder builder = new StringBuilder("http://hq.sinajs.cn/list=");
        if (code.startsWith("6")) {
            builder.append("sh");
            builder.append(code);
        } else {
            builder.append("sz");
            builder.append(code);
        }
        String stockInfo = requestGet(builder.toString(), new HashMap<String, String>());
        StockViewModel stockViewModel = new StockViewModel();
        //解析stockInfo 转换成 StockModel
        if (StringUtil.emptyOrNull(stockInfo)) {
            return stockViewModel;
        }
        int first = stockInfo.indexOf("\"");
        int second = stockInfo.indexOf("\"", first + 1);
        String substring = stockInfo.substring(first + 1, second);
        String[] split = substring.split(",");
        if (split.length < 5) {
            return stockViewModel;
        }
        String stockName = split[0];
        String stockyestodayPrice = split[2];
        String stockPrice = split[3];


        double stockPriceD = Double.parseDouble(stockPrice);
        double stockyestodayPriceD = Double.parseDouble(stockyestodayPrice);

        stockViewModel.mStockName = stockName;
        stockViewModel.mStockCode = code;
        stockViewModel.mStockPirce = stockPrice;
        stockViewModel.mStockType = StockViewModel.STOCK_TYPE_CHINA;
        stockViewModel.mStockChange = (stockPriceD - stockyestodayPriceD) / stockyestodayPriceD;
        if (stockPriceD == 0 && stockyestodayPriceD > 0) {
            stockViewModel.mStockState = StockViewModel.STOCK_STATE_SUSPENSION;
            stockViewModel.mStockPirce = stockyestodayPrice;
        } else {
            stockViewModel.mStockState = StockViewModel.STOCK_STATE_NORMAL;
        }

        return stockViewModel;
    }


    private static String requestGet(String baseUrl, HashMap<String, String> paramsMap) {
        try {
//            String baseUrl = "https://xxx.com/getUsers?";
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "gbk")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = IOHelper.fromIputStreamToString(urlConn.getInputStream());
                Log.e("TEST", "Get方式请求成功，result--->" + result);
                return result;
            } else {
                Log.e("TEST", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("TEST", e.toString());
        }
        return "";
    }
}
