package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LoginURL = "http://www.bibliosworld.com/Biblios/androidLogin.php";
    private TextView loginHeaderText, forgotPasswordText;
    private TextInputLayout emailTextInput, passwordTextInput;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ImageView closeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        closeImage = (ImageView) findViewById(R.id.close_image);
        loginHeaderText = (TextView) findViewById(R.id.login_header_text);
        emailTextInput = (TextInputLayout) findViewById(R.id.email_text_input);
        passwordTextInput = (TextInputLayout) findViewById(R.id.password_text_input);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.btn_login);
        forgotPasswordText = (TextView) findViewById(R.id.forgot_password_text);

        // hook up listeners
        closeImage.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordText.setOnClickListener(this);

        // Sins of my past
        emailEditText.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (emailEditText.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    emailTextInput.setErrorEnabled(true);
                    passwordTextInput.setErrorEnabled(false);
                    emailTextInput.requestFocus();
                    emailTextInput.setError("Invalid Email");
                } else if (passwordEditText.getText().toString().isEmpty()) {
                    passwordTextInput.setErrorEnabled(true);
                    emailTextInput.setErrorEnabled(false);
                    passwordTextInput.requestFocus();
                    passwordTextInput.setError("Invalid Password");
                } else {
                    emailTextInput.setErrorEnabled(false);
                    passwordTextInput.setErrorEnabled(false);
                    sendRequest();
                }
                break;
            case R.id.close_image:
                // Reverse the shared element transition here
                supportFinishAfterTransition();
                break;
            case R.id.forgot_password_text:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            default:
                break;
        }
    }

    private void sendRequest() {
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();


        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("Loading")
                .cancelable(false)
                .progress(true, 0)
                .build();
        dialog.show();
        StringRequest stringReq = new StringRequest(Request.Method.POST, LoginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("LOGIN", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 0) {
                        Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (status == 1) {
                        String userName = obj.getString("name");
                        String message = obj.getString("message");
                        String email = obj.getString("email");
                        String phone = obj.getString("phone");
                        String userId = obj.getString("user_id");
                        saveDetails(userName, message, email, phone, userId);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", "WhattabizBiblios");
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringReq);
    }

    private void saveDetails(String userName, String message, String email, String phone, String userId) {
        Store.user_id = userId;

        SharedPreferences sharedPreferences = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Name", userName);
        editor.putString("phone", phone);
        /* VV Imp */
        editor.putString("user_id", userId);
        editor.commit();

        // goto Navigation Home activity
        startActivity(new Intent(LoginActivity.this, NavigationHomeActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
