package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MembershipInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_info);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        this.setTitle("Membership Cards");
    }
}
