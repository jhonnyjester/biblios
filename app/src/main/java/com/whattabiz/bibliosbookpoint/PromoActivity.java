
package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromoActivity extends AppCompatActivity implements OnPromoCodeAppliedListener {
    private final String PROMO_URL = "http://bibliosworld.com/Biblios/androidpromo.php";

    private final String KEY = "PromoCodeActivity";
    private RecyclerView recyclerView;
    private PromoCodeAdapter mAdapter;
    private ProgressBar progressBar;

    private String tempTotal;

    private List<PromoCode> promoCodeList = new ArrayList<>();

    private String promoCodeAppliedId = null;

    private PromoCodeAdapter promoCodeAdapter;

    //private Type type = new TypeToken<List<PromoCode>>(){}.getType();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_select);
        setTitle("Promo Codes");

        progressBar = (ProgressBar) findViewById(R.id.prg_promo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        tempTotal = getIntent().getStringExtra(summary.TOTAL_AMOUNT_KEY);

        // set to visible initially
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        createPromoList();

        // Attach promo code listener to adapter
        promoCodeAdapter.setOnPromoCodeAppliedListener(this);
    }


    /**
     * Create a List of Promo Codes and Inflate via Recycler View
     * use LinearLayoutManager
     */
    private void createPromoList() {
        requestPromocodes();
        if (!promoCodeList.isEmpty() || promoCodeList != null) {
            promoCodeAdapter = new PromoCodeAdapter(promoCodeList, this);
            recyclerView.setAdapter(promoCodeAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "No Promo Codes Available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPromocodes() {
        StringRequest promoCodeRequest = new StringRequest(Request.Method.POST, Constants.PROMO_CODE_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(KEY, "Response: " + response);
                parseJson(response);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoActivity.this, "Some Error Occured, Please Try Again Later!", Toast.LENGTH_SHORT).show();
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

        /* Add the request to the queue */
        MySingleton.getInstance(this).addToRequestQueue(promoCodeRequest);
    }

    private void parseJson(String response) {
        /* Check if the String is empty or not */
        // Just in case if the JSON is empty
        if (response.equals("[{}]")) {
            Toast.makeText(this, "PromoCodes are not available!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        } else {
            /* Try to parse it into a JSON ARRAY of PromoCodes, this only happens if there are any promo codes */
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    PromoCode promoCode = new PromoCode();
                    promoCode.setId(jsonObj.getString("id"));
                    promoCode.setMsg(jsonObj.getString("msg"));
                    promoCode.setPercentage(jsonObj.getString("percentage"));
                    promoCodeList.add(promoCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Try to parse the JSON for no promo codes, status:0 */
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                // JUST in case
                if (status.contains("0")) {
                    Toast.makeText(this, "No Promo Codes available for you.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (status.contains("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("promo_codes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        PromoCode promoCode = new PromoCode();
                        promoCode.setId(jsonObj.getString("id"));
                        promoCode.setMsg(jsonObj.getString("msg"));
                        promoCode.setPercentage(jsonObj.getString("percentage"));
                        promoCodeList.add(promoCode);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    @Override
    public void OnPromoCodeApplied(String promoId, float percent) {

        //     Toast.makeText(PromoActivity.this, "PROMO ID: " + promoId + "\nPercent: " + String.valueOf(percent), Toast.LENGTH_SHORT).show();
        Intent data = new Intent();
        data.putExtra("promo_id", promoId);
        data.putExtra("percent", percent);
        setResult(RESULT_OK, data);
        finish();
    }
}