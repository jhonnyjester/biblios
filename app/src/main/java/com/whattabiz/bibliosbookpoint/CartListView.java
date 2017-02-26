package com.whattabiz.bibliosbookpoint;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartListView extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private static ArrayList<CartWishModel> book_details;
    private final Context context;


    public CartListView(CartActivity cartMainActivity, ArrayList<CartWishModel> book_details) {
        CartListView.book_details = book_details;
        context = cartMainActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return book_details.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Integer cost = Integer.valueOf(book_details.get(position).getCost());
        int totalCost = cost * book_details.get(position).getNumberOfItems();
        String rupee = context.getResources().getString(R.string.rupee);
        String tCost = book_details.get(position).getNumberOfItems() + " x " + rupee + cost + " = " + rupee + totalCost;

        Holder holder = new Holder();
        View v;

        v = inflater.inflate(R.layout.cart_list_view, null);
//        Store.getCartBook().get(position).getBookname();
        holder.bname = (TextView) v.findViewById(R.id.bname_tv);
        holder.orderThis = (Button) v.findViewById(R.id.order_item_from_cart);
        holder.bcost = (TextView) v.findViewById(R.id.bcost_tv);
        holder.img = (ImageView) v.findViewById(R.id.book_img_view);
        holder.numOfItemsTv = (TextView) v.findViewById(R.id.num_of_items);
        holder.rem = (Button) v.findViewById(R.id.cart_list_remove_now);
        holder.bname.setText(book_details.get(position).getBookname());
        holder.bcost.setText(tCost);
        Picasso.with(context)
                .load(book_details.get(position).getbUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(holder.img);
        holder.numOfItemsTv.setText("Number of Items: " + String.valueOf(book_details.get(position).getNumberOfItems()));

        holder.rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.CartBook.remove(position);
                book_details.remove(position);
                CartListView.this.notifyDataSetChanged();
            }
        });

        final CartWishModel cartWishModel = new CartWishModel();
        cartWishModel.setbUrl(Store.CartBook.get(position).getbUrl());
        cartWishModel.setNumberOfItems(Store.CartBook.get(position).getNumberOfItems());
        cartWishModel.setCost(Store.CartBook.get(position).getCost());
        cartWishModel.setBisbn(Store.CartBook.get(position).getBisbn());
        cartWishModel.setBookname(Store.CartBook.get(position).getBookname());

        holder.orderThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clear store
                Store.Orders.clear();
                Store.Orders.add(cartWishModel);
                context.startActivity(new Intent(context, summary.class));
            }
        });

        return v;
    }

    public class Holder {
        TextView bname, bcost, numOfItemsTv;
        ImageView img;
        Button rem, orderThis;
    }


}
