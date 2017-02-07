package com.whattabiz.bibliosbookpoint;

/**
 * Created by Rumaan on 15-Jan-17.
 */

public class Membership {

    public final String SILVER_MEMBERSHIP = "Silver Membership";
    public final String GOLDEN_MEMBERSHIP = "Golden Membership";
    public final String PLATINUM_MEMBERSHIP = "Platinum Membership";

    private String totalBookPurchased;
    private String totalOrdersWorth;
    private String membershipInfo;
    private String requiredBooksForUpgrade;
    private String requiredPriceForUpgrade;

    private String currentMembershipMessage;

    public Membership() {
        // Default constructor
    }

    public String getTotalBookPurchased() {
        return totalBookPurchased;
    }

    public void setTotalBookPurchased(String totalBookPurchased) {
        this.totalBookPurchased = totalBookPurchased;
    }

    public String getTotalOrdersWorth() {
        return totalOrdersWorth;
    }

    public void setTotalOrdersWorth(String totalOrdersWorth) {
        this.totalOrdersWorth = totalOrdersWorth;
    }

    public String getMembershipInfo() {
        return membershipInfo;
    }

    public void setMembershipInfo(String membershipInfo) {
        this.membershipInfo = membershipInfo;
    }

    public String getRequiredBooksForUpgrade() {
        return requiredBooksForUpgrade;
    }

    public void setRequiredBooksForUpgrade(String requiredBooksForUpgrade) {
        this.requiredBooksForUpgrade = requiredBooksForUpgrade;
    }

    public String getRequiredPriceForUpgrade() {
        return requiredPriceForUpgrade;
    }

    public void setRequiredPriceForUpgrade(String requiredPriceForUpgrade) {
        this.requiredPriceForUpgrade = requiredPriceForUpgrade;
    }

    public String getCurrentMembershipMessage() {
        return currentMembershipMessage;
    }

    public void setCurrentMembershipMessage(String currentMembershipMessage) {
        this.currentMembershipMessage = currentMembershipMessage;
    }
}
