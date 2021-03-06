package com.stock.util;

import com.stock.model.model.StockEventDataModel;
import com.stock.model.model.StockEventsDataList;
import com.stock.viewmodel.StoctEventSQLResultModel;

import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/8.
 */
public class TransformUtil {

    public static String stockCode2SQL(List<String> stockInfoList) {
        String pssql = "";
        for (int i = 0; i < stockInfoList.size(); i++) {
            pssql += "?,";
        }
        if (pssql.endsWith(",")) {
            pssql = pssql.substring(0, pssql.length() - 1);
        }
        return pssql;
    }

    public static String stockCodeList2String(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            builder.append(",");
        }
        if (builder.toString().endsWith(",")) {
            return builder.toString().substring(0, builder.length() - 1);
        }
        return builder.toString();
    }


    public static StockEventDataModel transfor2EventDataModel(StoctEventSQLResultModel resultModel, StockEventsDataList stockEvents) {
        int subType = stockEvents.subType;
        String attr_desc="";
        Integer holer_div=0;
        StockEventDataModel eventDataModel = new StockEventDataModel();
        if (subType == StockEventsDataList.TYPE_LIFTED) {//解禁
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr4 + "," + resultModel.attr3 + "解禁" + AmountUtil.transHandFromAmount(resultModel.attr1);
        } else if (subType == StockEventsDataList.TYPE_PLEDGE) {//质押
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            //范建震
            eventDataModel.eventDesc = resultModel.attr6 + FormatUtil.forDataStr(resultModel.attr1) + "向" + resultModel.attr7 + "质押" + AmountUtil.transHandFromAmount(resultModel.attr4);
        } else if (subType == StockEventsDataList.TYPE_EXCITATION) {
            eventDataModel.eventDate = FormatUtil.forDataStr(resultModel.eventDate);
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = FormatUtil.forDataStr(resultModel.eventDate) + resultModel.attr3 + "，数量：" + AmountUtil.transHandFromAmount(resultModel.attr4) + ",解禁日期：" + FormatUtil.forDataStr(resultModel.attr7);
        } else if (subType == StockEventsDataList.TYPE_EXCHANGE) {//股票置换
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate + "，股权置换数量为：" + AmountUtil.transHandFromAmount(resultModel.attr1);
        } else if (subType == StockEventsDataList.TYPE_INSTITUTIONAL_NUM) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate + "，机构持股数量为：" + AmountUtil.transHandFromAmount(resultModel.attr1) + "，占总股本比例：" + resultModel.attr2 + "%";
        } else if (subType == StockEventsDataList.TYPE_SHAREHOLDER_NUM) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            holer_div= Integer.parseInt(resultModel.attr4);
            if(holer_div>=0){
                attr_desc="股东人数较上期("+resultModel.attr2+")增加："+holer_div+" 人";
            }
            if(holer_div<0){
                attr_desc="股东人数较上期("+resultModel.attr2+")减少："+Math.abs(holer_div)+" 人";
            }
            eventDataModel.eventDesc = resultModel.eventDate + "日，股东数量为：" + FormatUtil.formatStockNum(resultModel.attr1) + "人,"+attr_desc;
        }
        //营业部买卖情况
        else if (subType == StockEventsDataList.TYPE_BUSINESS_DEPT) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc =  resultModel.eventDate+",买方："+resultModel.attr4+",成交价："+resultModel.attr1+"元,成交数量:"+resultModel.attr2+"万股,成交额:"+resultModel.attr3+"万元.卖方："+resultModel.attr5;
        }
        //国家队持股情况
        else if (subType == StockEventsDataList.TYPE_GUOJIADUI_HOLER) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate+","+resultModel.attr2+"持股比例"+resultModel.attr3;
        }
        //经营情况
        else if (subType == StockEventsDataList.TYPE_REVENUE) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate+",营业收入:"+resultModel.attr2+"亿元，净利润:"+resultModel.attr3+"亿元";
        }
        //融资融券
        else if (subType == StockEventsDataList.TYPE_MARGING) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.eventDate+",融资余额:"+resultModel.attr1+"亿元,融资买入额:"+resultModel.attr2+"万元.融券余额:"+resultModel.attr4+"万元,融券卖出量:"+resultModel.attr6+"股."+"融资融券余额:"+resultModel.attr8+"亿元";
        }
        //重组
        else if (subType == StockEventsDataList.TYPE_REFORM) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr1;
        }
        //定增
        else if (subType == StockEventsDataList.TYPE_SETBY) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr1;
        }
        //大宗交易
        else if (subType == StockEventsDataList.TYPE_BLOCK_TRADING) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr1;
        }
        //分红
        else if (subType == StockEventsDataList.TYPE_DIVIEND) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr1;
        }
        //减持
        else if (subType == StockEventsDataList.TYPE_REDUCE) {
            eventDataModel.eventDate = resultModel.eventDate;
            eventDataModel.eventTitle = stockEvents.eventName;
            eventDataModel.eventDesc = resultModel.attr1;
        }
        return eventDataModel;
    }
}
