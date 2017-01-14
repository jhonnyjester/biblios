package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MembershipMainActivity extends AppCompatActivity implements View.OnClickListener {
    /*public static TextView membership_txt;
    public static ImageView membership_card;*/

    final Membership userMembership = new Membership();
    private ScrollView rootView;
    private TextView currentMembership;
    private TextView membershipMessage;
    private TextView totalBooksPurchased;
    private TextView requiredBooksForUpgrade;
    private ImageView membershipImage;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle("Membership Info");

        rootView = (ScrollView) findViewById(R.id.root_container);
        currentMembership = (TextView) findViewById(R.id.current_membership);
        membershipMessage = (TextView) findViewById(R.id.current_membership_message);
        totalBooksPurchased = (TextView) findViewById(R.id.total_books_purchased);
        requiredBooksForUpgrade = (TextView) findViewById(R.id.required_books_upgrade);
        membershipImage = (ImageView) findViewById(R.id.membership_image);

        membershipImage.setOnClickListener(this);

        dialog = new MaterialDialog.Builder(MembershipMainActivity.this)
                .content("Loading Membership Information..")
                .progress(true, 0)
                .cancelable(false)
                .build();

        /*SharedPreferences shared = getSharedPreferences("Membership_Status", Context.MODE_PRIVATE);

        // get the total books purchased from the orders

        Membership_status = shared.getString("Membership", "");
        switch (Membership_status) {
            case "Golden":
                //setText to gold
                membership_txt.setText("Golden");
                membership_card.setImageResource(R.drawable.golden);
                break;
            case "Platinum":
                //setText to platinum
                membership_txt.setText("Platinum");
                membership_card.setImageResource(R.drawable.platinum);
                break;
            default:
                //setText to silver
                membership_txt.setText("Silver");
                membership_card.setImageResource(R.drawable.silver1);
                break;
        }


        up_mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MembershipMainActivity.this, MemCardInfo.class));
            }
        });


        membership_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MembershipMainActivity.this);
                alert.setTitle("Information about the Membership");
                alert.setMessage("Silver/Gold/Platinum Membership");
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // Default Value: False
        boolean isOpenedOnce = getSharedPreferences("Membership_Status", Context.MODE_PRIVATE).getBoolean("isOpenedOnce", false);
        // if membership is already configured
        // then set Continue ButtonState to INVISIBLE
        if (isOpenedOnce) {
            continueButton.setVisibility(View.INVISIBLE);
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when clicked
                // save in SharedPrefs that Membership is Configured Once
                SharedPreferences preferences = getSharedPreferences("Membership_Status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isOpenedOnce", true);
                editor.apply();
                // goto Home
                // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                MembershipMainActivity.this.finish();
            }
        });
*/
        if (isNetworkAvailable()) {
            requestMembership();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }

    /* Request the
    *  - Current User membership information
    *  - Total Book Purchased
    *  - Total Orders Worth Purchased
    *  - Required Books For Next Upgrade
    *  - Required amount For Next Upgrade
    *  - Current Membership message
    *
    *  Set the retrieved Membership info into a Membership object
    * */
    private void requestMembership() {
        /* Show to Progress Dialog */
        dialog.show();

        StringRequest membershipRequest = new StringRequest(Constants.MEMBERSHIP_REQUEST_URL + Store.user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    userMembership.setMembershipInfo(jsonObject.getString("membership_info"));
                    userMembership.setCurrentMembershipMessage(jsonObject.getString("message"));
                    userMembership.setRequiredBooksForUpgrade(jsonObject.getString("req_books"));
                    userMembership.setRequiredPriceForUpgrade(jsonObject.getString("price_req"));
                    userMembership.setTotalBookPurchased(jsonObject.getString("current_books"));
                    userMembership.setTotalOrdersWorth(jsonObject.getString("orders_worth"));

                            /* Set up the Layout */
                    setUpViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MembershipMainActivity.this, "Some Error Occured!", Toast.LENGTH_SHORT).show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        /* Add to the RequestQueue */
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(membershipRequest);
    }

    private void setUpViews() {
        currentMembership.setText(userMembership.getMembershipInfo());
        totalBooksPurchased.setText(userMembership.getTotalBookPurchased());
        membershipMessage.setText(userMembership.getCurrentMembershipMessage());
        requiredBooksForUpgrade.setText(userMembership.getRequiredBooksForUpgrade());
        if (userMembership.getMembershipInfo().contains(userMembership.SILVER_MEMBERSHIP)) {
            membershipImage.setImageResource(R.drawable.silver1);
        } else if (userMembership.getMembershipInfo().contains(userMembership.GOLDEN_MEMBERSHIP)) {
            membershipImage.setImageResource(R.drawable.golden);
        } else if (userMembership.getMembershipInfo().contains(userMembership.PLATINUM_MEMBERSHIP)) {
            membershipImage.setImageResource(R.drawable.platinum);
        } else {
            // Default: Silver
            membershipImage.setImageResource(R.drawable.silver1);
        }

        rootView.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.membership_image:
                startActivity(new Intent(MembershipMainActivity.this, MembershipInfoActivity.class));
                break;
            default:
                break;
        }
    }
}
