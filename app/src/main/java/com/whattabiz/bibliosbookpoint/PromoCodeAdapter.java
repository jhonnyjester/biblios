package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.MyViewHolder> {
    private List<PromoCode> promoCodeList;
    private Context mContext;

    public PromoCodeAdapter(List<PromoCode> promoCodeList, Context context) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.promoName.setText(promoCodeList.get(position).getName());
        holder.promoDiscount.setText(promoCodeList.get(position).getDiscount());
    }

    @Override
    public int getItemCount() {
        return promoCodeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView promoName;
        TextView promoDiscount;

        MyViewHolder(View view) {
            super(view);

            promoName = (TextView) view.findViewById(R.id.promo_name);
            promoDiscount = (TextView) view.findViewById(R.id.promo_discount);
        }
    }
}


