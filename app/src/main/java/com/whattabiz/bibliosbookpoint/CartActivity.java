package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private final ArrayList<CartWishModel> details = new ArrayList<>();
    private ListView lv;
    private Button orderAll;
    private ImageView emptyCartImg;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle("Cart");
        orderAll = (Button) findViewById(R.id.orderAll);
        lv = (ListView) findViewById(R.id.cart_list);
        emptyCartImg = (ImageView) findViewById(R.id.empty_cart_img);

        addItemsmine();
        if (details.isEmpty()) {
            Toast.makeText(CartActivity.this, "No Items In Cart", Toast.LENGTH_SHORT).show();
            orderAll.setVisibility(View.INVISIBLE);
            lv.setVisibility(View.INVISIBLE);
            emptyCartImg.setVisibility(View.VISIBLE);

        } else {
            emptyCartImg.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            CartListView cartListView = new CartListView(this, details);
            lv = (ListView) findViewById(R.id.cart_list);
            lv.setAdapter(cartListView);
            lv.setDividerHeight(0);
        }

        orderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assign CartList to OrdersList
                // clear before doing anything stupid
                Store.Orders.clear();
                Store.Orders.addAll(Store.CartBook);
                Intent i = new Intent(CartActivity.this, summary.class);
                startActivity(i);
            }
        });
    }

    private void addItemsmine() {
        details.addAll(Store.getCartBook());
        if (details.isEmpty()) {
            Log.d("Stored", "nothing");
        }
    }
}

