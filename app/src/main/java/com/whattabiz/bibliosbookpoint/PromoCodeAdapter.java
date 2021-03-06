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
    private final float currentTotal;
    private List<PromoCode> promoCodeList;
    private Context mContext;
    private MaterialDialog materialDialog;

    //  private ArrayList<String> appliedPromoCodes = new ArrayList<>();
    /* Instance on OnPromoCodeAppliedListener */
    private OnPromoCodeAppliedListener onPromoCodeAppliedListener;

    PromoCodeAdapter(List<PromoCode> promoCodeList, Context context, float currentTotal) {
        this.promoCodeList = promoCodeList;
        this.mContext = context;
        this.currentTotal = currentTotal;
    }

    /* Setter for PromoCode Applied Listener */
    public void setOnPromoCodeAppliedListener(OnPromoCodeAppliedListener listener) {
        this.onPromoCodeAppliedListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.promoName.setText(promoCodeList.get(position).getMsg());
        holder.promoDiscount.setText(promoCodeList.get(position).getPercentage());

        // hook up on click listeners
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PromoCodeAdapter", "CurrentTotal: " + currentTotal + "\n" +
                        "Limit: " + promoCodeList.get(position).getLimit());
                /* Check for limit before applying */
                /* If the Current Order Total is greater than limit ==> apply the promo code */
                if (currentTotal >= promoCodeList.get(position).getLimit())
                    applyPromoCode(holder.getAdapterPosition());
                else
                    Toast.makeText(mContext, "Sorry! You cannot apply this Promo Code.", Toast.LENGTH_SHORT).show();
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
                       /* float decimalOfPercent = percent / 100;
                        float discountAmount = decimalOfPercent * Store.CURRENT_TOTAL;
                        Store.CURRENT_TOTAL = Store.CURRENT_TOTAL - discountAmount;
*/
                        Store.isPromoCodeApplied = true;
                        Store.promoData.putExtra("promo_id", promoCodeList.get(position).getId());

                        Toast.makeText(mContext, "PromoCode Applied Successfully!", Toast.LENGTH_SHORT).show();

                        // Invoke this Callback
                        if (onPromoCodeAppliedListener != null) {
                            onPromoCodeAppliedListener.OnPromoCodeApplied(promoCodeList.get(position).getId(), percent);
                        }

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

        if (materialDialog.isShowing()) materialDialog.dismiss();
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