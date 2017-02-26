package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Rumaan on 27-10-2016.
 */

public class CardSliderFragment extends Fragment {

    private int position;

    public static CardSliderFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("POSITION", pos);
        CardSliderFragment fragment = new CardSliderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("POSITION");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_slider, container, false);

        CardView sliderCard = (CardView) rootView.findViewById(R.id.slider_card);

        // Add details to each child view elements
        ImageView bookImg = (ImageView) rootView.findViewById(R.id.book_img);
        Log.e("Img", Store.cardBannerItems.get(position).getbUrl());
        Picasso.with(getContext())
                .load(Store.cardBannerItems.get(position).getbUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(bookImg);

        TextView bookName = (TextView) rootView.findViewById(R.id.book_name);
        bookName.setText(Store.cardBannerItems.get(position).getBookname());

        TextView mrp = (TextView) rootView.findViewById(R.id.mrp_price_tv);
        mrp.setText(getContext().getString(R.string.rupee) + Store.cardBannerItems.get(position).getMrp());

        TextView sp = (TextView) rootView.findViewById(R.id.sp_price_tv);
        sp.setText(getContext().getString(R.string.rupee) + Store.cardBannerItems.get(position).getCost());
        return rootView;
    }
}
