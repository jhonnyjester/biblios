package com.whattabiz.bibliosbookpoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rumaan on 18-09-2016.
 */

public class CategoriesListItemAdapter extends RecyclerView.Adapter<CategoriesListItemAdapter.ViewHolder> {
    private final Context context;
    private final String KEY;
    private ArrayList<CartWishModel> cartWishModels = new ArrayList<>();

    public CategoriesListItemAdapter(Context context, String key) {
        this.context = context;
        this.KEY = key;
        // Log.d("Key", key);
        cartWishModels = Store.bookCategories.get(key);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.category_list_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bookName.setText(cartWishModels.get(position).getBookname());
        holder.bookPrice.setText(context.getResources().getString(R.string.rupee) + cartWishModels.get(position).getCost());
        Picasso.with(context)
                .load(cartWishModels.get(position).getbUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(holder.bookImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductView.class);
                intent.putExtra("BookName", holder.bookName.getText());
                intent.putExtra("BookImageUrl", cartWishModels.get(position).getbUrl());
                intent.putExtra("BookSellingPrice", cartWishModels.get(position).getCost());
                intent.putExtra("BookMrpPrice", cartWishModels.get(position).getMrp());
                intent.putExtra("BookBisbn", cartWishModels.get(position).getBisbn());

                /* Add Shared Element Transition if API level is above lollipop */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) context, (View) holder.bookImage, context.getString(R.string.ProductImageTransition));
                    context.startActivity(intent, optionsCompat.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartWishModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView bookName;
        final TextView bookPrice;
        final ImageView bookImage;
        final CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            bookImage = (ImageView) itemView.findViewById(R.id.category_item_image);
            bookName = (TextView) itemView.findViewById(R.id.category_item_name);
            bookPrice = (TextView) itemView.findViewById(R.id.category_item_price);
            cardView = (CardView) itemView.findViewById(R.id.category_card_item);
        }
    }
}
