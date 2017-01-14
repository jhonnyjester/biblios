package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

/**
 * Created by User on 8/4/2016.
 */

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.MyViewHolder> {
    private final Context mContext;

    public orderAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(Store.placedOrders.get(position).getBookname());
        float price = Integer.parseInt(Store.placedOrders.get(position).getCost()) * Store.placedOrders.get(position).getNumberOfItems();
        holder.price.setText(mContext.getResources().getString(R.string.rupee) + price);
        Picasso.with(mContext)
                .load(Store.placedOrders.get(position).getbUrl())
                .error(R.drawable.book_placeholder)
                .placeholder(R.drawable.book_placeholder)
                .into(holder.book_img);
        holder.numOfItems.setText(String.valueOf(Store.placedOrders.get(position).getNumberOfItems()));
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel the order
                // request the alert dialog
                new MaterialDialog.Builder(mContext)
                        .content("Are you sure you want to cancel this Order?")
                        .positiveText("Yes, Cancel the Order")
                        .negativeText("No, Don't Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // cancel order
                                cancelOrder(position);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // dismiss dialog
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Store.placedOrders.size();
    }

    private void cancelOrder(final int position) {
        String url = "http://bibliosworld.com/Biblios/androidcancel.php";
        String bname = Store.placedOrders.get(position).getBookname();
        bname = bname.replaceAll(" ", "%20");
        String bisbn = Store.placedOrders.get(position).getBisbn();
        bisbn = bisbn.replaceAll(" ", "%20");
        //TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String user_id = Store.user_id;
        String key = "WhattabizBiblios";

        url = url + "?&bname=" + bname + "&bisbn=" + bisbn + "&user_id=" + user_id + "&key=" + key;
        Log.i("Url Cancel", url);

        StringRequest cancelOrderReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response of Cancel", response);
                // if response was successfull remove that order pos elem
                if (response.contains("successful")) {
                    // remove that order
                    Store.placedOrders.remove(position);
                    Toast.makeText(mContext, "Your order has been Canceled!", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, Store.placedOrders.size());
                } else {
                    Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(mContext).addToRequestQueue(cancelOrderReq);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView numOfItems;
        final TextView price;
        final ImageView book_img;
        final LinearLayout main_view;
        final Button cancelButton;

        public MyViewHolder(View view) {
            super(view);
            book_img = (ImageView) view.findViewById(R.id.book_img);
            name = (TextView) view.findViewById(R.id.book_name);
            numOfItems = (TextView) view.findViewById(R.id.num_items);
            price = (TextView) view.findViewById(R.id.selling_price_tv);
            main_view = (LinearLayout) view.findViewById(R.id.main_view);
            cancelButton = (Button) itemView.findViewById(R.id.cancel_btn);
        }
    }
}


