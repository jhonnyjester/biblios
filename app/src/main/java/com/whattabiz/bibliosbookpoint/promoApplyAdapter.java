package com.whattabiz.bibliosbookpoint;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 8/4/2016.
 */

public class promoApplyAdapter extends RecyclerView.Adapter<promoApplyAdapter.MyViewHolder> {
    private final List<promoApply> promoApplyList;


    public promoApplyAdapter(List<promoApply> promoApplyList) {
        this.promoApplyList = promoApplyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_apply_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        promoApply view = promoApplyList.get(position);
        holder.discount.setText(view.getDiscount());
        holder.name.setText(view.getName());
        holder.detail.setText(view.getDetail());
        holder.promoSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto PromoCode apply activity
                // Toast.makeText(view.getContext(), "Clicked Here : " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "What Next? :-(", Snackbar.LENGTH_INDEFINITE).show();
                //  view.getContext().startActivity(new Intent(view.getContext(), PromoActivity.class));
            }
        });

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return promoApplyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView discount;
        final TextView name;
        final TextView detail;
        final LinearLayout main_view;
        final Button promoSelectButton;


        public MyViewHolder(View view) {
            super(view);
            discount = (TextView) view.findViewById(R.id.promo_discount);
            name = (TextView) view.findViewById(R.id.promo_name);
            detail = (TextView) view.findViewById(R.id.promo_detail);
            main_view = (LinearLayout) view.findViewById(R.id.main_view);
            promoSelectButton = (Button) view.findViewById(R.id.apply_promo_button);
        }
    }
}


