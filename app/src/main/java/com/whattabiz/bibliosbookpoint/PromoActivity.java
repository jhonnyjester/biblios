package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class PromoActivity extends AppCompatActivity {
    private final String PROMO_URL = "http://bibliosworld.com/Biblios/androidpromo.php";
    private RecyclerView recyclerView;
    private promoAdapter mAdapter;
    private ProgressBar progressBar;

    private String tempTotal;

    @Override
    protected void onStart() {
        super.onStart();
        tempTotal = getIntent().getStringExtra(summary.TOTAL_AMOUNT_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_select);
        setTitle("Promo Codes");

        progressBar = (ProgressBar) findViewById(R.id.prg_promo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        // if promo codes is empty get the promo codes
        if (Store.PromoCodes.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(GONE);
            getThePromocodes();
            instantiateList();
        } else {
            instantiateList();
            progressBar.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void instantiateList() {
        mAdapter = new promoAdapter(Store.PromoCodes, this, true, Float.valueOf(tempTotal));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void getThePromocodes() {
        // get the promo codes from server
        StringRequest promoRequest = new StringRequest(Request.Method.POST, PROMO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                addToList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoActivity.this, "Some Error Occurred! Please Try Again Later.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", Constants.BIBLIOS_KEY);
                params.put("user_id", Store.user_id);
                return params;
            }
        };

        promoRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(promoRequest);
    }

    private void addToList(String response) {
        // Parse the JSON
        // append the data to List
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String msg = jsonObject.getString("msg");
                String percentage = jsonObject.getString("percentage");
                Store.PromoCodes.add(new promo(percentage, msg));

                progressBar.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PromoActivity.this.finish();
    }
}

