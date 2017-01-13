package com.whattabiz.biblios;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String forgotUrl = "http://www.bibliosworld.com/Biblios/androidForgotPassword.php";
    private static final String resendVerificationUrl = "http://www.bibliosworld.com/Biblios/resendLink.php";
    private TextInputLayout emailForgotPasswordTextInput;
    private Button forgotPasswordButton, resendMailButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForgotPasswordTextInput = (TextInputLayout) findViewById(R.id.email_forgot_text_input);
        forgotPasswordButton = (Button) findViewById(R.id.btn_forgot_password);
        resendMailButton = (Button) findViewById(R.id.btn_resend_email_verification);
        if (isNetworkAvailable()) {
            // hook up listeners if network available
            forgotPasswordButton.setOnClickListener(this);
            resendMailButton.setOnClickListener(this);
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgot_password:
                if (validate()) {
                    sendRequest(forgotUrl);
                }
                break;
            case R.id.btn_resend_email_verification:
                if (validate()) {
                    sendRequest(resendVerificationUrl);
                }
                break;
            default:
                break;
        }
    }

    private boolean validate() {
        email = emailForgotPasswordTextInput.getEditText().getText().toString();
        Log.d("Email", email);
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailForgotPasswordTextInput.setErrorEnabled(true);
            emailForgotPasswordTextInput.setError("Invalid Email");
            return false;
        } else {
            emailForgotPasswordTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private void sendRequest(String URL) {
        final MaterialDialog dialog = new MaterialDialog.Builder(ForgotPasswordActivity.this)
                .progress(true, 0)
                .cancelable(false)
                .content("Please Wait..")
                .build();
        dialog.show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("FORGOT RESPONSE", response);
                int status;
                String message;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");

                    // Just print irrespective of status and Keep Calm
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, "Some Error Occured!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", "WhattabizBiblios");
                params.put("email", email);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /* Check if the network is available or not */
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // Connected to the Internet
                return true;
            }
        }
        return false;
    }
}
