package com.whattabiz.bibliosbookpoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rumaan on 25-09-2016.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<CartWishModel> searchResults = new ArrayList<>();

    public SearchResultsAdapter(Context context, ArrayList<CartWishModel> searchResults) {
        this.mContext = context;
        this.searchResults = searchResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.category_list_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(searchResults.get(position).getbUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(holder.bookImg);
        holder.bookCost.setText(mContext.getResources().getString(R.string.rupee) + searchResults.get(position).getCost());
        holder.bookName.setText(searchResults.get(position).getBookname());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductView.class);
                intent.putExtra("BookName", holder.bookName.getText());
                intent.putExtra("BookImageUrl", searchResults.get(position).getbUrl());
                Log.d("Image Url", searchResults.get(position).getbUrl());
                intent.putExtra("BookSellingPrice", searchResults.get(position).getCost());
                intent.putExtra("BookMrpPrice", searchResults.get(position).getMrp());
                intent.putExtra("BookBisbn", searchResults.get(position).getBisbn());

                // Add Shared Element Transition if API is above LOLLIPOP
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) mContext, (View) holder.bookImg, mContext.getString(R.string.ProductImageTransition));
                    mContext.startActivity(intent, optionsCompat.toBundle());
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final ImageView bookImg;
        final TextView bookName;
        final TextView bookCost;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.category_card_item);
            bookImg = (ImageView) itemView.findViewById(R.id.category_item_image);
            bookName = (TextView) itemView.findViewById(R.id.category_item_name);
            bookCost = (TextView) itemView.findViewById(R.id.category_item_price);
        }
    }
}
