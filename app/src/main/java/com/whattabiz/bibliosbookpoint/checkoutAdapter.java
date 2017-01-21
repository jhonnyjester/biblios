package com.whattabiz.bibliosbookpoint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 8/4/2016.
 */

public class checkoutAdapter extends RecyclerView.Adapter<checkoutAdapter.MyViewHolder> {

    public checkoutAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.name.setText(Store.Orders.get(position).getBookname());
        int items = Store.Orders.get(position).getNumberOfItems();
        int price = Integer.valueOf(Store.Orders.get(position).getCost()) * items;
        holder.price.setText(String.valueOf(price));
    }

    @Override
    public int getItemCount() {
        return Store.Orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView price;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.msg);
            price = (TextView) view.findViewById(R.id.selling_price_tv);
        }
    }
}


