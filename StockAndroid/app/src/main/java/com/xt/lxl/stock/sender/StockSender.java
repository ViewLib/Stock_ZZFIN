package com.xt.lxl.stock.sender;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xt.lxl.stock.application.StockApplication;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockDetailCompanyInfoResponse;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetMinuteDataResponse;
import com.xt.lxl.stock.model.reponse.StockHotSearchResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;
import com.xt.lxl.stock.model.reponse.StockRankListResponse;
import com.xt.lxl.stock.model.reponse.StockSyncResponse;
import com.xt.lxl.stock.model.reponse.StockUserRegisterResponse;
import com.xt.lxl.stock.model.request.StockDetailCompanyInfoRequest;
import com.xt.lxl.stock.model.request.StockGetDateDataRequest;
import com.xt.lxl.stock.model.request.StockGetMinuteDataRequest;
import com.xt.lxl.stock.model.request.StockHotSearchRequest;
import com.xt.lxl.stock.model.request.StockRankDetailFilterlRequest;
import com.xt.lxl.stock.model.request.StockRankDetailResquest;
import com.xt.lxl.stock.model.request.StockRankListResquest;
import com.xt.lxl.stock.model.request.StockSyncReqeust;
import com.xt.lxl.stock.model.request.StockUserRegisterRequest;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.IOHelper;
import com.xt.lxl.stock.util.StockUtil;
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

    public StockUserRegisterResponse requestRegister(String moblie, String clientId) {
        StockUserRegisterRequest registerRequest = new StockUserRegisterRequest();
        registerRequest.moblie = moblie;
        registerRequest.clientId = clientId;
        String requestJsonStr = JSON.toJSONString(registerRequest);
        String s = requestGet(mBaseAPIUrl + "user_register?", requestJsonStr, "utf-8");
        StockUserRegisterResponse registerResponse;
        try {
            registerResponse = JSON.parseObject(s, StockUserRegisterResponse.class);
        } catch (Exception e) {
            registerResponse = new StockUserRegisterResponse();
            registerResponse.resultCode = 500;
            registerResponse.resultMessage = "序列化失败";
        }
        return registerResponse;
    }


    public StockRankListResponse requestRankList() {
        StockRankListResquest registerRequest = new StockRankListResquest();
        String requestJsonStr = JSON.toJSONString(registerRequest);
        String s = requestGet(mBaseAPIUrl + "stock_ranklist?", requestJsonStr, "utf-8");
        StockRankListResponse rankResponse;
        try {
            rankResponse = JSON.parseObject(s, StockRankListResponse.class);
        } catch (Exception e) {
            rankResponse = new StockRankListResponse();
            rankResponse.resultCode = 500;
            rankResponse.resultMessage = "序列化失败";
        }
        return rankResponse;
    }

    public StockHotSearchResponse requestHosSearchList() {
        StockHotSearchRequest registerRequest = new StockHotSearchRequest();
        String requestJsonStr = JSON.toJSONString(registerRequest);
        String s = requestGet(mBaseAPIUrl + "stock_hotsearch?", requestJsonStr, "utf-8");
        StockHotSearchResponse hotSearchResponse;
        try {
            hotSearchResponse = JSON.parseObject(s, StockHotSearchResponse.class);
        } catch (Exception e) {
            hotSearchResponse = new StockHotSearchResponse();
            hotSearchResponse.resultCode = 500;
            hotSearchResponse.resultMessage = "序列化失败";
        }
        return hotSearchResponse;
    }

    public StockRankDetailResponse requestRankDetailList(String title, int search_reletion, List<StockRankFilterItemModel> searchList) {
        StockRankDetailResquest detailResquest = new StockRankDetailResquest();
        detailResquest.title = title;
        detailResquest.serch_relation = search_reletion;
        detailResquest.searchlist = searchList;
        String requestJsonStr = JSON.toJSONString(detailResquest);
        String s = requestGet(mBaseAPIUrl + "stock_rankdetail?", requestJsonStr, "utf-8");
        StockRankDetailResponse detailResponse;
        try {
            detailResponse = JSON.parseObject(s, StockRankDetailResponse.class);
        } catch (Exception e) {
            detailResponse = new StockRankDetailResponse();
            detailResponse.resultCode = 500;
            detailResponse.resultMessage = "序列化失败";
        }
        return detailResponse;
    }

    public StockSyncResponse requestStockSync(int version) {
        StockSyncReqeust reqeust = new StockSyncReqeust();
        reqeust.version = version;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_hotsearch?", requestJsonStr, "utf-8");
        StockSyncResponse stockSyncResponse;
        try {
            stockSyncResponse = JSON.parseObject(s, StockSyncResponse.class);
        } catch (Exception e) {
            stockSyncResponse = new StockSyncResponse();
            stockSyncResponse.resultCode = 500;
            stockSyncResponse.resultMessage = "序列化失败";
        }
        return stockSyncResponse;
    }

    public StockGetMinuteDataResponse requestMinuteData(String stockCode) {
        StockGetMinuteDataRequest reqeust = new StockGetMinuteDataRequest();
        reqeust.stockCode = stockCode;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_minute?", requestJsonStr, "utf-8");
        StockGetMinuteDataResponse getMinuteDataResponse;
        try {
            getMinuteDataResponse = JSON.parseObject(s, StockGetMinuteDataResponse.class);
        } catch (Exception e) {
            getMinuteDataResponse = new StockGetMinuteDataResponse();
            getMinuteDataResponse.resultCode = 500;
            getMinuteDataResponse.resultMessage = "序列化失败";
        }
        return getMinuteDataResponse;
    }

    public StockGetDateDataResponse requestDateData(String stockCode, String getType) {
        StockGetDateDataRequest reqeust = new StockGetDateDataRequest();
        reqeust.stockCode = stockCode;
        reqeust.stockKData = getType;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_data?", requestJsonStr, "utf-8");
        StockGetDateDataResponse getDateDataResponse;
        try {
            getDateDataResponse = JSON.parseObject(s, StockGetDateDataResponse.class);
        } catch (Exception e) {
            getDateDataResponse = new StockGetDateDataResponse();
            getDateDataResponse.resultCode = 500;
            getDateDataResponse.resultMessage = "序列化失败";
        }
        return getDateDataResponse;
    }

    /**
     * 公司详情服务
     *
     * @param stockCode
     * @return
     */
    public StockDetailCompanyInfoResponse requestStockCompanyService(String stockCode) {
        StockDetailCompanyInfoRequest reqeust = new StockDetailCompanyInfoRequest();
        reqeust.stockCode = stockCode;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_info?", requestJsonStr, "utf-8");
        StockDetailCompanyInfoResponse getDateDataResponse;
        try {
            getDateDataResponse = JSON.parseObject(s, StockDetailCompanyInfoResponse.class);
        } catch (Exception e) {
            getDateDataResponse = new StockDetailCompanyInfoResponse();
            getDateDataResponse.resultCode = 500;
            getDateDataResponse.resultMessage = "序列化失败";
        }
        return getDateDataResponse;
    }


    /**
     * 筛选列表
     *
     * @return
     */
    public StockRankDetailFilterlResponse requestFilterList() {
        StockRankDetailFilterlRequest reqeust = new StockRankDetailFilterlRequest();
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_rankfilter?", requestJsonStr, "utf-8");
        StockRankDetailFilterlResponse filterlResponse;
        try {
            filterlResponse = JSON.parseObject(s, StockRankDetailFilterlResponse.class);
        } catch (Exception e) {
            filterlResponse = new StockRankDetailFilterlResponse();
            filterlResponse.resultCode = 500;
            filterlResponse.resultMessage = "序列化失败";
        }
        return filterlResponse;
    }

    private static String requestGet(String baseUrl, String requestJsonStr, String code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data", requestJsonStr);
        return requestGet(baseUrl, paramsMap, "utf-8");
    }

    private static String requestGet(String baseUrl, HashMap<String, String> paramsMap, String code) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), code)));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(10 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(10 * 1000);
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
                StockUtil.showToastOnMainThread(StockApplication.getInstance(), errorInfo);
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
