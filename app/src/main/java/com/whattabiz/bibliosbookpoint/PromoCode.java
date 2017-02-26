package com.whattabiz.bibliosbookpoint;

/**
 * Created by User on 8/4/2016.
 */

public class PromoCode {


    private String id;
    private String msg;
    private String percentage;
    private int limit;

    public PromoCode() {
    }

    public PromoCode(String id, String msg, String percentage) {
        this.id = id;
        this.msg = msg;
        this.percentage = percentage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}



