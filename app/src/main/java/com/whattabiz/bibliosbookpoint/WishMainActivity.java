package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WishMainActivity extends AppCompatActivity {

    Context context; // used as 'this'
    private RecyclerView recyclerView;
    private LinearLayout wishListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        wishListEmpty = (LinearLayout) findViewById(R.id.empty_wish_img);
        recyclerView = (RecyclerView) findViewById(R.id.wish_list);

        this.setTitle("Wish List");

        /* If Wishlist is Empty, show the empty Img*/
        if (Store.WishList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            wishListEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Wish list is Empty!", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            wishListEmpty.setVisibility(View.GONE);


            recyclerView.setAdapter(new WishlistAdapter(this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}
