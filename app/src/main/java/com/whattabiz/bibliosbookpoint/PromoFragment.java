package com.whattabiz.bibliosbookpoint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;


/**
 * Shows PromoCode for User
 */
public class PromoFragment extends Fragment {

    private final String PROMO_URL = "http://bibliosworld.com/Biblios/androidpromo.php";
    private final List<PromoCode> promoCodeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PromoCodeAdapter mAdapter;
    private RelativeLayout promoRelativeLayout;
    private ProgressBar progressBar;


    public PromoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getActivity().setTitle("Promo Codes");

        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        /* Set it Initially */
        progressBar.setVisibility(View.VISIBLE);

        // To get promoCode from the server
        buildPromo();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        promoRelativeLayout = (RelativeLayout) view.findViewById(R.id.promo_relativeLay);
    }

    private void addToList(String response) {
        Log.e("PROMO CODE RESPONSE", response);
        // Parse the JSON
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String msg = jsonObject.getString("msg");
                String percentage = jsonObject.getString("percentage");
                promoCodeList.add(new PromoCode(percentage, msg));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // after all this Add the list items to the recycler
        inflateList();
    }

    private void inflateList() {
        // append the list details to the RecyclerView
        mAdapter = new PromoCodeAdapter(promoCodeList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        promoRelativeLayout.setVisibility(View.GONE);
    }

    private void buildPromo() {
        StringRequest promoRequest = new StringRequest(Request.Method.POST, PROMO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addToList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Some Error Occurred! Please Try Again Later.", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(promoRequest);
    }
}
