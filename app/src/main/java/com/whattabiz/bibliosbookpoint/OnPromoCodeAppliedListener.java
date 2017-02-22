package com.whattabiz.bibliosbookpoint;

/**
 * Created by Rumaan on 22-Feb-17.
 */

/* Create a Interface for handling PromoCode Applies */
public interface OnPromoCodeAppliedListener {
    // Pass in the PromoCode id
    void OnPromoCodeApplied(String promoId, float percentage);
}