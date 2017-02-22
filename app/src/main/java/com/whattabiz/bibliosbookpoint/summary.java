package com.whattabiz.bibliosbookpoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class summary extends AppCompatActivity implements PaymentChoiceListener {

    public static final String TOTAL_AMOUNT_KEY = "TOTAL_AMOUNT";
    public static String[] ADDRESS = new String[3];
    private final String TAG = "summary Activity";
    private final String URL = "http://whattabiz.com/Biblios/androidorders.php";
    private final String KEY = "WhattabizBiblios";
    /* Promo Request Code */
    private final int PROMO_REQUEST_CODE = 13;
    private final String PROMO_CANCEL = "http://bibliosworld.com/Biblios/androidPromoCancel.php";
    private int sum;
    // TODO: Request Memership Percentage Discount
    private TextView total;

    private RecyclerView recyclerView;
    private checkoutAdapter mAdapter;
    private Button placeOrder, cancleOrder, promoCode;
    private boolean isOrderedOnce = false;
    private String ordersJson;
    private String DELIVERY_ADDRESS = "";
    /* Address Lines here */
    private TextInputLayout addressLine1, addressLine2, addressLine3;

    private String appliedPromoCodeId = null;

    /* Applied Promo Code Id */
    private String promoId;

    /* Promo Code Percentage */
    private float percent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // handle toolbar back button click
            cancelOrders();
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelOrders() {
        if (appliedPromoCodeId == null || appliedPromoCodeId.isEmpty())
            finish();
        else {
            // send that promo code wasnt applied
            StringRequest promoCancel = new StringRequest(Request.Method.POST, PROMO_CANCEL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(summary.this, "Some Error Occured!", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", Store.user_id);
                    params.put("promo_id", appliedPromoCodeId);
                    return params;
                }
            };

            MySingleton.getInstance(this).addToRequestQueue(promoCancel);

            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Summary");
        }
        this.setTitle("Summary");

        addressLine1 = (TextInputLayout) findViewById(R.id.address_line_1);
        addressLine2 = (TextInputLayout) findViewById(R.id.address_line_2);
        addressLine3 = (TextInputLayout) findViewById(R.id.address_line_3);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        total = (TextView) findViewById(R.id.total);
        placeOrder = (Button) findViewById(R.id.place_order);
        cancleOrder = (Button) findViewById(R.id.cancel_order);
        promoCode = (Button) findViewById(R.id.btn_promocode);
        total = (TextView) findViewById(R.id.total);

        mAdapter = new checkoutAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(summary.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // set total price
        sum = 0;
        for (int i = 0; i < Store.Orders.size(); i++) {
            int items = Store.Orders.get(i).getNumberOfItems();
            Log.d("items" + i, "" + items);
            int eachbookPrice = Integer.valueOf(Store.Orders.get(i).getCost());
            int eachRowPrice = items * eachbookPrice;
            sum += eachRowPrice;
        }
        total.setText(String.valueOf(sum));

        cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrders();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressLine1.getEditText().getText().toString().isEmpty()) {
                    addressLine1.setErrorEnabled(true);
                    addressLine1.setError("Enter the Address");
                    addressLine2.setErrorEnabled(false);
                    addressLine3.setErrorEnabled(false);
                } else if (addressLine2.getEditText().getText().toString().isEmpty()) {
                    addressLine2.setErrorEnabled(true);
                    addressLine2.setError("Enter the Address");
                    addressLine1.setErrorEnabled(false);
                    addressLine3.setErrorEnabled(false);
                } else if (addressLine3.getEditText().getText().toString().isEmpty()) {
                    addressLine3.setErrorEnabled(true);
                    addressLine3.setError("Enter the Pin Code");
                    addressLine1.setErrorEnabled(false);
                    addressLine2.setErrorEnabled(false);
                } else {
                    addressLine1.setErrorEnabled(false);
                    addressLine2.setErrorEnabled(false);
                    addressLine3.setErrorEnabled(false);
                    DELIVERY_ADDRESS = getAddress();

                    // Create the final Order json
                    makeJson();

                    // place the order
                    if (!isOrderedOnce) {
                        showDialog();
                    } else {
                        Toast.makeText(summary.this, "You have already placed this order!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        promoCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!promoCode.isClickable()) {
                    Toast.makeText(summary.this, "You have already applied a Promo Code!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        promoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /* Send the current Total price and calculate the discount amount from PromoCode codes and return back
                 * the Total amount and set it to the TotalAmount TextView */
                Intent promoIntent = new Intent(summary.this, PromoActivity.class);
                promoIntent.putExtra(TOTAL_AMOUNT_KEY, total.getText());
                //
                startActivityForResult(promoIntent, PROMO_REQUEST_CODE);
            }
        });
    }

    private void cancelAppliedPromoCode(final String promoId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROMO_CANCEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("PROMOCANCEL", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(summary.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("promo_id", promoId);
                params.put("user_id", Store.user_id);
                return params;
            }
        };

        // add the request to queue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Check if the activity was PromoCodeActivity */
        if (requestCode == PROMO_REQUEST_CODE) {
            /* If Activity called was successfully returned */
            if (resultCode == RESULT_OK) {
                /* Get the promo data */
                promoId = data.getStringExtra("promo_id");
                percent = data.getFloatExtra("percent", (float) 1);

                Log.i(TAG, "PromoDetails: " + "" +
                        "\nId: " + promoId + "\n" + "Percent: " + percent);

                /* set the promo code button clickable false */
                promoCode.setClickable(false);

                applyPromoCode();
            }
        }
    }

    /**
     * Calculate the total checkout cost after applying promo code
     */
    private void applyPromoCode() {
        float decimalOfPercent = percent / 100;
        float discountAmount = decimalOfPercent * sum;
        sum = (int) (sum - discountAmount);
        Log.d(TAG, "SUM after promo code apply: " + sum);
        total.setText(sum + "*");
    }

    // display a dialog of choices
    private void showDialog() {
        PaymentChoiceFragment paymentChoiceFragment = new PaymentChoiceFragment();
        paymentChoiceFragment.show(getSupportFragmentManager(), "PaymentChoiceFragment");
    }

    // Redirect the payment to Pay-U money webview
    private void redirectToPayU() {
        Intent intent = new Intent(this, PaymentGatewayActivity.class);
        intent.putExtra("PRODUCT_INFO", makeProductInfoJson());
        intent.putExtra("TOTAL_AMOUNT", String.valueOf(sum));
        startActivity(intent);
    }

    // send the JSON directly to biblios server
    private void redirectToBibliosServer() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .cancelable(false)
                .progress(true, 0)
                .content("Placing your order. Please Wait!");
        final MaterialDialog dialog = builder.build();

        dialog.show();

        StringRequest stringRequest = new
                StringRequest(Request.Method.POST, Constants.ORDERS_BIBLIOS_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        Toast.makeText(summary.this, "You order will be placed soon..", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("key", Constants.BIBLIOS_KEY);
                        params.put("user_id", Store.user_id);
                        params.put("orders", ordersJson);
                        params.put("total", String.valueOf(sum));
                        return params;
                    }
                };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /*
    * Send the orders as a json array to backend
    * */
    public void sendTheOrders() {
        Log.d("StringJson", ordersJson);
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this)
                .content("Placing your Order...")
                .cancelable(false)
                .progress(true, 0);
        final MaterialDialog materialDialog = dialog.build();
        materialDialog.show();

        Log.i("User_id", Store.user_id);
        Log.i("Order url", URL + "?user_id=" + Store.user_id);
        StringRequest request = new StringRequest(Request.Method.POST, URL + "?user_id=" + Store.user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isOrderedOnce = true;

                // add the book object to placed orders
                Store.placedOrders.addAll(Store.Orders);
                Log.d("Response", response);
                materialDialog.dismiss();
                if (response.contains("successfully")) {
                    summary.this.finish();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), NavigationHomeActivity.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                materialDialog.dismiss();
                Log.e("Error Response", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // SHREYAS SEE HERE
                params.put("key", KEY);
                // orders json string here
                params.put("orders", ordersJson);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    /*
    * Form a JSON of books that are in summary page -> Store.Orders
    * */
    private void makeJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < Store.Orders.size(); i++) {
                JSONObject object = new JSONObject();
                object.put("bookname", Store.Orders.get(i).getBookname());
                object.put("bookprice", Store.Orders.get(i).getCost());
                object.put("bisbn", Store.Orders.get(i).getBisbn());
                jsonArray.put(object);
            }

            // Add the orders to the json object
            // { "orders": ""}
            jsonObject.put("orders", jsonArray);

            // Add the total sum
            // { "total_price" : "566"}
            jsonObject.put("total_price", String.valueOf(sum));

            // Append Address to the JSON
            // { "orders" : "", "address" : ""}
            jsonObject.put("address", getAddress());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ordersJson = jsonObject.toString();
        Log.v("summary.java", "Json Order: " + ordersJson);
    }

    // Get the address lines from editText
    // Delimit each address by a ',' comma
    // Return the final Address
    private String getAddress() {
        return addressLine1.getEditText().getText().toString() + "," +
                addressLine2.getEditText().getText().toString() + "," +
                addressLine3.getEditText().getText().toString();
    }

    private String makeProductInfoJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < Store.Orders.size(); i++) {
                JSONObject object = new JSONObject();

                object.put("name", Store.Orders.get(i).getBookname());
                object.put("description", Store.Orders.get(i).getBisbn());
                object.put("value", Store.Orders.get(i).getCost());
                object.put("isRequired", "true");
                object.put("settlementEvent", "EmailConfirmation");

                jsonArray.put(object);
            }

            jsonObject.put("paymentParts", jsonArray);

            JSONArray idArray = new JSONArray();
            //current date
            Calendar calendar = Calendar.getInstance();
            Date localTime = calendar.getTime();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String DATE = format.format(localTime);

            JSONObject object1 = new JSONObject();
            object1.put("field", "CompletionDate");
            object1.put("value", DATE);
            idArray.put(object1);

            //transaction id to our server
            String TIMESTAMP = new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime());
            String txnid = "TXN" + Store.user_id + TIMESTAMP;

            JSONObject object2 = new JSONObject();
            object2.put("field", "txnid");
            object2.put("value", txnid);
            idArray.put(object2);

            JSONObject object3 = new JSONObject();
            object3.put("field", "user_id");
            object3.put("value", Store.user_id);
            idArray.put(object3);

            JSONObject object4 = new JSONObject();
            object4.put("field", "address");
            object4.put("value", DELIVERY_ADDRESS);
            idArray.put(object4);

            jsonObject.put("paymentIdentifiers", idArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ProductInfoJSON", jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void onBackPressed() {
        cancelOrders();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * When user chooses the Payment Method for the orders
     */
    @Override
    public void onPositiveDialogClick(DialogInterface dialog, int choice) {
        switch (choice) {
            case 0:
                // COD
                redirectToBibliosServer();
                break;
            case 1:
                // Pay - u
                redirectToPayU();
                break;
        }
    }
}
