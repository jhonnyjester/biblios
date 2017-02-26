package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * What the heck is this class for
 */
public class NumberVerify extends AppCompatActivity {

    private EditText number;
    private TextView text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_verification);
        number = (EditText) findViewById(R.id.numfield);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        AnimationUtils.loadAnimation(this, R.anim.viewhide);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);


            }
        });
    }
}
