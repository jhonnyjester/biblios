package com.whattabiz.bibliosbookpoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentGatewayActivity extends AppCompatActivity {

    final String BIB_URL = "http://www.bibliosworld.com/Biblios/bibliosPayment.php";
    WebView wv;
    Context context = PaymentGatewayActivity.this;

    private static String bytesToHexString(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle("Payment Gateway");

        wv = (WebView) findViewById(R.id.payment_web_view);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);


        //original url
        String url = "https://secure.payu.in/_payment";


        //test url
        //String url = "https://test.payu.in/_payment";

        wv.postUrl(url, EncodingUtils.getBytes(getPostString(), "BASE64"));

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);


        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();

                if (url.contains("success") || url.contains("Success")) {
                    Toast.makeText(PaymentGatewayActivity.this, "success", Toast.LENGTH_SHORT).show();
                    showSuccess();
                }
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                pd.dismiss();
                Log.e("Error", "Exception caught!");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private String getPostString() {
        //original key and salt
        String key = "FRZFIS9J";
        String salt = "tBwj02IxRx";

        String time = new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime());

        String txnid = "TXN" + Store.user_id + time;

        String amount = getIntent().getExtras().getString("TOTAL_AMOUNT");
        Log.d("AMOUNT", amount);

        SharedPreferences sharedPreferences = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
        String firstname = sharedPreferences.getString("Name", "");
        String email = sharedPreferences.getString("Email", "");
        String phone = sharedPreferences.getString("phone", "");

//        String firstname = "jonathan";
//        String email = "rodriguesjnyadr@gmail.com";
//        String phone = "8762367588";
        String surl = "http://bibliosworld.com/payumoney/success.php";
        String furl = "http://bibliosworld.com/payumoney/failure.php";

//        String surl = "http://legall.co.in/payumoney/success.php";
//        String furl = "http://legall.co.in/payumoney/failure.php";
        String productInfo = getIntent().getExtras().getString("PRODUCT_INFO");
        Log.e("PRODUCT INFO", productInfo);

        StringBuilder post = new StringBuilder();

        post.append("key=");
        post.append(key);
        post.append("&");

        post.append("txnid=");
        post.append(txnid);
        post.append("&");

        post.append("amount=");
        post.append(amount);
        post.append("&");

        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");

        post.append("firstname=");
        post.append(firstname);
        post.append("&");

        post.append("email=");
        post.append(email);
        post.append("&");

        post.append("phone=");
        post.append(phone);
        post.append("&");

        post.append("surl=");
        post.append(surl);
        post.append("&");

        post.append("furl=");
        post.append(furl);
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();

        MessageDigest digest;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");

            checkSumStr.append(key);
            checkSumStr.append("|");

            checkSumStr.append(txnid);
            checkSumStr.append("|");

            checkSumStr.append(amount);
            checkSumStr.append("|");

            checkSumStr.append(productInfo);
            checkSumStr.append("|");

            checkSumStr.append(firstname);
            checkSumStr.append("|");

            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            /*checkSumStr.append("|");

            // udf1
            checkSumStr.append(addressLine1);
            checkSumStr.append("|");

            // udf2
            checkSumStr.append(addressLine2);
            checkSumStr.append("|");

            // udf3
            checkSumStr.append(addressLine3);
            //  checkSumStr.append("|");

            checkSumStr.append("||||||||");*/

            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");

        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace();
        }

        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showPrompt();
                break;

        }
        return true;
    }

    public void showPrompt() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentGatewayActivity.this);
        dialog.setCancelable(false)
                .setMessage("Do you want to cancel the transaction ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("NO", null).show();
    }

    public void showSuccess() {

        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_registration_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView title = (TextView) dialog.findViewById(R.id.title);
        if (title != null) {
            title.setText("Transaction Success");
        }
        TextView caption = (TextView) dialog.findViewById(R.id.caption);
        if (caption != null) {
            caption.setText("We've recieved your payment. Thank You!");
        }


        Button back = (Button) dialog.findViewById(R.id.btn_back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    finish();
                    return;
                }


            });
        }

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showPrompt();
    }
}