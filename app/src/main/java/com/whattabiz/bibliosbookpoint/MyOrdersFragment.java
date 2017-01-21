package com.whattabiz.bibliosbookpoint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private orderAdapter mAdapter;
    private ImageView emptyOrders;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emptyOrders = (ImageView) view.findViewById(R.id.empty_orders_img);

        if (Store.Orders.isEmpty()) {
            Toast.makeText(getContext(), "No Orders!", Toast.LENGTH_SHORT).show();
            emptyOrders.setVisibility(View.VISIBLE);
        } else {
            emptyOrders.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mAdapter = new orderAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
}
