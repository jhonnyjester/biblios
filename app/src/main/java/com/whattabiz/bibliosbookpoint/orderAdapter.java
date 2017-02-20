package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 8/4/2016.
 */

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.MyViewHolder> {
    private final Context mContext;
    ArrayList<CartWishModel> cartWishModelArrayList;

    public orderAdapter(Context context, ArrayList<CartWishModel> cartWishModelArrayList) {
        this.mContext = context;
        this.cartWishModelArrayList = new ArrayList<>(cartWishModelArrayList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /* Set the Book image */
        Picasso.with(mContext).load("http://bibliosworld.com/Biblios/" + cartWishModelArrayList.get(holder.getAdapterPosition()).getbUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(holder.book_img);

        /* Set the Book name */
        holder.name.setText(cartWishModelArrayList.get(holder.getAdapterPosition()).getBookname());

        /* Set the Delivery status */
        holder.deliveryStatus.setText(cartWishModelArrayList.get(holder.getAdapterPosition()).getOrderStatus() == 1 ? "Delivered" : "Order Placed");

        /* Set the Price */
        holder.price.setText(cartWishModelArrayList.get(holder.getAdapterPosition()).getCost());
    }

    @Override
    public int getItemCount() {
        return cartWishModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        ImageView book_img;
        TextView deliveryStatus;
        LinearLayout main_view;

        MyViewHolder(View view) {
            super(view);
            book_img = (ImageView) view.findViewById(R.id.book_img);
            name = (TextView) view.findViewById(R.id.book_name);
            price = (TextView) view.findViewById(R.id.selling_price_tv);
            main_view = (LinearLayout) view.findViewById(R.id.main_view);
            deliveryStatus = (TextView) view.findViewById(R.id.availablity);
        }
    }
}


