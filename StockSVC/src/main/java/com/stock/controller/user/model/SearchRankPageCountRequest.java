package com.stock.controller.user.model;

public class SearchRankPageCountRequest {
    private int startIndex;
    private int count;

    public SearchRankPageCountRequest(){
        this.startIndex = 1;
        this.count = 10;
    }

    public SearchRankPageCountRequest(int startIndex, int count){
        this.startIndex = startIndex;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
