package com.stock.service;

import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.model.model.*;
import com.stock.model.request.*;
import com.stock.model.response.*;
import com.stock.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //返回K线数据
    public List<StockDetailDataModel> stockDetailDataModels(StockDetailDataRequest stockDetailDataRequest, StockDetailDataResponse stockDetailDataResponse) {
        List<StockDetailDataModel> stockDetailDataModels = dao.selectStocDetailkDataList(stockDetailDataRequest.stockCode, stockDetailDataRequest.sqlCode);
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
            //获取th节点  成交时间-买卖性质
            org.jsoup.select.Elements ths = tr.select("th");
            stockMinuteDataModel.time = ths.get(0).text().toString();

//            System.out.print(stockMinuteDataModel.dateTime);
//            System.out.print(' ');
//            System.out.print(stockMinuteDataModel.dateTime.substring(0, 5));

            if (!dateTime.equals(ths.get(0).text().substring(0, 5).toString())) {
                dateTime = stockMinuteDataModel.time.substring(0, 5).toString();
                //dateTime=stockMinuteDataModel.time.toString();
                // System.out.println("555:"+stockMinuteDataModel.time.substring(0, 5).toString());
                stockMinuteDataModel.time = ths.get(0).text().toString();

                //stockMinuteDataModel. = ths.get(1).text().toString();
                //  System.out.print(stockMinuteDataModel.time);
                System.out.print(' ');
                //  System.out.print(stockMinuteDataModel.time.substring(0, 5));
                //  System.out.print(stockMinuteDataModel.Property);

                org.jsoup.select.Elements tds = tr.select("td");
                stockMinuteDataModel.price = Float.parseFloat(tds.get(0).text().toString());

                System.out.print(stockMinuteDataModel.price);

                // stockMinuteDataModel.pricePercent = tds.get(1).text().toString();

                // System.out.print(stockMinuteDataModel.pricePercent);

                // stockMinuteDataModel.priceDiff = tds.get(2).text().toString();

                //System.out.print(stockMinuteDataModel.priceDiff);
                stockMinuteDataModel.volume = Integer.parseInt(tds.get(3).text().toString());

                // System.out.print(stockMinuteDataModel.excAmount);

                // stockMinuteDataModel.excMoney = tds.get(4).text().toString();

                //  System.out.print(stockMinuteDataModel.excMoney);
//            for (int j = 0; j < tds.size(); ++j) {
//                org.jsoup.nodes.Element td = tds.get(j);
//                count=count+1;
//                System.out.print(td.text()+' ');
//            }
                // System.out.println("");
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
//      for (int i=1;i<66;i++) {
//          urlhtml = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol="+stockCode+"&page="+i;
//          System.out.println(urlhtml);
//          stockService.getMinuteDate(urlhtml,stockMinuteDataModels);
//      }
        for (int i = 1; i < 14; i++) {

            String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol=sh603997&page=" + i;

            try {
                urlhtml = stockService.jsoupFetch(url);
                System.out.println(url);
                stockService.getMinuteDate(urlhtml, stockMinuteDataModelMap);
            } catch (Exception e) {

            }
        }

        return stockMinuteDataModelMap;
    }
}
