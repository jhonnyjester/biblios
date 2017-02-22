package com.whattabiz.bibliosbookpoint;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This Fragment Shows ABOUT information of The App / Whattabiz
 */
public class AboutFragment extends Fragment {

    private static final String ABOUT_US_URL = "http://bibliosworld.com/Biblios/aboutUs.php";

    private static final String TAG = "AboutFragment";

    private int count = 0;

    private TextView aboutBibliosText;

    private String instaUrl, fbUrl, aboutText;

    private ImageButton instaBtn, fbBtn;

    private boolean isLoaded = false;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // send request for about biblios details
        getAboutDetails();
    }

    private void getAboutDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ABOUT_US_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                // parse the response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    instaUrl = jsonObject.getString("insta");
                    fbUrl = jsonObject.getString("insta");
                    aboutText = jsonObject.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getActivity().setTitle("About");

        aboutBibliosText = (TextView) view.findViewById(R.id.about_biblios);
        instaBtn = (ImageButton) view.findViewById(R.id.insta_btn);
        fbBtn = (ImageButton) view.findViewById(R.id.fb_btn);

        instaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instaUrl));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbUrl));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // easter egg
        ImageView wImageView = (ImageView) view.findViewById(R.id.whattabiz_logo);
        wImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 15;
                count++;
                if (count >= 10) {
                    if (count >= 15) {
                        Toast.makeText(v.getContext(), "Tiger Mode Enabled!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "You're " + (i - count) + " steps away from tiger mode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /* If receiving about details is successful */
        if (isLoaded) {
            aboutBibliosText.setText(aboutText);
        }
    }
}
