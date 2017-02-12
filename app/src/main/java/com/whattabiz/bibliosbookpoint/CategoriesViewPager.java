package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class CategoriesViewPager extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    String[] bookNames;
    private ArrayList<String> items = new ArrayList<>();
    private int pos;
    private boolean flag = false;
    private MaterialSearchView searchView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_view_pager);
        flag = false;

        /* Clear if not empty */
        if (!Store.categoriesBookName.isEmpty()) {
            Store.categoriesBookName.clear();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.categories_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // link ViewPager and and set its PageAdapter
        ViewPager categoriesPager = (ViewPager) findViewById(R.id.view_pager);
        categoriesPager.setAdapter(new PageAdapter(getSupportFragmentManager(), this));

        // add viewPager to tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(categoriesPager);


        /* Run these Asynchronously */
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... params) {

                for (int j = 0; j < NavigationHomeActivity.BookCategories.size(); j++) {
                    for (int k = 0; k < Store.bookCategories.get(NavigationHomeActivity.BookCategories.get(j)).size(); k++) {
                        ArrayList<CartWishModel> arrayList;
                        arrayList = Store.bookCategories.get(NavigationHomeActivity.BookCategories.get(j));
                        Store.categoriesBookName.add(arrayList.get(k));
                    }
                }

                for (int i = 0; i < Store.categoriesBookName.size(); i++) {
                    items.add(Store.categoriesBookName.get(i).getBookname());
                }

                return null;
            }
        });

        /* Set up Material SearchView */
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        /* Convert ArrayList to String[] array */
        bookNames = items.toArray(new String[items.size()]);

        /* Set the search view suggestions */
        searchView.setSuggestions(bookNames);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // send the search string to next activity
    private void searchForBooks(String string) {
        Intent intent = new Intent(CategoriesViewPager.this, SearchResultsActivity.class);
        intent.putExtra("SEARCH_STRING", string);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.categories_menu_items, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(menuItem);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            searchView.clearFocus();
        } else {
            super.onBackPressed();
            Intent intent = new Intent(CategoriesViewPager.this, NavigationHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(CategoriesViewPager.this, NavigationHomeActivity.class));
                CategoriesViewPager.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv = (TextView) view.findViewById(R.id.actv_tv);
        Intent intent = new Intent(getApplicationContext(), ProductView.class);
        intent.putExtra("BookName", tv.getText());
        for (int j = 0; j < Store.categoriesBookName.size(); j++) {
            if (Store.categoriesBookName.get(j).getBookname().equals(tv.getText())) {
                pos = j;
                flag = true;
            }
        }
        intent.putExtra("BookImageUrl", Store.categoriesBookName.get(pos).getbUrl());
        intent.putExtra("BookSellingPrice", Store.categoriesBookName.get(pos).getCost());
        intent.putExtra("BookMrpPrice", Store.categoriesBookName.get(pos).getMrp());
        intent.putExtra("BookBisbn", Store.categoriesBookName.get(pos).getBisbn()); //Store.CartBook.get(pos).getBisbn()
        if (flag) {
            startActivity(intent);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
