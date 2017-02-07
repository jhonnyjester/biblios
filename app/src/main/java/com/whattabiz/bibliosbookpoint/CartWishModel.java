package com.whattabiz.bibliosbookpoint;

/**
 * Created by user on 03-09-2016.
 */
public class CartWishModel {

    private String bUrl;
    private String bname;
    private String cst;
    private String mrp;
    private String categoryName;
    private String id;
    private String bisbn;
    private int numberOfItems;

    public CartWishModel(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public CartWishModel() {
        //
    }

    public CartWishModel(String bookname, String cost, String bookUrl) {
        this.bname = bookname;
        this.cst = cost;
        this.bUrl = bookUrl;
    }


    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getBisbn() {
        return bisbn;
    }

    public void setBisbn(String bisbn) {
        this.bisbn = bisbn;
    }

    public String getbUrl() {
        return bUrl;
    }

    public void setbUrl(String bUrl) {
        this.bUrl = bUrl;
    }

    public String getBookname() {
        return bname;
    }

    public void setBookname(String bookname) {
        this.bname = bookname;
    }

    public String getCost() {
        return cst;
    }

    public void setCost(String cost) {
        this.cst = cost;
    }


}
