package com.whattabiz.bibliosbookpoint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Replacement of HomePage in HomeActivty
 * Shows Coming Soon Book Card and RecyclerView list items for TopBooks and
 */
public class HomeFragment extends Fragment {
    // --Commented out by Inspection (08-10-2016 17:25):private final String baseUrl = "http://whattabiz.com/Biblios/images";
    private final String recentBooks = "http://bibliosworld.com/Biblios/androidrecent.php";
    private final String topBooks = "http://bibliosworld.com/Biblios/androidtop.php";
    private final String soonUrl = "http://bibliosworld.com/Biblios/androidComingSoon.php?key=WhattabizBiblios";
    private final String bestSellers = "http://bibliosworld.com/Biblios/androidbestseller.php";

    private ScrollView contentMainScrollView;
    private RecyclerView rvTop;
    private RecyclerView rvRecents;
    // private RecyclerView rvCardBanner;
    private RecyclerView rvSuggestedBooks;
    private TextView topBooksTv, recentBookTv, suggestedBooksTv;
    private MaterialDialog loadingDialog;

    // Slider Pager
    private ViewPager sliderPager;

    private String rupee;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // after the Fragment is created inflate the respective views
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderPager = (ViewPager) view.findViewById(R.id.slider_card_pager);
        contentMainScrollView = (ScrollView) view.findViewById(R.id.content_main_scrollview);
        contentMainScrollView.setSmoothScrollingEnabled(true);
        contentMainScrollView.setScrollbarFadingEnabled(true);

        topBooksTv = (TextView) view.findViewById(R.id.top_books_tv);
        recentBookTv = (TextView) view.findViewById(R.id.recent_books_tv);
        suggestedBooksTv = (TextView) view.findViewById(R.id.suggessted_books_tv);

        //
        suggestedBooksTv.setVisibility(View.GONE);

        rupee = getActivity().getApplicationContext().getResources().getString(R.string.rupee);

        /* Link RecyclerViews with the XML */
        rvRecents = (RecyclerView) view.findViewById(R.id.recycler_view);
        rvTop = (RecyclerView) view.findViewById(R.id.recycler_view_recents);
        //  rvCardBanner = (RecyclerView) view.findViewById(R.id.card_list);
        rvSuggestedBooks = (RecyclerView) view.findViewById(R.id.suggested_books_list);

        /* Build the ProgressDialog */
        loadingDialog = new MaterialDialog.Builder(getContext())
                .content("Loading. Please Wait!")
                .cancelable(false)
                .progress(true, 0)
                .build();

        /* Get the Book details only if Store.topBooks or Store.recentBooks is empty */
        if (Store.recentBooks.isEmpty() || Store.topBooks.isEmpty() || Store.cardBannerItems.isEmpty()) {
            makeRequest(view);
        } else {
            /* Get the Book details from Store.topBooks and Store.recentBooks and Store.cardBannerItems since it isn't empty */
            /* Add the HORIZONTAL LinearLayoutManager to RecyclerView*/
            //    rvCardBanner.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvTop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvRecents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            //  rvCardBanner.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            /* Send the Context and the id of RecyclerView */
            //   rvCardBanner.setAdapter(new CardBannerAdapter(getContext()));
            rvTop.setAdapter(new RecyclerAdapter(getContext(), 0));
            rvRecents.setAdapter(new RecyclerAdapter(getContext(), 1));
            //  rvCardBanner.setAdapter(new RecyclerAdapter(getContext(), 2));

            /* Set the Visibility of RecyclerView to Visible */
            topBooksTv.setVisibility(View.VISIBLE);
            recentBookTv.setVisibility(View.VISIBLE);
            rvRecents.setVisibility(View.VISIBLE);
            //   rvCardBanner.setVisibility(View.VISIBLE);
            rvTop.setVisibility(View.VISIBLE);

            initializeCardBannerItems();
            initializeSuggestedBooks();
        }
    }

    /*
    * Get the topBooks and recentBooks and cardBannerItems from the BackEnd
    * Parse the Json
    * Add the data into a BookObject
    * Add the BookObject to a static ArrayList<>
    * */
    private void makeRequest(final View view) {
        /* Show a Loading Dialog */
        loadingDialog.show();

        /* Request Top Books */
        StringRequest topBooksRequest = new StringRequest(topBooks, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        /* Create a book object and add details to it */
                        CartWishModel cartWishModel = new CartWishModel();
                        cartWishModel.setBisbn(jsonObject.getString("bisbn"));
                        cartWishModel.setMrp(jsonObject.getString("mrp"));
                        cartWishModel.setbUrl(jsonObject.getString("ppath"));
                        cartWishModel.setBookname(jsonObject.getString("bname"));
                        cartWishModel.setCost(jsonObject.getString("sp"));

                        /* Add this object into a static ArrayList<> */
                        Store.topBooks.add(cartWishModel);

                        /* Initialize TopBooks RecyclerView */
                        initializeTopBooks();

                        /* Dismiss Loading dialog if showing */
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /* Dismiss the Progress Dialog */
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        /* Request recent books */
        StringRequest recentBooksRequest = new StringRequest(recentBooks, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Recent Books", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        /* Create a book object and add details to it */
                        CartWishModel cartWishModel = new CartWishModel();
                        cartWishModel.setBisbn(jsonObject.getString("bisbn"));
                        cartWishModel.setMrp(jsonObject.getString("mrp"));
                        cartWishModel.setbUrl(jsonObject.getString("ppath"));
                        cartWishModel.setBookname(jsonObject.getString("bname"));
                        cartWishModel.setCost(jsonObject.getString("sp"));

                        /* Add this object into a static ArrayList<> */
                        Store.recentBooks.add(cartWishModel);

                        /* Initialize RecentBooks RecyclerView */
                        initializeRecentBooks();

                         /* Dismiss the Progress Dialog */
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /* Dismiss the Progress Dialog */
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        /* Make request for card banner items */
        StringRequest cardBannerRequest = new StringRequest(soonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        /* Create a book object and add details to it */
                        CartWishModel cartWishModel = new CartWishModel();
                        cartWishModel.setBisbn(jsonObject.getString("bisbn"));
                        cartWishModel.setMrp(jsonObject.getString("mrp"));
                        cartWishModel.setbUrl(jsonObject.getString("path"));
                        cartWishModel.setBookname(jsonObject.getString("name"));
                        cartWishModel.setCost(jsonObject.getString("sp"));

                        /* Add this object into a static ArrayList<> */
                        Store.cardBannerItems.add(cartWishModel);

                        /* Initialize CardBannerItems ViewPager */
                        initializeCardBannerItems();

                         /* Dismiss the Progress Dialog */
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        /* Requesting Suggested books here, NOTE: ACTUALLY ITS BEST SELLERS */
        StringRequest suggestBooks = new StringRequest(bestSellers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        /* Create a book object and add details to it */
                        CartWishModel cartWishModel = new CartWishModel();
                        cartWishModel.setBisbn(jsonObject.getString("bisbn"));
                        cartWishModel.setMrp(jsonObject.getString("mrp"));
                        cartWishModel.setbUrl(jsonObject.getString("ppath"));
                        cartWishModel.setBookname(jsonObject.getString("bname"));
                        cartWishModel.setCost(jsonObject.getString("sp"));

                        /* Add this object into a static ArrayList<> */
                        Store.suggestedBooks.add(cartWishModel);

                        /* Initialize CardBannerItems ViewPager */
                        initializeSuggestedBooks();

                         /* Dismiss the Progress Dialog */
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        /* Set Retry Time to 20s */
        cardBannerRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        topBooksRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        recentBooksRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        suggestBooks.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /* Add the Requests to the SingeInstance RequestQueue */
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(cardBannerRequest);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(recentBooksRequest);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(topBooksRequest);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(suggestBooks);
    }

    private void initializeCardBannerItems() {
        sliderPager.setAdapter(new CardSliderAdapter(getActivity().getSupportFragmentManager()));
    }

    private void initializeTopBooks() {
        /* Set the Visibility of RecyclerView to Visible */
        topBooksTv.setVisibility(View.VISIBLE);
        rvTop.setVisibility(View.VISIBLE);

        /* Add the HORIZONTAL LinearLayoutManager to RecyclerView*/
        rvTop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Send the Context and the id of RecyclerView */
        rvTop.setAdapter(new RecyclerAdapter(getContext(), 0));
    }

    private void initializeRecentBooks() {
        /* Set the Visibility of RecyclerView to Visible */
        recentBookTv.setVisibility(View.VISIBLE);
        rvRecents.setVisibility(View.VISIBLE);

        /* Add the HORIZONTAL LinearLayoutManager to RecyclerView*/
        rvRecents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Send the Context and the id of RecyclerView */
        rvRecents.setAdapter(new RecyclerAdapter(getContext(), 1));
    }

    // TODO: do some DataMining on Users book purchases and views
    // FIXME: displays books from RecentBooks
    private void initializeSuggestedBooks() {
        //    Store.suggestedBooks = Store.recentBooks;

        suggestedBooksTv.setVisibility(View.VISIBLE);

        /* Attach the Adapter to the Recycler View */
        rvSuggestedBooks.setAdapter(new RecyclerAdapter(getContext(), 2));

        /* Add Layout Manager */
        rvSuggestedBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private class CardSliderAdapter extends FragmentStatePagerAdapter {
        // Default Constructor
        CardSliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CardSliderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // number of Slider Pages is size of ArrayList<> CardBannerItems
            return Store.cardBannerItems.size();
        }
    }
}
