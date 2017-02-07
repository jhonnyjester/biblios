package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private static final String DELETE_TOKEN_URL = "http://bibliosworld.com/Biblios/deleteToken.php";
    private static final String MEMBERSHIP_URL = "http://bibliosworld.com/Biblios/androidmembership.php?key=WhattabizBiblios";
    private CardView accountCardView, notifyCardView, clearCardView, aboutCardView;
    private View.OnClickListener mClickListener;
    private Context mContext;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getActivity().setTitle("Settings");


        accountCardView = (CardView) view.findViewById(R.id.account_card_View);
        // notifyCardView = (CardView) view.findViewById(R.id.notification_card_view);
        clearCardView = (CardView) view.findViewById(R.id.clear_records_card_view);
        aboutCardView = (CardView) view.findViewById(R.id.about_card_view);

        // make a typical onClickListener
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.account_card_View:
                        // changed from AccountSettings. class to AccountSettingsActivity.class
                        Intent intent = new Intent(view.getContext(), AccountSettingsActivity.class);
                        startActivity(intent);
                        break;
                    // case R.id.notification_card_view:
                    // TODO: Remove Notification
                    //   break;
                    case R.id.clear_records_card_view:
                        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                        ab.setTitle("Log Out").setMessage("Are you sure you want to Log Out?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        logOut();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = ab.create();
                        alertDialog.show();
                        break;
                    case R.id.about_card_view:
                       /* AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(aboutbiblios + versionCode)
                                .setMessage(about)
                                .setNeutralButton(ok, null); // try to link to version number
                        AlertDialog dialog = builder.create();
                        dialog.show(); */
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder
                                .setTitle(getString(R.string.about_biblios) + BuildConfig.VERSION_NAME)
                                .setMessage(getString(R.string.about))
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
            }
        };

        clearCardView.setOnClickListener(mClickListener);
//        notifyCardView.setOnClickListener(mClickListener);
        accountCardView.setOnClickListener(mClickListener);
        clearCardView.setOnClickListener(mClickListener);
        aboutCardView.setOnClickListener(mClickListener);

        /* Request Membership */
        requestMembership();
    }

    private void requestMembership() {
        /* Get the Membership details here */
        Log.e("USER ID", Store.user_id);
        StringRequest stringRequest = new StringRequest(MEMBERSHIP_URL + Store.user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MEMBERSHIP RESPONSE", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    /**
     * Removes SharedPrefs
     * Assigns all strings to ""
     */
    private void logOut() {
        StringRequest deleteTokenRequest = new StringRequest(Request.Method.POST, DELETE_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 1) {
                        SharedPreferences detailsPrefs = getActivity().getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor;
                        editor = detailsPrefs.edit();
                        editor.remove("Email");
                        editor.remove("Name");
                        editor.remove("College Name");
                        editor.remove("Course");
                        editor.remove("Sem");
                        editor.remove("Membership");
                        editor.remove("phone");
                        editor.remove("user_id");
                        editor.apply();

                        goNext();
                    } else {
                        Toast.makeText(getContext(), "Failed To Log Out!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Failed To Log Out!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Store.user_id);
                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(deleteTokenRequest);
    }

    private void goNext() {
        Toast.makeText(getActivity().getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish();
        startActivity(new Intent(getActivity().getApplicationContext(), RegistrationActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
