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

/**
 * Custom Adapter for Recycler Items in the HomeFragment
 * Created by Rumaan on 12-Jul-16.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final Context mContext;
    // whichBooks ->
    // 0 - top books
    // 1 -> recent books
    // 2 -> suggested books
    private final int whichBooks;
    private final String baseUrl = "http://bibliosworld.com/Biblios/";

    public RecyclerAdapter(Context context, int whichBooks) {
        this.mContext = context;
        this.whichBooks = whichBooks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (whichBooks == 0) {
            // its top books
            Picasso.with(getContext())
                    .load(baseUrl + Store.topBooks.get(position).getbUrl())
                    .resize(295, 400)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .into(holder.imageView);

            //imageView.setImageResource(model.getImageId());
            holder.hTextView.setText(Store.topBooks.get(position).getBookname());

            holder.price.setText(mContext.getResources().getString(R.string.rupee) + Store.topBooks.get(position).getCost());

            // set onClickListener to CardView
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProductView.class);
                    Log.d("BookName", holder.hTextView.getText().toString());
                    intent.putExtra("BookName", holder.hTextView.getText());
                    intent.putExtra("BookImageUrl", baseUrl + Store.topBooks.get(position).getbUrl());
                    intent.putExtra("BookSellingPrice", Store.topBooks.get(position).getCost());
                    intent.putExtra("BookMrpPrice", Store.topBooks.get(position).getMrp());
                    intent.putExtra("BookBisbn", Store.topBooks.get(position).getBisbn());

                     /* Add Shared Element Transition if Api is above LOLLIPOP */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                                .makeSceneTransitionAnimation((Activity) getContext(), (View) holder.imageView, getContext().getString(R.string.ProductImageTransition));
                        getContext().startActivity(intent, activityOptionsCompat.toBundle());
                    } else {
                        getContext().startActivity(intent);
                    }
                }
            });
        } else if (whichBooks == 1) {
            // its Recent Books
            Picasso.with(getContext())
                    .load(baseUrl + Store.recentBooks.get(position).getbUrl())
                    .resize(290, 400)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .into(holder.imageView);


            holder.hTextView.setText(Store.recentBooks.get(position).getBookname());

            holder.price.setText(mContext.getResources().getString(R.string.rupee) + Store.recentBooks.get(position).getCost());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProductView.class);
                    Log.d("BookName", holder.hTextView.getText().toString());
                    intent.putExtra("BookName", holder.hTextView.getText());
                    intent.putExtra("BookImageUrl", baseUrl + Store.recentBooks.get(position).getbUrl());
                    intent.putExtra("BookSellingPrice", Store.recentBooks.get(position).getCost());
                    intent.putExtra("BookMrpPrice", Store.recentBooks.get(position).getMrp());
                    intent.putExtra("BookBisbn", Store.recentBooks.get(position).getBisbn());

                    /* Add Shared Element Transition if Api is above LOLLIPOP */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                                .makeSceneTransitionAnimation((Activity) getContext(), (View) holder.imageView, getContext().getString(R.string.ProductImageTransition));
                        getContext().startActivity(intent, activityOptionsCompat.toBundle());
                    } else {
                        getContext().startActivity(intent);
                    }
                }
            });
        } else if (whichBooks == 2) {

            // its Suggested Books
            // FIXME: 31-10-2016 data mining for suggested books
            //Collections.shuffle(Store.suggestedBooks);
            Picasso.with(getContext())
                    .load(baseUrl + Store.suggestedBooks.get(position).getbUrl())
                    .resize(290, 400)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .into(holder.imageView);

            holder.hTextView.setText(Store.suggestedBooks.get(position).getBookname());

            holder.price.setText(mContext.getResources().getString(R.string.rupee) + Store.suggestedBooks.get(position).getCost());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProductView.class);
                    Log.d("BookName", holder.hTextView.getText().toString());
                    intent.putExtra("BookName", holder.hTextView.getText());
                    intent.putExtra("BookImageUrl", baseUrl + Store.suggestedBooks.get(position).getbUrl());
                    intent.putExtra("BookSellingPrice", Store.suggestedBooks.get(position).getCost());
                    intent.putExtra("BookMrpPrice", Store.suggestedBooks.get(position).getMrp());
                    intent.putExtra("BookBisbn", Store.suggestedBooks.get(position).getBisbn());

                    /* Add Shared Element Transition if Api is above LOLLIPOP */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                                .makeSceneTransitionAnimation((Activity) getContext(), (View) holder.imageView, getContext().getString(R.string.ProductImageTransition));
                        getContext().startActivity(intent, activityOptionsCompat.toBundle());
                    } else {
                        getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        /* Check if TopBooks or RecentBooks */
        // 0 -> TopBooks
        // 1 -> Recent Books
        // 3 -> suggested books
        switch (whichBooks) {
            case 0:
                return Store.topBooks.size();
            case 1:
                return Store.recentBooks.size();
        }
        return Store.suggestedBooks.size();
    }

    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView price;
        final TextView hTextView;
        final CardView card;
        private final Context context;

        public ViewHolder(Context context, final View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.whattabiz_logo);
            this.hTextView = (TextView) itemView.findViewById(R.id.list_header);
            this.price = (TextView) itemView.findViewById(R.id.list_item_price);
            this.card = (CardView) itemView.findViewById(R.id.card);
            this.context = context;
        }

    }
}