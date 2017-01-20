package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Invoked after User enter the college and other relevant INFO
 */

public class UserDetails extends AppCompatActivity {

    // unique android Id
    // Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    private ImageView topicon;
    private Spinner cource, sem, prof;
    private Button submit;
    private Animation iconRotate;
    private EditText fullName, collegeName, email, passwordEditText, phoneEditText, passwordReeditText;
    private LinearLayout layout_clg, spinner_details;
    private boolean isNameValid = false;
    private boolean isEmailValid = false;
    private boolean isCourseValid = false;
    private boolean isCollegeValid = false;
    private boolean isSemValid = false;
    private boolean isProffesionValid = false;
    private boolean isProfessionSelected = false;
    private boolean isPasswordValid = false;
    private List<String> cources;
    private List<String> profession;
    private List<String> sems;
    private ArrayAdapter<String> dataAdapter0, dataAdapter1, dataAdapter3, dataAdapter2;
    private String name, collegename, course, semester, profess, Email, password, phoneNumber, passwordRedit;
    private int professionId;

    private SharedPreferences.Editor editor;


    private String url = "http://www.bibliosworld.com/Biblios/androidregistration.php";
    private boolean isPhoneValid = false;
    private boolean isPasswordReeditValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        submit = (Button) findViewById(R.id.submitbtn);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        prof = (Spinner) findViewById(R.id.prof);
        iconRotate = AnimationUtils.loadAnimation(this, R.anim.iconrot);
        topicon = (ImageView) findViewById(R.id.topicon);

        layout_clg = (LinearLayout) findViewById(R.id.layout_clg);
        spinner_details = (LinearLayout) findViewById(R.id.spinner_details);
        layout_clg.setVisibility(View.GONE);
        spinner_details.setVisibility(View.GONE);

        fullName = (EditText) findViewById(R.id.fullName);
        collegeName = (EditText) findViewById(R.id.collegeName);
        email = (EditText) findViewById(R.id.email);

        passwordReeditText = (EditText) findViewById(R.id.password_reenter_edit_text);

        cource = (Spinner) findViewById(R.id.cource);
        cources = new ArrayList<>();
        cources.add("Course");
        cources.add("B.E");
        cources.add("B.Arch");
        cources.add("M.tech");
        cources.add("Medical");
        dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cources);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cource.setAdapter(dataAdapter1);

        sem = (Spinner) findViewById(R.id.sem);
        sems = new ArrayList<>();
        sems.add("Semester");
        sems.add("I");
        sems.add("II");
        sems.add("III");
        sems.add("IV");
        sems.add("V");
        sems.add("VI");
        sems.add("VII");
        sems.add("VIII");
        dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sems);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem.setAdapter(dataAdapter2);

        //dataAdapter for department
        List<String> dept = new ArrayList<>();
        dept.add("department");
        dept.add("Computer Science");
        dept.add("Mechanical");
        dept.add("Civil");
        dept.add("Electronics");
        dataAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dept);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // profession adapters
        profession = new ArrayList<>();
        profession.add("Profession");
        profession.add("Student");
        profession.add("Lecturer");
        profession.add("Other");
        dataAdapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profession);
        dataAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prof.setAdapter(dataAdapter0);


        // Profession Selection Adapter
        prof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        layout_clg.setVisibility(View.GONE);
                        spinner_details.setVisibility(View.GONE);
                        // for 'Profession' option don't ask college name
                        isProfessionSelected = false;
                        break;
                    case 1:
                        // 'Student' option
                        cource.setAdapter(dataAdapter1);
                        sem.setAdapter(dataAdapter2);
                        layout_clg.setVisibility(View.VISIBLE);
                        spinner_details.setVisibility(View.VISIBLE);
                        // set the profession Selected to true
                        professionId = 0;
                        isProfessionSelected = true;
                        break;
                    case 2:
                        // 'Lecturer' option
                        cource.setAdapter(dataAdapter1);
                        sem.setAdapter(dataAdapter3);
                        layout_clg.setVisibility(View.VISIBLE);
                        spinner_details.setVisibility(View.VISIBLE);
                        professionId = 1;
                        isProfessionSelected = true;
                        break;
                    case 3:
                        layout_clg.setVisibility(View.GONE);
                        spinner_details.setVisibility(View.GONE);
                        // for 'Other' option don't ask college name
                        isProfessionSelected = false;
                        professionId = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Sends the User Details to Server
         * */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName, userEmail, userCollegeName, userCourse, userSem, profession;

                userName = fullName.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userCollegeName = collegeName.getText().toString().trim();
                userCourse = cource.getSelectedItem().toString();
                userSem = sem.getSelectedItem().toString();
                profession = prof.getSelectedItem().toString();
                password = passwordEditText.getText().toString();
                phoneNumber = phoneEditText.getText().toString();
                passwordRedit = passwordReeditText.getText().toString();


                // validate the details
                if (isAllDetailsValid(userName, userEmail, userCourse, userCollegeName,
                        userSem, profession, password, phoneNumber, passwordRedit)) {
                    iconRotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            topicon.setImageResource(R.drawable.verified);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                    });


                    if (isNetworkAvailable()) {

                        submit.setClickable(false);

                        // save details in SharedPrefs

                        SharedPreferences sharedPreferences = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("Email", userEmail);
                        editor.putString("Name", userName);
                        editor.putString("College Name", userCollegeName);
                        editor.putString("Course", userCourse);
                        editor.putString("Sem", userSem);
                        editor.putString("phone", phoneNumber);

                        editor.apply();

                        // save default Membership status now
                        SharedPreferences memSharedPrefs = getSharedPreferences("Membership_Status", Context.MODE_PRIVATE);
                        SharedPreferences.Editor memSharedPrefEditor = memSharedPrefs.edit();
                        memSharedPrefEditor.putString("Membership", "Silver");
                        memSharedPrefEditor.apply();

                        name = userName;
                        Email = userEmail;
                        collegename = userCollegeName;
                        course = userCourse;
                        semester = userSem;

                        makeRequest();
                    } else {
                        submit.setClickable(true);
                        Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }

                } else if (!isNameValid) {
                    // Toast.makeText(getApplicationContext(), "Invalid Name!", Toast.LENGTH_SHORT).show();
                    fullName.setError("Invalid Name!");
                    fullName.requestFocus();
                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                } else if (!isEmailValid) {
                    // Toast.makeText(getApplicationContext(), "Invalid Email Address!", Toast.LENGTH_SHORT).show();
                    email.setError("Invalid Email Address!");
                    email.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else if (!isCollegeValid) {
                    // Toast.makeText(getApplicationContext(), "Invalid College Name!", Toast.LENGTH_SHORT).show();
                    collegeName.setError("Invalid College Name!");
                    collegeName.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else if (!isCourseValid) {
                    Toast.makeText(getApplicationContext(), "Invalid Course!", Toast.LENGTH_SHORT).show();
                } else if (!isSemValid) {
                    Toast.makeText(getApplicationContext(), "Invalid Semester!", Toast.LENGTH_SHORT).show();
                } else if (!isProffesionValid) {
                    Toast.makeText(getApplicationContext(), "Please Choose the Profession!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid) {
                    passwordEditText.setError("Invalid Password!");
                    passwordEditText.requestFocus();
                } else if (!isPasswordReeditValid) {
                    passwordReeditText.setError("Passwords Don't match");
                    passwordReeditText.requestFocus();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid Details!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO: add textWatcher to validate input
    }

    private boolean isAllDetailsValid(String userName, String userEmail, String userCourse,
                                      String collegeName, String userSem, String uProf, String password, String phoneNumber, String passwordRedit) {
        if (isNameValid(userName)) {
            isNameValid = true;
            Log.v("Name Valid: ", "Valid");
        }

        if (isEmailValid(userEmail)) {
            isEmailValid = true;
            Log.v("Email Valid: ", "Valid");
        }

        if (isCourseValid(userCourse)) {
            isCourseValid = true;
            Log.v("Course Valid: ", "Valid");
        }

        if (isCollegeValid(collegeName)) {
            isCollegeValid = true;
            Log.v("College Valid: ", "Valid");
        }

        if (isSemValid(userSem)) {
            isSemValid = true;
            Log.v("Sem Valid: ", "Valid");
        }

        if (isProffesionsValid(uProf)) {
            isProffesionValid = true;
        }

        if (isPasswordValid(password)) {
            isPasswordValid = true;
        }

        if (isPhoneNumberValid(phoneNumber)) {
            isPhoneValid = true;
        }

        if (isPasswordReeditValid(passwordRedit)) {
            isPasswordReeditValid = true;
        }

        return isEmailValid & isNameValid & isCourseValid & isCollegeValid
                & isSemValid
                & isProffesionValid
                & isPasswordValid
                & isPasswordReeditValid
                & isPhoneValid;
    }

    private boolean isPasswordReeditValid(String passwordRedit) {
        return !passwordRedit.isEmpty() && passwordRedit.equals(password);
    }

    private boolean isPasswordValid(String password) {
        return !(password.isEmpty() && password.length() > 4);
    }

    private boolean isPhoneNumberValid(String phone) {
        return !(phone.isEmpty() && phone.length() != 10);
    }

    private boolean isNameValid(String name) {
        //  && name.matches("[a-zA-Z]+ ?")
        return !(name.isEmpty());
    }

    private boolean isEmailValid(String email) {
        return !(email.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isCourseValid(String course) {
        // if other and profession is Selected
        // don't check this
        return !isProfessionSelected || !(course.isEmpty()) && !course.equals(cources.get(0));
    }

    private boolean isCollegeValid(String college) {
        // check for college name only if Profession is Selected
        // else Just set as VALID
        //  && college.matches("[ ^0-9]")
        return !isProfessionSelected || !(college.isEmpty());
    }

    private boolean isSemValid(String sem) {
        // check for the Selected Profession
        // for OTHER and Profession dont check these
        return !isProfessionSelected || !(sem.isEmpty()) && !sem.equals(sems.get(0));
    }

    private boolean isProffesionsValid(String professions) {
        return !professions.equals(profession.get(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void makeRequest() {

        // make a material dialog progress bar
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("Registering...")
                .progress(true, 0)
                .cancelable(false)
                .build();

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // on response
                dialog.dismiss();
                Log.d("Response", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("status") == 1) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        // if successful go next
                        gotoNext();
                    } else {
                        submit.setClickable(true);
                        Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                submit.setClickable(true);
                // on Error
                Toast.makeText(getApplicationContext(), "Some Error Occured!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", "WhattabizBiblios");
                params.put("name", name);
                params.put("email", email.getText().toString());
                params.put("Password", password);
                params.put("professionId", String.valueOf(professionId));
                params.put("course", course);
                params.put("sem", semester);
                params.put("cname", collegename);
                params.put("phoneNum", phoneNumber);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

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

    private void gotoNext() {
        topicon.startAnimation(iconRotate);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), SplashPageActivity.class));
                UserDetails.this.finish();
            }
        }, 300);

    }

}
