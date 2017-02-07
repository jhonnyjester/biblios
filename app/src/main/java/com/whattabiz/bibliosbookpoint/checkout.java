package com.whattabiz.bibliosbookpoint;

/**
 * Created by User on 8/4/2016.
 */

public class checkout {


    private String name, price;

    public checkout() {
    }

    public checkout(String name, String price) {
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String detail) {
        this.price = detail;
    }
}



