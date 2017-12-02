package com.xt.lxl.stock.sender;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xt.lxl.stock.application.StockApplication;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockDetailCompanyInfoResponse;
import com.xt.lxl.stock.model.reponse.StockDetailCompareResponse;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.model.reponse.StockDetailGradeResponse;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetMinuteDataResponse;
import com.xt.lxl.stock.model.reponse.StockHotSearchResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;
import com.xt.lxl.stock.model.reponse.StockRankListResponse;
import com.xt.lxl.stock.model.reponse.StockSyncResponse;
import com.xt.lxl.stock.model.reponse.StockUserRegisterResponse;
import com.xt.lxl.stock.model.request.StockDetailCompanyInfoRequest;
import com.xt.lxl.stock.model.request.StockDetailCompareRequest;
import com.xt.lxl.stock.model.request.StockDetailFinanceRequest;
import com.xt.lxl.stock.model.request.StockDetailGradeRequest;
import com.xt.lxl.stock.model.request.StockEventsDataRequest;
import com.xt.lxl.stock.model.request.StockHotSearchRequest;
import com.xt.lxl.stock.model.request.StockRankDetailFilterlRequest;
import com.xt.lxl.stock.model.request.StockRankDetailResquest;
import com.xt.lxl.stock.model.request.StockRankListResquest;
import com.xt.lxl.stock.model.request.StockSyncReqeust;
import com.xt.lxl.stock.model.request.StockUserRegisterRequest;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.IOHelper;
import com.xt.lxl.stock.util.LogUtil;
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
    private static String mBaseUrl = "http://115.159.31.128/";
    private static String mBaseAPIUrl = "http://115.159.31.128:8090/zzfin/api/";


    /**
     * http://115.159.31.128/stockMinute?stockCode=300170&date=2017-11-13
     * 日线：http://115.159.31.128/stockDay?stockCode=300170
     * 周线：http://115.159.31.128/stockWeek?stockCode=300170
     * 月线：http://115.159.31.128/stockMonth?stockCode=300170
     */


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
        detailResquest.search_relation = search_reletion;
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

    public StockGetMinuteDataResponse requestMinuteData(String stockCode, String date) {
        HashMap<String, String> params = new HashMap<>();
        params.put("stockCode", stockCode);
        params.put("date", date);
        String s = requestGet(mBaseUrl + "stockMinute?", params, "utf-8");
        if (s.length() < 100) {
            LogUtil.LogI("requestMinuteData返回结果异常：" + s);
        }
        s = "{\"stockMinuteDataModels\":" + s + "}";
        StockGetMinuteDataResponse getMinuteDataResponse;
        try {
            getMinuteDataResponse = JSON.parseObject(s, StockGetMinuteDataResponse.class);
        } catch (Exception e) {
            getMinuteDataResponse = new StockGetMinuteDataResponse();
            getMinuteDataResponse.resultCode = 500;
            getMinuteDataResponse.resultMessage = "序列化失败";
            LogUtil.LogE("requestMinuteData接口异常：" + e.getMessage());
        }
        return getMinuteDataResponse;
    }

    /**
     * 日线，周线，月线数据
     *
     * @param stockCode
     * @param getType
     * @return
     */
    public StockGetDateDataResponse requestDateData(String stockCode, String getType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("stockCode", stockCode);
        String s = requestGet(mBaseUrl + getType + "?", params, "utf-8");
        s = "{\"dateDataList\":" + s + "}";
        StockGetDateDataResponse getDateDataResponse;
        try {
            getDateDataResponse = JSON.parseObject(s, StockGetDateDataResponse.class);
            getDateDataResponse.stockKDataType = getType;
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
     * 公司评级服务
     *
     * @param stockCode
     * @return
     */
    public StockDetailGradeResponse requestStockGradeService(String stockCode) {
        StockDetailGradeRequest reqeust = new StockDetailGradeRequest();
        reqeust.stockCode = stockCode;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_grade?", requestJsonStr, "utf-8");
        StockDetailGradeResponse gradeResponse;
        try {
            gradeResponse = JSON.parseObject(s, StockDetailGradeResponse.class);
        } catch (Exception e) {
            gradeResponse = new StockDetailGradeResponse();
            gradeResponse.resultCode = 500;
            gradeResponse.resultMessage = "序列化失败";
        }
        return gradeResponse;
    }

    public StockDetailFinanceResponse requestFinanceService(String stockCode) {
        StockDetailFinanceRequest reqeust = new StockDetailFinanceRequest();
        reqeust.stockCode = stockCode;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_finicial?", requestJsonStr, "utf-8");
        StockDetailFinanceResponse response;
        try {
            response = JSON.parseObject(s, StockDetailFinanceResponse.class);
        } catch (Exception e) {
            response = new StockDetailFinanceResponse();
            response.resultCode = 500;
            response.resultMessage = "序列化失败";
        }
        return response;
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

    /**
     * 重大事件
     *
     * @param requestStockCode
     * @return
     */
    public StockEventsDataResponse requestStockEventImportant(String requestStockCode, int type) {
        StockEventsDataRequest reqeust = new StockEventsDataRequest();
        reqeust.stockCode = requestStockCode;
        reqeust.type = type;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_event?", requestJsonStr, "utf-8");
        StockEventsDataResponse eventsDataResponse;
        try {
            eventsDataResponse = JSON.parseObject(s, StockEventsDataResponse.class);
        } catch (Exception e) {
            eventsDataResponse = new StockEventsDataResponse();
            eventsDataResponse.resultCode = 500;
            eventsDataResponse.resultMessage = "序列化失败";
        }
        return eventsDataResponse;
    }

    /**
     * 横向比较
     *
     * @param requestStockCode
     * @return
     */
    public StockDetailCompareResponse requestStockCompare(String requestStockCode) {
        StockDetailCompareRequest reqeust = new StockDetailCompareRequest();
        reqeust.stockCode = requestStockCode;
        String requestJsonStr = JSON.toJSONString(reqeust);
        String s = requestGet(mBaseAPIUrl + "stock_compare?", requestJsonStr, "utf-8");
        StockDetailCompareResponse eventsDataResponse;
        try {
            eventsDataResponse = JSON.parseObject(s, StockDetailCompareResponse.class);
        } catch (Exception e) {
            eventsDataResponse = new StockDetailCompareResponse();
            eventsDataResponse.resultCode = 500;
            eventsDataResponse.resultMessage = "序列化失败";
        }
        return eventsDataResponse;
    }

    /**
     * 年度涨幅的接口
     *
     * @param requestStockCode
     * @return
     */
    public float requestForwadPrice(String requestStockCode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("stockCode", requestStockCode);
        String s = requestGet("http://115.159.31.128/yearPrice?", hashMap, "utf-8");

        try {
            JSONArray objects = JSON.parseArray(s);
            JSONObject jsonObject = objects.getJSONObject(0);
            Float close = jsonObject.getFloat("close");
            return close;
        } catch (Exception e) {

        }
        return 0f;
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
//                Log.e("TEST", "Get方式请求成功，result--->" + result);
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
