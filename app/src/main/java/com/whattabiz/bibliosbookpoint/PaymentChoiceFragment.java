package com.whattabiz.bibliosbookpoint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Rumaan on 12-Feb-17.
 */

public class PaymentChoiceFragment extends DialogFragment {
    String[] choiceList = {"Cash On Delivery (COD)", "Pay-U Money"};

    // set default choice to 0
    int choice;

    PaymentChoiceListener mPaymentChoiceListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mPaymentChoiceListener = (PaymentChoiceListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        String ordersJson = getArguments().getString(Constants.ORDERS_JSON_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle("Choose Payment Method")
                .setSingleChoiceItems(choiceList, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                }).setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPaymentChoiceListener.onPositiveDialogClick(dialog, choice);
                    }
                });

        return builder.create();
    }
}
