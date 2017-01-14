package com.whattabiz.bibliosbookpoint;

/**
 * Created by User on 8/4/2016.
 */

public class promo {


    private String discount, name;

    public promo() {
    }

    public promo(String discount, String name) {
        this.discount = discount;
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}



