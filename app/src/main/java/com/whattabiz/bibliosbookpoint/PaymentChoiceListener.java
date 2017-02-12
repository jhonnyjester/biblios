package com.whattabiz.bibliosbookpoint;

import android.content.DialogInterface;

/**
 * Created by Rumaan on 12-Feb-17.
 */

public interface PaymentChoiceListener {

    // When user chooses the payment mathod
    void onPositiveDialogClick(DialogInterface dialog, int choice);
}
