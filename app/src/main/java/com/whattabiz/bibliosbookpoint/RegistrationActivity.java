package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton, signButton;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginButton = (Button) findViewById(R.id.btn_login);
        signButton = (Button) findViewById(R.id.btn_signup);

        // hook up onclick listeners here
        loginButton.setOnClickListener(this);
        signButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_signup:
                Intent signUpIntent = new Intent(RegistrationActivity.this, UserDetails.class);
                startActivity(signUpIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Kill all, hopefully
        finishAffinity();
    }
}
