package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockMinuteDataModel;
import com.stock.model.request.StockMinuteDataRequest;
import com.stock.model.response.StockMinuteDataResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;
import com.stock.util.DateUtil;
import org.jsoup.helper.DataUtil;

import javax.servlet.annotation.WebServlet;
import javax.xml.crypto.Data;
import java.util.*;

/**
 * Created by hp on 2017/10/18.StockMinuteDataServlet
 */
@WebServlet(name = "StockMinuteDataServlet")
public class StockMinuteDataServlet extends BaseServlet {
    StockService stockService;

    public StockMinuteDataServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockMinuteDataRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if (flag) {
            c = StockMinuteDataResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockMinuteDataRequest stockMinuteDataRequest = (StockMinuteDataRequest) serviceRequest;
        StockMinuteDataResponse stockMinuteDataResponse = (StockMinuteDataResponse) serviceResponse;
        Map<String, StockMinuteDataModel> stringStockMinuteDataModelMap = stockService.stockMinuteDataModels(stockMinuteDataRequest, stockMinuteDataResponse);//map类型
        List<StockMinuteDataModel> minuteDataModelList = new ArrayList<>();

        stockMinuteDataResponse.stockCode = stockMinuteDataRequest.stockCode;
        stockMinuteDataResponse.stockName = stockMinuteDataRequest.stockName;
        stockMinuteDataResponse.stockMinuteDataModels = minuteDataModelList;

        //获取当前时间
        Calendar currentCalendar = DateUtil.getCurrentCalendar();

        Calendar historyTime = new GregorianCalendar();
        historyTime.setTime(currentCalendar.getTime());
        historyTime.set(Calendar.HOUR_OF_DAY, 9);
        historyTime.set(Calendar.MINUTE, 30);

        while (true) {
            if (historyTime.getTimeInMillis() >= currentCalendar.getTimeInMillis()) {
                break;
            }
            StockMinuteDataModel minuteDataModel = new StockMinuteDataModel();
            minuteDataModel.time = DateUtil.calendar2Time(historyTime, DateUtil.SIMPLEFORMATTYPESTRING13);
            StockMinuteDataModel mapModel = stringStockMinuteDataModelMap.get(minuteDataModel.time);
            if (mapModel == null) {
                mapModel = getLastStockMinuteDataModel(minuteDataModelList, minuteDataModel.time);
            }
            minuteDataModel.price = mapModel.price;
            minuteDataModel.volume = mapModel.volume;
            minuteDataModel.state = mapModel.state;
            minuteDataModel.basePrice = mapModel.basePrice;

            minuteDataModelList.add(minuteDataModel);

            int hour = historyTime.get(Calendar.HOUR_OF_DAY);
            int minute = historyTime.get(Calendar.MINUTE);

            //如果时间为11点半到1点之间，直接跳过
            if ((hour == 11 && minute > 30) || hour == 12) {
                historyTime.set(Calendar.HOUR_OF_DAY, 13);
                historyTime.set(Calendar.MINUTE, 0);
            } else if (hour >= 15) {//如果是13点以后，直接跳过
                break;
            } else {
                historyTime.add(Calendar.MINUTE, 1);
            }
        }
    }

    public StockMinuteDataModel getLastStockMinuteDataModel(List<StockMinuteDataModel> minuteDataModelList, String time) {
        StockMinuteDataModel minuteDataModel = new StockMinuteDataModel();
        if (minuteDataModelList == null) {
            minuteDataModel.time = time;
            return minuteDataModel;
        }
        for (int i = minuteDataModelList.size() - 1; i >= 0; i++) {
            StockMinuteDataModel minuteDataModel1 = minuteDataModelList.get(i);
            minuteDataModel.time = time;
            minuteDataModel.volume = 0;
            minuteDataModel.price = minuteDataModel1.price;
            break;
        }
        return minuteDataModel;
    }

}

