package com.whattabiz.bibliosbookpoint;

/**
 * Created by Rumaan on 18-09-2016.
 */

public class Categories {
    private String bUrl;
    private String bname;
    private String authr;
    private String cst;
    private String mrp;
    private String bisbn;
    private String categoryName;
    private String id;

    public Categories(String bUrl, String authr, String bname, String cst, String bisbn) {
        this.bUrl = bUrl;
        this.authr = authr;
        this.bname = bname;
        this.cst = cst;
        this.bisbn = bisbn;
    }

    public Categories(String categoryName, String id) {
        this.categoryName = categoryName;
        this.id = id;
    }

    public Categories() {
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

    public void setBookname(String bname) {
        this.bname = bname;
    }

    public String getAuthr() {
        return authr;
    }

    public void setAuthr(String authr) {
        this.authr = authr;
    }

    public String getCost() {
        return cst;
    }

    public void setCost(String cst) {
        this.cst = cst;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getBisbn() {
        return bisbn;
    }

    public void setBisbn(String bisbn) {
        this.bisbn = bisbn;
    }

    public String getCategoryName() {
        return categoryName;
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
}
