package com.whattabiz.bibliosbookpoint;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {
    private final String TAG = "MyOrdersFragment";
    private RecyclerView recyclerView;
    private orderAdapter mAdapter;
    private ImageView emptyOrders;
    private ArrayList<CartWishModel> ordersList;
    private boolean isOrdersEmpty = true;

    public MyOrdersFragment() {
        // Required empty public constructor

        Bundle bundle = getArguments();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        StringRequest ordersRequest = new StringRequest(Request.Method.POST, Constants.MYORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Some Error Occurred. Please try again Later!", Toast.LENGTH_SHORT).show();
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

        /* Add the Request to Request Queue */
        MySingleton.getInstance(context).addToRequestQueue(ordersRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = Integer.parseInt(jsonObject.getString("status"));
            String message = jsonObject.getString("message");

            /* Display the response message */
            //     Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            if (status == 1) {
                isOrdersEmpty = false;

                // send the JSON Object for parsing orders
                new ParseTask().execute(jsonObject);
            } else {
                isOrdersEmpty = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emptyOrders = (ImageView) view.findViewById(R.id.empty_orders_img);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

    }

    /**
     * Parse the Orders Json Asynchronously
     * This is Completely USELESS
     * There are no other Views for UI thread to Inflate
     * This is Just a Test
     */
    class ParseTask extends AsyncTask<JSONObject, Void, ArrayList<CartWishModel>> {

        @Override
        protected ArrayList<CartWishModel> doInBackground(JSONObject... jsonObjects) {
            ArrayList<CartWishModel> list = new ArrayList<>();

            try {
                JSONArray jsonArray = jsonObjects[0].getJSONArray("orders");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject bookObj = jsonArray.getJSONObject(i);
                    CartWishModel cartWishModel = new CartWishModel();
                    cartWishModel.setBisbn(bookObj.getString("bisbn"));
                    cartWishModel.setBookname(bookObj.getString("name"));
                    cartWishModel.setCost(bookObj.getString("value"));
                    cartWishModel.setbUrl(bookObj.getString("path"));
                    cartWishModel.setOrderStatus(bookObj.getString("order_status"));

                    // add this book model object to the list
                    list.add(cartWishModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<CartWishModel> cartWishModels) {
            super.onPostExecute(cartWishModels);
            ordersList = new ArrayList<>(cartWishModels);
            mAdapter = new orderAdapter(getContext(), ordersList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setItemAnimator(new SlideInUpAnimator());
            recyclerView.setAdapter(mAdapter);

            emptyOrders.setVisibility(isOrdersEmpty ? View.VISIBLE : View.GONE);
        }
    }
}
