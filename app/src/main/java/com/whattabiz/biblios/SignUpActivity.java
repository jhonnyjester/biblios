package com.whattabiz.biblios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rumaan on 12-Jan-17.
 */

public class SignUpActivity extends AppCompatActivity {

    private Fragment userContactFragment, userDetailsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userContactFragment = new UserContactFragment();
    }
}
