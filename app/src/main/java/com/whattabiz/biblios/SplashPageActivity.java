package com.whattabiz.biblios;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/* Activity which overrides the default White splash Page */

public class SplashPageActivity extends AppCompatActivity {

    // Parameter for previous login
    private boolean isLoggedIn = false;

    @Override
    protected void onStart() {
        super.onStart();
        String details = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE).getString("user_id", "");
        if (details.isEmpty() || details.equals("")) {
            isLoggedIn = false;
        } else {
            isLoggedIn = true;
            Log.d("LOGGED IN", details);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Check for permissions first */
        // ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        Dexter.initialize(getApplicationContext());
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Log.d("PERMISSION", "Granted");
                doNext();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(SplashPageActivity.this, "Permissions are Required for Biblios to function properly!", Toast.LENGTH_LONG).show();
                SplashPageActivity.this.finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.READ_PHONE_STATE);


    }

    private void doNext() {
        if (isLoggedIn) {
            startActivity(new Intent(SplashPageActivity.this, NavigationHomeActivity.class));
        } else {
            startActivity(new Intent(SplashPageActivity.this, RegistrationActivity.class));
        }
    }
}
