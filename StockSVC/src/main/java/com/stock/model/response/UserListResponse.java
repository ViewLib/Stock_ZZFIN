package com.stock.model.response;

import com.stock.model.model.StockUserModel;

import java.util.ArrayList;

/**
 * Created by xiangleiliu on 2017/11/3.
 */
public class UserListResponse {
    public int userNum;
    public ArrayList<StockUserModel> userList = new ArrayList<>();
}
