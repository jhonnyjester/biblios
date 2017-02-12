package com.whattabiz.bibliosbookpoint;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.whattabiz.bibliosbookpoint.databinding.ActivitySearchResultsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private final String KEY = "WhattabizBiblios";
    private final ArrayList<CartWishModel> searchResults = new ArrayList<>();
    private final String PREFIX_IMG_URL = "http://bibliosworld.com/Biblios/";
    private ActivitySearchResultsBinding activitySearchResultsBinding;
    private String searchString;
    private LinearLayout emptySearch;
    private String url = "http://bibliosworld.com/Biblios/androidsearch.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchResultsBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_results);

        // setContentView(R.layout.activity_search_results);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /* Get the Details from Previous Activity */
        if (getIntent().getStringExtra("SEARCH_STRING") != null) {
            searchString = getIntent().getStringExtra("SEARCH_STRING");
            this.setTitle("Results for " + "\'" + searchString + "\'");
            // this.setTitle("Results for " + "'" + searchString + "'");
        }

        emptySearch = (LinearLayout) findViewById(R.id.empty_search);
        activitySearchResultsBinding.searchResultBooks.setVisibility(View.GONE);

        initiateSearch();
    }

    private void initiateSearch() {
        // append the search to q parameter
        url += "q=" + searchString;
        // append the key
        url += "&key=" + KEY;
        StringRequest searchReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Search Response", response);
                // hide the progress
                activitySearchResultsBinding.progress.setVisibility(View.GONE);
                if (response.equals("[]") || response.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty Results!", Toast.LENGTH_SHORT).show();
                    emptySearch.setVisibility(View.VISIBLE);
                } else {
                    addItemsToArrayList(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(searchReq);
        /*if (!searchString.equals("") || !searchString.isEmpty()) {
            // check in Categories books names
            for (int i = 0; i < Store.categoriesBookName.size(); i++) {
                if (Store.categoriesBookName.get(i).getBookname().contains(searchString)) {
                    bookPresent = true;
                    // add that book to the SearchResults
                    searchResults.add(Store.categoriesBookName.get(i));
                }
            }
            // check in TopBooks
            for (int j = 0; j < Store.topBooks.size(); j++) {
                if (Store.topBooks.get(j).getBookname().contains(searchString)) {
                    bookPresent = true;
                    // add that book to the SearchResults
                    searchResults.add(Store.topBooks.get(j));
                }
            }
            // check in Recent Books
            for (int k = 0; k < Store.recentBooks.size(); k++) {
                if (Store.recentBooks.get(k).getBookname().contains(searchString)) {
                    bookPresent = true;
                    // add that book to the SearchResults
                    searchResults.add(Store.recentBooks.get(k));
                }
            }

            if (bookPresent) {
                emptySearch.setVisibility(View.GONE);
                populateList();
            } else {
                emptySearch.setVisibility(View.VISIBLE);
            }
        } else {
            bookPresent = false;
            activitySearchResultsBinding.emptySearch.setVisibility(View.VISIBLE);
            this.setTitle("No Books Found!");
        }*/
    }

    private void addItemsToArrayList(String response) {
        searchResults.clear();
        // parse the json
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObj = jsonArray.getJSONObject(i);
                CartWishModel cw = new CartWishModel();
                cw.setBisbn(bookObj.getString("bisbn"));
                cw.setBookname(bookObj.getString("bname"));
                cw.setMrp(bookObj.getString("mrp"));
                cw.setbUrl(PREFIX_IMG_URL + bookObj.getString("ppath"));
                cw.setCost(bookObj.getString("sp"));
                searchResults.add(cw);
            }

            // populate list view
            populateList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateList() {
        // feed the search results into the RecyclerView
        // TODO: Inflate the RecyclerView with the Search Results
        activitySearchResultsBinding.searchResultBooks.setVisibility(View.VISIBLE);
        activitySearchResultsBinding.searchResultBooks.setLayoutManager(new LinearLayoutManager(this));
        activitySearchResultsBinding.searchResultBooks.setAdapter(new SearchResultsAdapter(this, searchResults));
    }

    @Override
    public void onBackPressed() {
        SearchResultsActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SearchResultsActivity.this.finish();
        }
        return true;
    }
}
