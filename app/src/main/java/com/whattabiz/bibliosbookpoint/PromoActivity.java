package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Objects;

public class PromoActivity extends AppCompatActivity {
    private final String PROMO_URL = "http://bibliosworld.com/Biblios/androidpromo.php";

    private final String KEY = "PromoCodeActivity";
    private RecyclerView recyclerView;
    private PromoCodeAdapter mAdapter;
    private ProgressBar progressBar;

    private String tempTotal;

    private List<PromoCode> promoCodeList = new ArrayList<>();

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

        /*// if PromoCode codes is empty get the PromoCode codes
        if (Store.PROMO_CODE_CODEs.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(GONE);
            getThePromocodes();
            instantiateList();
        } else {
            instantiateList();
            progressBar.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }*/

        createPromoList();

    }

    /**
     * Create a List of Promo Codes and Inflate via Recycler View
     * use LinearLayoutManager
     *
     */
    private void createPromoList() {
        requestPromocodes();
        if (!promoCodeList.isEmpty() || promoCodeList != null) {
            recyclerView.setAdapter(new PromoCodeAdapter(promoCodeList, this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void requestPromocodes() {
        StringRequest promoCodeRequest = new StringRequest(Request.Method.POST, Constants.PROMO_CODE_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(KEY, "Response: " + response);
                if (Objects.equals(response, "[{}]") || response.contains("id")) {
                    Toast.makeText(PromoActivity.this, "No PromoCodes Available at the moment!", Toast.LENGTH_SHORT).show();
                } else {
                    parseJson(response);
                }
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

        MySingleton.getInstance(this).addToRequestQueue(promoCodeRequest);
    }

    private void parseJson(String response) {
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PromoActivity.this.finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PromoActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }
}

