package com.whattabiz.bibliosbookpoint;

/**
 * Created by User on 8/4/2016.
 */

public class PromoCode {


    private String discount, name;

    public PromoCode() {
    }

    public PromoCode(String discount, String name) {
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



