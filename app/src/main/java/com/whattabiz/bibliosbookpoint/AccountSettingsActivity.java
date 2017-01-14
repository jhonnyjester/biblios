package com.whattabiz.bibliosbookpoint;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {
    public static final String MEMBERSHIP_SHAREDPREF_NAME = "Membership_Status";
    public static final String MEMBERSHIP_SHAREDPREF_KEY = "";
    private final String url = "http://bibliosworld.com/Biblios/androidedituser.php?user_id=" + Store.user_id;
    // what the FUCK hari, this is TOTALLY BS
    // what shit names do you give to Variables
    /* Removed:: TextView editmembership */
    private TextView ac_name, ac_phone, ac_email, ac_user_id, edit_ac_name, edit_ac_phone, edit_ac_email, memberShip;
    private TextView curMemStatus;
    private String userName, userEmail, userNo, userMeminfo;
    private MaterialDialog.Builder dialog;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        this.setTitle("Account Settings");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        backButton = (ImageView) findViewById(R.id.back_button_toolbar);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AccountSettingsActivity.this.finish();
//            }
//        });

        //TextVIews to display info
        ac_name = (TextView) findViewById(R.id.account_name);
        ac_phone = (TextView) findViewById(R.id.account_phone);
        ac_email = (TextView) findViewById(R.id.account_email);
        ac_user_id = (TextView) findViewById(R.id.account_user_id);
        curMemStatus = (TextView) findViewById(R.id.current_mem_status);
        memberShip = (TextView) findViewById(R.id.edit_ac_membership);
        //TextViews to edit info
        edit_ac_name = (TextView) findViewById(R.id.edit_ac_name);
        edit_ac_phone = (TextView) findViewById(R.id.edit_ac_phone);
        edit_ac_email = (TextView) findViewById(R.id.edit_ac_email);

        // set the user id
        ac_user_id.setText(Store.user_id);


        // gets the details from SharedPrefs and sets it to respective TextViews
        getInfoFromSharedPref();


        edit_ac_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog("Name", 0);
            }
        });

        edit_ac_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog("Phone Number", 1);
            }
        });

        edit_ac_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog("Email ID", 2);
            }
        });

        memberShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getApplicationContext(), " Stopped Working due to some technical issues. Will be fixed ASAP!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AccountSettingsActivity.this, MembershipMainActivity.class));
            }
        });
    }

    private void getInfoFromSharedPref() { //gets info from shared preference and sets it to respective textViews

        SharedPreferences sharedPreferences = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("Name", "");
        userEmail = sharedPreferences.getString("Email", "");
        userNo = sharedPreferences.getString("phone", "");
        userMeminfo = getSharedPreferences(MEMBERSHIP_SHAREDPREF_NAME, Context.MODE_PRIVATE).getString(MEMBERSHIP_SHAREDPREF_KEY, "");
        ac_name.setText(userName);
        ac_email.setText(userEmail);
        curMemStatus.setText(userMeminfo);
        ac_phone.setText(userNo);
    }

    public void putInfoToShredPref(String text, int id) {
        SharedPreferences sharedPreferences = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (id) {
            case 0:
                editor.putString("Name", text);
                break;
            case 1:
                editor.putString("phone", text);
                break;
            case 2:
                editor.putString("Email", text);
                break;
        }
        editor.apply();
        restartActivity();
    }

    private void showEditDialog(String title, final int id) {
        // id =0 -->name , id =1 -->phone, id =2 -->email

        dialog = new MaterialDialog.Builder(this);
        dialog.title(title);
        switch (id) {
            case 0:
                dialog.input("Enter your name", userName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        validateInput(input.toString(), id);
                    }
                });
                break;
            case 1:
                dialog.inputType(InputType.TYPE_CLASS_PHONE);
                dialog.input("Enter Phone Number", userNo, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        validateInput(input.toString(), id);
                    }
                });
                break;
            case 2:
                dialog.inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                dialog.input("Enter your EmailID", userEmail, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        validateInput(input.toString(), id);
                    }
                });
                break;
        }
        dialog.show();
    }

    private void restartActivity() {
        finish();
        startActivity(new Intent(AccountSettingsActivity.this, AccountSettingsActivity.class));
//        this.recreate();
    }


    private void validateInput(String text, int ID) {
        switch (ID) {
            case 0:
                if (validateName(text)) {
                    // name is VALID
                    sendToServer(text, "name");
                    putInfoToShredPref(text, ID);
                } else {
                    Toast.makeText(this, "Invalid Name!", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;

            case 1:
                if (validatePhone(text)) {
                    // phone is VALID
                    sendToServer(text, "phone");
                    putInfoToShredPref(text, ID);
                } else {
                    Toast.makeText(this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;

            case 2:
                if (validateEmail(text)) {
                    // email is VALID
                    sendToServer(text, "email");
                    putInfoToShredPref(text, ID);
                } else {
                    Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;

        }

    }

    private void sendToServer(final String text, final String edit) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountSettingsActivity.this, "Some Error Occured! Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", "WhattabizBiblios");
                if (edit.contains("email")) {
                    // send email
                    params.put("email", text);
                } else if (edit.contains("phone")) {
                    // send phone number
                    params.put("phoneNum", text);
                } else if (edit.contains("name")) {
                    // send name
                    params.put("name", text);
                }

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @NonNull
    private Boolean validateName(String name) {
        boolean isValidName = false;
        if (!name.isEmpty()) {
            isValidName = true;
        }
        return isValidName;
    }

    private Boolean validatePhone(String phone) {
        boolean isValidPhone = false;
        if (phone.matches("[0-9]+") && (phone.length() == 12 || phone.length() == 10)) {
            isValidPhone = true;
        }
        return isValidPhone;
    }

    private boolean validateEmail(String email) {
        boolean isValidEmail = false;
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValidEmail = true;
        }
        return isValidEmail;
    }
}
