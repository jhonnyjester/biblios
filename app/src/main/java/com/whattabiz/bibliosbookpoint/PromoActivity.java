package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

public class PromoActivity extends AppCompatActivity {
    private final String PROMO_URL = "http://bibliosworld.com/Biblios/androidpromo.php";
    private RecyclerView recyclerView;
    private PromoCodeAdapter mAdapter;
    private ProgressBar progressBar;

    private String tempTotal;

    @Override
    protected void onStart() {
        super.onStart();
        tempTotal = getIntent().getStringExtra(summary.TOTAL_AMOUNT_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_select);
        setTitle("Promo Codes");

        progressBar = (ProgressBar) findViewById(R.id.prg_promo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        /*// if PromoCode codes is empty get the PromoCode codes
        if (Store.PROMO_CODE_CODEs.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(GONE);
            getThePromocodes();
            instantiateList();
        } else {
            instantiateList();
            progressBar.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }*/

        createPromoList();
    }

    /**
     * Create a List of Promo Codes and Inflate via Recycler View
     * use LinearLayoutManager
     */
    private void createPromoList() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PromoActivity.this.finish();
    }
}

