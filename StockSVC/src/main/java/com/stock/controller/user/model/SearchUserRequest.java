package com.stock.controller.user.model;

/**
 * Created by 杨蕾 on 2017/11/19.
 */
public class SearchUserRequest {
    private int limit;    //页面大小
    private int offset;
    private int maxrows;
    private int pageindex;
    private String userid;
    private String mobile;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMaxrows() {
        return maxrows;
    }

    public void setMaxrows(int maxrows) {
        this.maxrows = maxrows;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
