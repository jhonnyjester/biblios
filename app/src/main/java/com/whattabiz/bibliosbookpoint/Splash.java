package com.whattabiz.bibliosbookpoint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Shown at the Beginning of the AppLaunch
 */
public class Splash extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        Dexter.initialize(getApplicationContext());
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                // TODO: make case for onPermissionDenied()
                doNext();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Splash.this, "Permissions are Required for Biblios to function properly!", Toast.LENGTH_LONG).show();
                Splash.this.finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.READ_PHONE_STATE);

    }


    private void doNext() {
        // check for Previous Log-Ins
        // TODO: Register and Sign in page
        sharedPreferences = getSharedPreferences("BibliosUserPhoneNum", Context.MODE_PRIVATE);
        SharedPreferences spDetails = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
        String phoneNum = sharedPreferences.getString("BibliosUserPhoneNum", "");
        // if PhoneNumber from SharedPrefs is Empty move to RegistrationActivity
        // check both phone number and user details
        if (phoneNum.isEmpty() || spDetails.getString("Email", "").isEmpty() || spDetails.getString("Name", "").isEmpty()) {
            final Intent next = new Intent(Splash.this, RegistrationActivity.class);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(next);
                    Splash.this.finish();

                }
            }, 3000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, NavigationHomeActivity.class));
                    Splash.this.finish();
                }
            }, 3000);
        }
    }
}
