package com.xt.lxl.stock.sender;

import android.util.Log;

import com.xt.lxl.stock.application.StockApplication;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.IOHelper;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.util.StringUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockSender {
    private static StockSender sender;
    private static String mBaseStockUrl = "http://qt.gtimg.cn/q=";
    //    http://qt.gtimg.cn/q=sz300170,sz300171,sz300172,sz300173,sz300174,sz300170,sz300170,sz300170,sz300170,
//    private static String mBaseAPIUrl = "http://10.32.151.30:8090/zzfin/api/";
    private static String mBaseAPIUrl = "http://115.159.31.128:8090/zzfin/api/";
//    http://10.32.151.30:8090/zzfin/api/register?moblie=15601817211
//    http://10.32.151.30:8090/zzfin/api/completion?userid=10000002&moblie=15601817296&nickname=hahahah&area=山东日照&age=28

    private StockSender() {
    }

    public static synchronized StockSender getInstance() {
        if (sender == null) {
            sender = new StockSender();
        }
        return sender;
    }

    public String builderCodeStr(List<String> codeList) {
        StringBuilder builder = new StringBuilder();
        for (String code : codeList) {
            builder.append(DataShowUtil.code2MarketCode(code));
            builder.append(",");
        }
        return builder.toString();
    }

    public List<StockViewModel> requestStockModelByCode(String code) {
        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        return requestStockModelByCode(codeList);
    }

    //线程调用
    public List<StockViewModel> requestStockModelByCode(List<String> codeList) {
        StringBuilder builder = new StringBuilder(mBaseStockUrl);
        builder.append(builderCodeStr(codeList));
        String stockInfoResult = requestGet(builder.toString(), new HashMap<String, String>(), "gbk");
        List<StockViewModel> stockList = new ArrayList<>();
        //解析stockInfo 转换成 StockModel
        if (StringUtil.emptyOrNull(stockInfoResult)) {
            return stockList;
        }
        stockList = DataShowUtil.resultStr2StockList(stockInfoResult);
        return stockList;
    }

    public String requestRegister(String moblie) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("moblie", moblie);
        paramsMap.put("clientId", "123");
        return requestGet(mBaseAPIUrl + "register?", paramsMap, "utf-8");
    }


    private static String requestGet(String baseUrl, HashMap<String, String> paramsMap, String code) {
        try {
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
                String result = IOHelper.fromIputStreamToString(urlConn.getInputStream(), code);
                Log.e("TEST", "Get方式请求成功，result--->" + result);
                return result;
            } else {
                String errorInfo = IOHelper.fromIputStreamToString(urlConn.getInputStream(), code);
                StockShowUtil.showToastOnMainThread(StockApplication.getInstance(), errorInfo);
                Log.e("TEST", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("TEST", e.toString());
            return e.getMessage();
        }
        return "";
    }
}
