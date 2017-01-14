package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 8/4/2016.
 */

public class promoAdapter extends RecyclerView.Adapter<promoAdapter.MyViewHolder> {
    private List<promo> promoList;
    private Context mContext;
    private boolean isClickEnabled = false;
    private float total;

    public promoAdapter(List<promo> promoList, Context context) {
        this.promoList = promoList;
        this.mContext = context;
    }

    public promoAdapter(List<promo> promoList, Context context, boolean isClickEnabled, float total) {
        this.promoList = promoList;
        this.mContext = context;
        this.isClickEnabled = isClickEnabled;
        this.total = total;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        promo view = promoList.get(position);
        holder.discount.setText(view.getDiscount());
        holder.name.setText(view.getName());

        if (isClickEnabled) {
            holder.main_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView discount;
        public final TextView name;
        final LinearLayout main_view;

        public MyViewHolder(View view) {
            super(view);
            discount = (TextView) view.findViewById(R.id.promo_discount);
            name = (TextView) view.findViewById(R.id.promo_name);
            main_view = (LinearLayout) view.findViewById(R.id.main_view);
        }
    }
}


