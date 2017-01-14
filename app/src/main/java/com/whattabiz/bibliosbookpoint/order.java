package com.whattabiz.bibliosbookpoint;

/**
 * Created by User on 8/4/2016.
 */

public class order {


    private String imgurl, name, date, price;

    public order() {
    }

    public order(String imgurl, String name, String date, String price) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.imgurl = imgurl;
    }

    public String getImgUrl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}



