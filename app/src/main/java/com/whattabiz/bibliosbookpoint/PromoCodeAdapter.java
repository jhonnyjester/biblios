package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.MyViewHolder> {
    private final String KEY = "PromoAdapter";
    private List<PromoCode> promoCodeList;
    private Context mContext;
    private MaterialDialog materialDialog;

    PromoCodeAdapter(List<PromoCode> promoCodeList, Context context) {
        this.promoCodeList = promoCodeList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.promoName.setText(promoCodeList.get(position).getMsg());
        holder.promoDiscount.setText(promoCodeList.get(position).getPercentage());

        // hook up on click listeners
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPromoCode(holder.getAdapterPosition());
            }
        });
    }

    private void applyPromoCode(final int position) {
        materialDialog = new MaterialDialog.Builder(mContext)
                .content("Applying...")
                .progress(true, 0)
                .cancelable(false)
                .build();

        /* Show Progress Bar */
        materialDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PROMO_CODE_APPLY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(KEY, "Response: " + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int status = jsonObj.getInt("status");
                    if (status == 1) {
                        // Keep it Clean ;-)
                        float percent = Float.parseFloat(promoCodeList.get(position).getPercentage());
                        float decimalOfPercent = percent / 100;
                        float discountAmount = decimalOfPercent * Store.CURRENT_TOTAL;
                        Store.CURRENT_TOTAL = Store.CURRENT_TOTAL - discountAmount;

                        Toast.makeText(mContext, "PromoCode Applied Successfully!", Toast.LENGTH_SHORT).show();

                        // remove the Applied Promo Code
                        removeAt(position);
                    } else {
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }

                        Toast.makeText(mContext, "Could not apply this Promo Code!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }
                    Toast.makeText(mContext, "JSON EXception Occured!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", Constants.BIBLIOS_KEY);
                Log.i(KEY, "user_id: " + Store.user_id);
                Log.i(KEY, "promo_id: " + promoCodeList.get(position).getId());
                params.put("promo_id", promoCodeList.get(position).getId());
                params.put("user_id", Store.user_id);
                return params;
            }
        };

        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void removeAt(int pos) {
        promoCodeList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, promoCodeList.size());
    }


    @Override
    public int getItemCount() {
        return promoCodeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView promoName;
        TextView promoDiscount;
        LinearLayout rootView;

        MyViewHolder(View view) {
            super(view);
            promoName = (TextView) view.findViewById(R.id.promo_name);
            promoDiscount = (TextView) view.findViewById(R.id.promo_discount);
            rootView = (LinearLayout) view.findViewById(R.id.main_view);
        }
    }
}


