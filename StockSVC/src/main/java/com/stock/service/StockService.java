package com.stock.service;

import com.alibaba.fastjson.JSONArray;
import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.dao.StockLinkDaoImpl;
import com.stock.model.model.*;
import com.stock.model.request.*;
import com.stock.model.response.*;
import com.stock.util.AmountUtil;
import com.stock.util.DateUtil;
import com.stock.util.GetDayStocklData;
import com.stock.util.StringUtil;
import com.stock.viewmodel.SQLViewModel;
import com.stock.viewmodel.StoctEventSQLResultModel;
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
            sqlLists = "and ts.ts_code in(";
            for (int i = 0; i < searchlist.size(); i++) {
                if (searchlist.size() > 2) {
                    if (i == 0) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  intersect  ";
                    }
                    if (i > 0 && i<searchlist.size() - 1) {
                        sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  intersect  ";
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
                            sqlLists = sqlLists + dao.getListSql(searchlist.get(i).filterId) + "  intersect  ";
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
        if (stockCode.length() == 9) {
            return stockCode;
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
        if(stockDetailCompanyModels.establishDate.contains(" ")){
            stockDetailCompanyModels.establishDate = stockDetailCompanyModels.establishDate.split(" ")[0];
        }
        return stockDetailCompanyModels;
    }

    public List<StockDetailStockHolder> stockHolders(StockDetailCompanyInfoRequest stockDetailCompanyInfoRequest, StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse) throws Exception {
        String stockCode = transCode(stockDetailCompanyInfoRequest.stockCode);
        List<StockDetailStockHolder> stockDetailStockHolders = dao.getStockHolder(stockCode);//dao.selectStockFirstTypList(stockFirstTypeRequest.first_type);
        //针对数据做处理，转换成手
        for (StockDetailStockHolder holder : stockDetailStockHolders) {
            holder.stockHolderAmount = AmountUtil.transHandFromAmount(holder.stockHolderAmount);
            holder.stockHolderRatio = AmountUtil.transRatioFromHave(holder.stockHolderRatio);
        }
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

    public List<StockDetailFinanceGroup> getFinicilaGroup(StockDetailFinanceRequest stockDetailFinanceRequest, StockDetailFinanceResponse stockDetailFinanceResponse) throws Exception {
        List<StockDetailFinanceGroup> stockDetailFinanceGroupList = new ArrayList<>();
        List<StockDetailFinanceItem> stockDetailFinanceItemList = new ArrayList<>();
        String stockCode = transCode(stockDetailFinanceRequest.stockCode);
        for (int i = 1; i < 5; i++) {
            StockDetailFinanceGroup stockDetailFinanceGroup = new StockDetailFinanceGroup();
            stockDetailFinanceItemList = dao.getFinalList(stockCode, i);
            stockDetailFinanceGroup.financeItemList = stockDetailFinanceItemList;
            if (i == 1) {
                stockDetailFinanceGroup.financeName = "收入";
            }
            if (i == 2) {
                stockDetailFinanceGroup.financeName = "净利率";
            }
            if (i == 3) {
                stockDetailFinanceGroup.financeName = "毛利率";
            }
            if (i == 4) {
                stockDetailFinanceGroup.financeName = "分红率";
            }
            //处理下时间格式
            for (StockDetailFinanceItem item : stockDetailFinanceGroup.financeItemList) {
                if (item.dateStr.contains(" ")) {
                    item.dateStr = item.dateStr.split(" ")[0];
                }
            }
            stockDetailFinanceGroup.financeType = i;
            stockDetailFinanceGroupList.add(stockDetailFinanceGroup);
        }
        return stockDetailFinanceGroupList;
    }

    public List<StockEventsDataList> getStockEventsList(StockEventsDataRequest stockEventsDataRequest, StockEventsDataResponse stockEventsDataResponse) throws Exception {
        List<StockEventsDataList> stockEventsDataLists = new ArrayList<>();
        //  List<StockEventDataModel> stockEventsDataModels=new ArrayList<>();

        String stockCode = transCode(stockEventsDataRequest.stockCode);
//        stockEventsDataLists = getTextData();
        //根据类型获取数量
        //根据类型获取sql
        List<SQLViewModel> stockEventSQLList = dao.getStockEventSQL(stockEventsDataRequest.type);

        //根据sql去查询对应的类型
        for (int i = 0; i < stockEventSQLList.size(); i++) {
            SQLViewModel sqlViewModel = stockEventSQLList.get(i);
            List<StoctEventSQLResultModel> stockEventBySQLModel = linkDao.getStockEventBySQLModel(sqlViewModel, stockCode);

            StockEventsDataList stockEvents = new StockEventsDataList();
            stockEvents.eventName = sqlViewModel.sqlTitle;
            stockEvents.eventType = sqlViewModel.sqlType;
            stockEvents.subType = sqlViewModel.subSqlType;
            stockEvents.stockEventsDataModels.clear();
            for (int j = 0; j < stockEventBySQLModel.size(); j++) {
                StoctEventSQLResultModel resultModel = stockEventBySQLModel.get(j);
                StockEventDataModel eventDataModel = transfor2EventDataModel(resultModel, stockEvents);//转换
                stockEvents.stockEventsDataModels.add(eventDataModel);
            }
            stockEventsDataLists.add(stockEvents);
        }
        return stockEventsDataLists;
    }

    public StockEventDataModel transfor2EventDataModel(StoctEventSQLResultModel resultModel, StockEventsDataList stockEvents) {
        int subType = stockEvents.subType;
        StockEventDataModel eventDataModel = new StockEventDataModel();
        if (subType == StockEventsDataList.TYPE_LIFTED) {//解禁
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr4 + "," + resultModel.attr3 + "解禁" + resultModel.attr1 + "股";
        } else if (subType == StockEventsDataList.TYPE_PLEDGE) {//质押
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            //范建震
            eventDataModel.eventDesc = resultModel.attr6 + resultModel.attr1 + "向" + resultModel.attr7 + "质押" + resultModel.attr4 + "万股";
        } else if (subType == StockEventsDataList.TYPE_HOLDER_CHANGE) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate + "，股东人数为：" + resultModel.attr1;
        }
        return eventDataModel;
    }


    private List<StockEventsDataList> getTextData() {
        List<StockEventsDataList> stockEventsDataLists = new ArrayList<>();

        StockEventsDataList eventsDataList1 = new StockEventsDataList();
        eventsDataList1.eventName = "股票解禁";
        eventsDataList1.eventType = StockEventsDataList.TYPE_LIFTED;

        StockEventDataModel model11 = new StockEventDataModel();
        model11.eventDate = "2017-10-29";
        model11.eventTitle = "股权解禁";
        model11.eventDesc = "定向增发机构配售股份，赵旭民解禁3375000股票";

        StockEventDataModel model12 = new StockEventDataModel();
        model12.eventDate = "2017-10-31";
        model12.eventTitle = "股权解禁";
        model12.eventDesc = "首发原股东限售股份，曲水迪宣投资管理合伙企业(有限合伙)解禁3375000股票";

        eventsDataList1.stockEventsDataModels.add(model11);
        eventsDataList1.stockEventsDataModels.add(model12);

        stockEventsDataLists.add(eventsDataList1);


        StockEventsDataList eventsDataList2 = new StockEventsDataList();
        eventsDataList2.eventName = "股权质押";
        eventsDataList2.eventType = StockEventsDataList.TYPE_LIFTED;

        StockEventDataModel model21 = new StockEventDataModel();
        model21.eventDate = "2017-10-29";
        model21.eventTitle = "股权质押";
        model21.eventDesc = "范建震向上海东方证券资产管理有限公司质押股票327.5433万手";

        StockEventDataModel model22 = new StockEventDataModel();
        model22.eventDate = "2017-10-31";
        model22.eventTitle = "股权质押";
        model22.eventDesc = "陈迪清向兴业证券股份有限公司质押股票550万手";

        eventsDataList2.stockEventsDataModels.add(model21);
        eventsDataList2.stockEventsDataModels.add(model22);

        stockEventsDataLists.add(eventsDataList2);

        return stockEventsDataLists;
    }
}
