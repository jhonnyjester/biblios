package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestActivity extends AppCompatActivity {

    private EditText req_book_name, req_book_authour, req_book_categ;
    private Button request;
    private TextInputLayout til_req_book, til_req_author, til_req_categ;

    private String REQUEST_BOOK_URL = "http://bibliosworld.com/Biblios/androidrequest.php?key=WhattabizBiblios";

    @Override
    public void onBackPressed() {
        RequestActivity.this.finish();
        startActivity(new Intent(getApplicationContext(), NavigationHomeActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        this.setTitle("Request Book");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        req_book_name = (EditText) findViewById(R.id.req_book_name);
        req_book_authour = (EditText) findViewById(R.id.req_author);
        req_book_categ = (EditText) findViewById(R.id.req_categ);

        til_req_book = (TextInputLayout) findViewById(R.id.til_req_book);
        til_req_author = (TextInputLayout) findViewById(R.id.til_author);
        til_req_categ = (TextInputLayout) findViewById(R.id.til_categ);

        request = (Button) findViewById(R.id.request_btn);

        til_req_book.setErrorEnabled(true);
        til_req_author.setErrorEnabled(true);


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (req_book_name.getText().toString().isEmpty()) {
                    til_req_book.setError("Mandatory Field");
                }
                if (req_book_authour.getText().toString().isEmpty()) {
                    til_req_author.setError("Mandatory Field");
                } else {
                    Toast.makeText(getApplicationContext(), "Requesting, Please Wait!", Toast.LENGTH_SHORT).show();
                    request();
                }

               /* details[0] = req_book_name.getText().toString();
                details[1] = req_book_authour.getText().toString();
                details[2] = req_book_categ.getText().toString();*/
            }
        });
    }

    private void request() {
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .content("Sending your Request....")
                .progress(true, 0)
                .cancelable(false);
        final MaterialDialog dialog = materialDialog.build();

        dialog.show();
        String uid, cid, bname, bAname;
        // i don't know why, but still being extra cautious
        uid = Store.user_id;
        cid = String.valueOf(1);
        bAname = til_req_author.getEditText().getText().toString();
        bname = til_req_book.getEditText().getText().toString();
        StringRequest request = new StringRequest(REQUEST_BOOK_URL + "?bname=" + bname
                + "&author=" + bAname + "&user_id=" + uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response.contains("Thank")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
                dialog.dismiss();
                Toast.makeText(RequestActivity.this, "Some Error Occured, Please try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
