package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Rumaan on 10-10-2016.
 */

public class CardBannerAdapter extends RecyclerView.Adapter<CardBannerAdapter.ViewHolder> {

    private Context mContext;

    public CardBannerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookName.setText(Store.cardBannerItems.get(position).getBookname());
        holder.bookMPrice.setText(Store.cardBannerItems.get(position).getMrp());
        holder.bookSprice.setText(Store.cardBannerItems.get(position).getCost());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Picasso.with(mContext)
                .load(Store.cardBannerItems.get(position).getbUrl())
                .error(R.drawable.book_placeholder)
                .placeholder(R.drawable.book_placeholder)
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return Store.cardBannerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView bookImage;
        TextView bookName;
        TextView bookSprice;
        TextView bookMPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.book_card);
            bookImage = (ImageView) itemView.findViewById(R.id.book_image_card);
            bookMPrice = (TextView) itemView.findViewById(R.id.book_card_mprice);
            bookSprice = (TextView) itemView.findViewById(R.id.book_card_sprice);
            bookName = (TextView) itemView.findViewById(R.id.book_name_card);
        }
    }
}
