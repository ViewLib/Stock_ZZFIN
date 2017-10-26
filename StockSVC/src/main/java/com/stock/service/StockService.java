package com.stock.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.dao.StockLinkDaoImpl;
import com.stock.model.model.*;
import com.stock.model.request.*;
import com.stock.model.response.*;
import com.stock.util.DateUtil;
import com.stock.util.GetDayStocklData;
import com.stock.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.net.URL;
import java.util.*;

public class StockService {

    StockDao dao;
    StockLinkDaoImpl linkDao = new StockLinkDaoImpl();

    public static StockService stockService;

    private StockService() {
        dao = StockDaoImpl.getDao();
        linkDao = new StockLinkDaoImpl();
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
        String m = JSONArray.toJSONString(rankDetailResquest.searchlist);
        List<StockRankFilterItemModel> searchlist = rankDetailResquest.searchlist;
        String sqlLists = "";
        if (searchlist.size() > 0) {
            sqlLists = "and stock_code in(";
            for (int i = 0; i < searchlist.size(); i++) {
                if (searchlist.size() > 2) {
                    if (i == 0) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  union  ";
                    }
                    if (i > 0 && i < searchlist.size() - 2) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  union  ";
                    }
                    if (i == searchlist.size() - 1 && searchlist.size() > 2) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId);
                    }
                }
                if (searchlist.size() <= 2) {
                    if (searchlist.size() == 1) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId);
                    }
                    if (searchlist.size() == 2) {
                        if (i == 0) {
                            sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  union  ";
                        } else {
                            sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId);
                        }
                    }
                }
            }
            sqlLists = sqlLists + ")";
        }
        List<StockRankResultModel> rankResultModels = dao.selectRankDetailModelList(rankDetailResquest.search_relation, sqlLists);

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

    public static String transCode(String stockCode) throws Exception {
        //转换一下
        if (stockCode.length() == 6) {
            if (stockCode.startsWith("6")) {
                return stockCode + "." + "sh";
            }
            return stockCode + "." + "sz";
        }

        if (stockCode.length() == 8) {
            return stockCode.substring(2, 8) + "." + stockCode.substring(0, 2);
        }
        throw new Exception("StockCode不合法!");
    }

    //返回filter
    public List<StockRankFilterGroupModel> getStockFilterList(StockRankDetailFilterlRequest stockRankDetailFilterlRequest, StockRankDetailFilterlResponse stockRankDetailFilterlResponse) {
        List<StockRankFilterGroupModel> topList = dao.getStockRankFilterGroup();//查询所有的一级节点
        List<StockRankFilterItemModel> allStockRankFilterItem = dao.getAllStockRankFilterItem();

        Map<Integer, List<StockRankFilterItemModel>> filterItemMap = new HashMap<>();
        for (int i = 0; i < allStockRankFilterItem.size(); i++) {
            StockRankFilterItemModel stockRankFilterItemModel = allStockRankFilterItem.get(i);
            List<StockRankFilterItemModel> stockRankFilterItemModelList = filterItemMap.get(stockRankFilterItemModel.parentGroupId);
            if (stockRankFilterItemModelList == null) {
                stockRankFilterItemModelList = new ArrayList<>();
                filterItemMap.put(stockRankFilterItemModel.parentGroupId, stockRankFilterItemModelList);
            }
            stockRankFilterItemModelList.add(stockRankFilterItemModel);
        }

        for (int i = 0; i < topList.size(); i++) {
            StockRankFilterGroupModel stockRankFilterGroupModel = topList.get(i);
            List<StockRankFilterGroupModel> topGroupModel = dao.getStockRankFilterSubGroup(stockRankFilterGroupModel.groupId);
            stockRankFilterGroupModel.filterGroupList.addAll(topGroupModel);
            for (int j = 0; j < topGroupModel.size(); j++) {
                StockRankFilterGroupModel secondLevelModel = topGroupModel.get(j);
                List<StockRankFilterItemModel> stockRankFilterItemModels = filterItemMap.get(secondLevelModel.groupId);
                if (stockRankFilterItemModels != null) {
                    secondLevelModel.filteList.addAll(stockRankFilterItemModels);
                }
            }
        }
        return topList;
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
        System.out.println(doc);
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
//                org.jsoup.select.Elements table_price = doc.select("table").select("h6").select("span");
//                stockMinuteDataModel.basePrice = Float.parseFloat(table_price.get(0).text().toString()) * 100;
                dateTime = stockMinuteDataModel.time.substring(0, 5).toString();
                stockMinuteDataModel.time = ths.get(0).text().toString();
                org.jsoup.select.Elements tds = tr.select("td");
                stockMinuteDataModel.price = Float.parseFloat(tds.get(0).text().toString()) * 100;

                System.out.print(stockMinuteDataModel.price);
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


        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol=" + stockCode;
        String currentDate = DateUtil.getCurrentDate();
        Integer flag = dao.getHoliday(currentDate);
        Integer mount = 0;
        Calendar c = Calendar.getInstance();
        String nextDate = currentDate;
        try {
            nextDate = GetDayStocklData.getCalDate(currentDate);
        } catch (Exception e) {

        }
        System.out.println(flag);
        if (flag > 0) {

            try {
                nextDate = GetDayStocklData.getCalDate(currentDate);
                Calendar calendarByDateStr = DateUtil.getCalendarByDateStr(nextDate);
                String s = DateUtil.calendar2Time(calendarByDateStr, DateUtil.SIMPLEFORMATTYPESTRING7);
                url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradehistory.php?symbol=" + stockCode + "&date=" + s;
            } catch (Exception e) {
            }
            flag = dao.getHoliday(nextDate);
        }
        for (int i = 1; i < 66; i++) {
            //url = url+"&page="+i;
            try {
                String urltemp = url + "&page=" + i;
                urlhtml = stockService.jsoupFetch(urltemp);
                System.out.println(url + "&page=" + i);
                stockService.getMinuteDate(urlhtml, stockMinuteDataModelMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stockMinuteDataModelMap;
    }

    public StockDetailCompanyModel stockDetailCompanyModels(StockDetailCompanyInfoRequest stockDetailCompanyInfoRequest, StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse) throws Exception {
        String stockCode = transCode(stockDetailCompanyInfoRequest.stockCode);
        StockDetailCompanyModel stockDetailCompanyModels = dao.getCompanyInfo(stockCode);//dao.selectStockFirstTypList(stockFirstTypeRequest.first_type);
        return stockDetailCompanyModels;
    }

    public List<StockDetailStockHolder> stockHolders(StockDetailCompanyInfoRequest stockDetailCompanyInfoRequest, StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse) throws Exception {
        String stockCode = transCode(stockDetailCompanyInfoRequest.stockCode);
        List<StockDetailStockHolder> stockDetailStockHolders = dao.getStockHolder(stockCode);//dao.selectStockFirstTypList(stockFirstTypeRequest.first_type);
        return stockDetailStockHolders;
    }

    public List<StockDetailGradleModel> getStockGrade(StockDetailGradeRequest request, StockDetailGradeResponse response) throws Exception {
        String stockCode = transCode(request.stockCode);
        List<StockDetailGradleModel> resultModelList = new ArrayList<>();//整理成需要的形式
        List<StockDetailGradleModel> gradleModelList = linkDao.selectStockGradle(stockCode);//返回所有的查询结果

        float max = 0;
        float average = 0;
        float min = 0;
        //最高价格
        List<StockDetailGradleModel> maxGradeList = new ArrayList<>();

        //最低价格
        List<StockDetailGradleModel> minGradeList = new ArrayList<>();

        for (int i = 0; i < gradleModelList.size(); i++) {
            StockDetailGradleModel gradleModel = gradleModelList.get(i);
            //最高价格集合
            if (gradleModel.maxPrice > max) {
                maxGradeList.clear();
                maxGradeList.add(gradleModel);
                max = gradleModel.maxPrice;
            } else if (gradleModel.maxPrice == max && gradleModel.maxPrice > 0) {
                maxGradeList.add(gradleModel);
            }

            //最低价格集合
            if (gradleModel.minPrice > 0 && gradleModel.minPrice < min) {
                minGradeList.clear();
                minGradeList.add(gradleModel);
                min = gradleModel.minPrice;
            } else if (gradleModel.minPrice > 0 && gradleModel.minPrice == min) {
                minGradeList.add(gradleModel);
            }
        }
        System.out.println("gradleModelList:" + gradleModelList.size());
        for (StockDetailGradleModel gradleModel : maxGradeList) {
            gradleModel.showPrice = String.valueOf(gradleModel.maxPrice);
        }
        resultModelList.addAll(maxGradeList);
        if (max > 0 && min > 0) {
            average = (max + min) / 2;
            //平均价格
            StockDetailGradleModel averageGrade = new StockDetailGradleModel();
            averageGrade.stockBrokerName = "券商平均价格";
            averageGrade.showPrice = Float.toString(average);
            resultModelList.add(averageGrade);
        }
        for (StockDetailGradleModel gradleModel : minGradeList) {
            gradleModel.showPrice = String.valueOf(gradleModel.minPrice);
        }
        resultModelList.addAll(minGradeList);
        return resultModelList;
    }
}
