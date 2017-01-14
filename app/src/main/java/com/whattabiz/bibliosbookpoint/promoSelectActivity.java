package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class promoSelectActivity extends AppCompatActivity {

    private final List<promoApply> promoApplyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private promoApplyAdapter mAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_select);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new promoApplyAdapter(promoApplyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(promoSelectActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        buildPromo();

    }

    private void buildPromo() {
        promoApply code = new promoApply("20", "Summer Sale", "Get 20% discount on all purchases");
        promoApplyList.add(code);

        code = new promoApply("30", "New user Offer", "flat 30% off on all books");
        promoApplyList.add(code);
        mAdapter.notifyDataSetChanged();
    }
}

