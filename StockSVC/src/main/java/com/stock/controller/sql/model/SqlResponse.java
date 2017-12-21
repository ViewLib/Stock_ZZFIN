package com.stock.controller.sql.model;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
public class SqlResponse {
    private int resultCode;
    private String showMessage;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }
}
