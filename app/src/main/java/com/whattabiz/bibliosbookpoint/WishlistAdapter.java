package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Rumaan on 18-09-2016.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private final Context mContext;

    public WishlistAdapter(Context context) {
        // Default Constructor
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.wishlist_listview, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bookName.setText(Store.WishList.get(position).getBookname());
        holder.bookPrice.setText(mContext.getResources().getString(R.string.rupee) + Store.WishList.get(position).getCost());
        Picasso.with(mContext)
                .load(Store.WishList.get(position).getbUrl())
                .error(R.drawable.book_placeholder)
                .placeholder(R.drawable.book_placeholder)
                .into(holder.bookImage);


        final CartWishModel modelObj = Store.WishList.get(position);
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if book already present in Cart
                if (Store.isBookAlreadyInCart(modelObj)) {
                    holder.addToCartBtn.setClickable(false);
                    Toast.makeText(mContext, modelObj.getBookname() + " is Already present in Cart!", Toast.LENGTH_SHORT).show();
                } else {
                     /*
                  * Add the current item to cart
                  * Reference using position ans send the CartWishModel Object
                 * */
                    // initialize number of items to 1
                    modelObj.setNumberOfItems(1);
                    // add the Object to Cart
                    Store.CartBook.add(modelObj);
                    Toast.makeText(mContext, Store.WishList.get(position).getBookname() + " has been added to Cart!", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(mContext, "Add to cart of " + position + " clicked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.WishList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Store.WishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView bookName;
        final TextView bookPrice;
        final ImageView bookImage;
        final Button addToCartBtn;
        final Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookImage = (ImageView) itemView.findViewById(R.id.book_img_view);
            bookName = (TextView) itemView.findViewById(R.id.bname_tv_wish);
            bookPrice = (TextView) itemView.findViewById(R.id.bcost_tv_wish);
            addToCartBtn = (Button) itemView.findViewById(R.id.cwishlist_add_to_cart);
            removeButton = (Button) itemView.findViewById(R.id.wishlist_remove_btn);
        }
    }
}
