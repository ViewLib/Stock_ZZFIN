package com.stock.service;

import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.model.model.*;
import com.stock.model.request.*;
import com.stock.model.response.*;
import com.stock.util.DateUtil;
import com.stock.util.GetDayStocklData;
import com.stock.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.EOFException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class StockService {

    StockDao dao;

    public static StockService stockService;

    private StockService() {
        dao = StockDaoImpl.getDao();
    }

    public static synchronized StockService getInstance() {
        if (stockService == null) {
            stockService = new StockService();
        }
        return stockService;
    }


    public List<StockSearchModel> handleRankList(StockRankListRequest stockRankListResquest, StockRankListResponse stockRankListResponse) {
        List<StockSearchModel> stockSearchModelList = new ArrayList<>();
        List<StockSearchModel> searchModels = dao.selectSerchModelRankList(StockSearchModel.STOCK_SHOW_TYPE_TOP10, 10);
        stockSearchModelList.addAll(searchModels);
        return stockSearchModelList;
    }

    public List<StockSearchModel> handleHotSearchList(StockHotSearchRequest hotSearchRequest, StockHotSearchResponse hotSearchResponse) {
        List<StockSearchModel> stockSearchModelList = new ArrayList<>();
        List<StockSearchModel> searchModels = dao.selectSerchModelRankList(StockSearchModel.STOCK_SHOW_TYPE_HOTSEARCH, 3);
        stockSearchModelList.addAll(searchModels);
        return stockSearchModelList;
    }

    public List<StockRankResultModel> getStockDetail(StockRankDetailResquest rankDetailResquest, StockRankDetailResponse rankDetailResponse) {
        List<StockRankResultModel> rankResultModelList = new ArrayList<>();
        List<StockRankResultModel> rankResultModels = dao.selectRankDetailModelList(rankDetailResquest.search_relation);
        if (rankResultModels.size() > 11) {
            rankResultModelList.addAll(rankResultModels.subList(0, 11));
        } else {
            rankResultModelList.addAll(rankResultModels);
        }
        return rankResultModelList;
    }

    public List<StockSyncModel> getStockSyncList(StockSyncReqeust syncReqeust, StockSyncResponse syncResponse) {
        int version = syncReqeust.version;
        List<StockSyncModel> syncModelList = dao.selectSyncModelList(version);
        return syncModelList;
    }

    public List<StockRankFilterModel> stockRankFilterModels(StockRankDetailFilterlRequest stockRankDetailFilterlRequest, StockRankDetailFilterlResponse stockRankDetailFilterlResponse) {
        // List<StockRankFilterModel> stockRankFilterModelList = new ArrayList<>();
        List<StockRankFilterModel> stockRankFilterModels = dao.selectStockFilterList(stockRankDetailFilterlRequest.first_type);
        //  stockRankFilterModelList.addAll(stockRankFilterModels);
        return stockRankFilterModels;
    }

    //返回筛选大类
    public List<StockFirstTypeModel> stockFirstTypeModels(StockFirstTypeRequest stockFirstTypeRequest, StockFirstTypeResponse stockFirstTypeResponse) {
        List<StockFirstTypeModel> stockFirstTypeModels = dao.selectStockFirstTypList(stockFirstTypeRequest.first_type);
        return stockFirstTypeModels;
    }
    public static  String transCode(String stockCode)throws Exception {
        if(stockCode.length()==8) {
            return stockCode.substring(2, 8) + "." + stockCode.substring(0, 2);

        }
        throw new Exception("Error!") ;
    }
    //返回K线数据
    public List<StockDateDataModel> stockDetailDataModels(StockGetDateDataRequest stockDetailDataRequest, StockGetDateDataResponse stockDetailDataResponse) throws Exception {

        List<StockDateDataModel> stockDetailDataModels = dao.selectStocDetailkDataList(transCode(stockDetailDataRequest.stockCode), stockDetailDataRequest.stockKData);
        return stockDetailDataModels;
    }

    public static String jsoupFetch(String url) throws Exception {
        return Jsoup.parse(new URL(url), 2 * 1000).html();
    }



    public void getMinuteDate(String urlhtml, Map<String, StockMinuteDataModel> stockMinuteDataModelMap) {

        Document doc = Jsoup.parse(urlhtml);
        org.jsoup.nodes.Element table = doc.getElementById("datatbl");
        org.jsoup.select.Elements trs = table.select("tr");
        Integer count = 0;
        String dateTime = "0";

        for (int i = 1; i < trs.size(); ++i) {
            // 获取一个tr
            StockMinuteDataModel stockMinuteDataModel = new StockMinuteDataModel();
            org.jsoup.nodes.Element tr = trs.get(i);
            //获取th节点  成交时间-买卖性质  &&Integer.parseInt(tds.get(3).text().toString())>0
            org.jsoup.select.Elements ths = tr.select("th");
            stockMinuteDataModel.time = ths.get(0).text().toString();
            if (!dateTime.equals(ths.get(0).text().substring(0, 5).toString())) {
                org.jsoup.select.Elements table_price = doc.select("table").select("h6").select("span");
                stockMinuteDataModel.basePrice=Float.parseFloat(table_price.get(0).text().toString())*100;
                dateTime = stockMinuteDataModel.time.substring(0, 5).toString();
                stockMinuteDataModel.time = ths.get(0).text().toString();
                org.jsoup.select.Elements tds = tr.select("td");
                stockMinuteDataModel.price = Float.parseFloat(tds.get(0).text().toString())*100;

                //System.out.print(stockMinuteDataModel.price);
                stockMinuteDataModel.volume = Integer.parseInt(tds.get(3).text().toString());
                String time = stockMinuteDataModel.time;
                if (StringUtil.emptyOrNull(time) || time.length() != 8) {
                    continue;
                }
                time = time.substring(0, 5);
                stockMinuteDataModelMap.put(time, stockMinuteDataModel);
            }
        }
    }

    public Map<String, StockMinuteDataModel> stockMinuteDataModels(StockMinuteDataRequest stockMinuteDataRequest, StockMinuteDataResponse stockMinuteDataResponse) {
        Map<String, StockMinuteDataModel> stockMinuteDataModelMap = new HashMap<>();
        String stockCode = stockMinuteDataRequest.stockCode;
        String urlhtml;


        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol="+stockCode;
        String  currentDate= DateUtil.getCurrentDate();
        Integer flag=dao.getHoliday(currentDate);
        Integer mount=0;
        Calendar c = Calendar.getInstance();
        String nextDate=currentDate;
        try {
             nextDate=GetDayStocklData.getCalDate(currentDate);
        }
        catch (Exception e){

         }
        System.out.println(flag);
        if(flag>0){

           try{
               nextDate=GetDayStocklData.getCalDate(currentDate);
               Calendar calendarByDateStr = DateUtil.getCalendarByDateStr(nextDate);
               String s = DateUtil.calendar2Time(calendarByDateStr, DateUtil.SIMPLEFORMATTYPESTRING7);
               url="http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradehistory.php?symbol="+stockCode+"&date="+s;
           }
              catch (Exception e){
           }
            flag=dao.getHoliday(nextDate);
        }
        for (int i = 1; i < 66; i++) {
              //url = url+"&page="+i;
            try {
                urlhtml = stockService.jsoupFetch(url+"&page="+i);
               // System.out.println(url+"page="+i);
                stockService.getMinuteDate(urlhtml, stockMinuteDataModelMap);
            } catch (Exception e) {

            }
        }

        return stockMinuteDataModelMap;
    }
}
