package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavigationHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<String> BookCategories = new ArrayList<>();

    private final String CART_SHAREDPREF_KEY = "CartBookList";
    private final String WISHLIST_SHAREDPREF_KEY = "WishBookList";
    private final String ORDERS_SHAREDPREF_KEY = "OrdersList";

    private final String CATEGORIES_URL = "http://bibliosworld.com/Biblios/androidcategory.php";
    private Fragment fragment;
    // private ArrayList<Categories> categories = new ArrayList<>();
    // required for back press count
    private int count = 0;

    private MaterialSearchView searchView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Home");


        // get user_id
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Store.user_id = getSharedPreferences("BibliosUserDetails", Context.MODE_PRIVATE)
                        .getString("user_id", "");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("USER ID", Store.user_id);
            }
        });




        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        addSearchView();
        if (Store.bookCategories.isEmpty()) {
            // get the details of categories
            makeCategoriesRequest();
        }

        getCartDetails();
        getOrderDetails();
        getWishlistDetails();


        // initiate home activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_navigation_home, new HomeFragment())
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(NavigationHomeActivity.this, SearchResultsActivity.class);
                intent.putExtra("SEARCH_STRING", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                count++;
                if (count > 1) {
                    putDataIntoPersistent();
                    moveTaskToBack(true);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Press back again to Leave!", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // listen for 2nd onBackPressed()
                        count = 0;

                    }
                }, 2000);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_items, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.wishlist_toolbar) {
            // goto wishlist
            startActivity(new Intent(NavigationHomeActivity.this, WishMainActivity.class));
        } else if (id == R.id.cart_toolbar) {
            // goto cart
            startActivity(new Intent(NavigationHomeActivity.this, CartActivity.class));
            //startActivity(new Intent(NavigationHomeActivity.this, AccountSettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            this.setTitle("Home");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_navigation_home, new HomeFragment()).commit();

        } else if (id == R.id.nav_order) {
            this.setTitle("My Orders");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_navigation_home, new MyOrdersFragment()).commit();

        } else if (id == R.id.nav_about) {
            this.setTitle("About");
            fragment = new AboutFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_navigation_home, fragment).commit();

        } else if (id == R.id.nav_request) {
            // HomeActivity.this.finish();
            startActivity(new Intent(getApplicationContext(), RequestActivity.class));
        } else if (id == R.id.nav_categories) {
            //   HomeActivity.this.finish();
            // send the categories details
            Intent intent = new Intent(NavigationHomeActivity.this, CategoriesViewPager.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            this.setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_home, new SettingsFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        putDataIntoPersistent();
    }

    private void putDataIntoPersistent() {
        // store cart list items into sharefprefs
        if (!Store.isCartListEmpty()) {
            // clean up the sharedprefs
            getSharedPreferences(CART_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            putCartListDataIntoSharedPref();
        } else {
            // clean up the sharedprefs
            getSharedPreferences(CART_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            Log.d("Cart is Empty", "Not Storing anything to SharedPrefs");
        }

        // store wishlist items into shared prefs
        if (!Store.CartBook.isEmpty()) {
            // if cart book list isn't empty
            getSharedPreferences(WISHLIST_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            putWishlistDetailsIntoSharedPref();
        } else {
            // clean up shared prefs
            getSharedPreferences(WISHLIST_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            Log.d("Wishlist is empty", "Not storing anything into shared prefs");
        }

        // store order details into SharedPrefs
        if (!Store.placedOrders.isEmpty()) {
            // if cart book list isn't empty
            getSharedPreferences(ORDERS_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            putOrdersIntoSharedPref();
        } else {
            // clean up shared prefs
            getSharedPreferences(ORDERS_SHAREDPREF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            Log.d("Orders is empty", "Not storing anything into shared prefs");

        }
    }

    private void putOrdersIntoSharedPref() {
        // save details to SharedPrefs
        SharedPreferences sharedPreferences = getSharedPreferences(ORDERS_SHAREDPREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Store.placedOrders.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bookname", Store.placedOrders.get(i).getBookname());
                jsonObject.put("bookimgurl", Store.placedOrders.get(i).getBookname());
                jsonObject.put("bisbn", Store.placedOrders.get(i).getBisbn());
                jsonObject.put("numberOfItems", Store.placedOrders.get(i).getNumberOfItems());
                jsonObject.put("bookprice", Store.placedOrders.get(i).getCost());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject parentJsonObj = new JSONObject();
        try {
            parentJsonObj.put("orderlist", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = String.valueOf(parentJsonObj);
        Log.i("Json String orderlsit", jsonString);

        editor.putString("Orderlist", jsonString);
        editor.apply();
    }

    private void putWishlistDetailsIntoSharedPref() {
        // save details to SharedPrefs
        SharedPreferences sharedPreferences = getSharedPreferences(WISHLIST_SHAREDPREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Store.WishList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bookname", Store.WishList.get(i).getBookname());
                jsonObject.put("bookimgurl", Store.WishList.get(i).getBookname());
                jsonObject.put("bisbn", Store.WishList.get(i).getBisbn());
                jsonObject.put("numberOfItems", Store.WishList.get(i).getNumberOfItems());
                jsonObject.put("bookprice", Store.WishList.get(i).getCost());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject parentJsonObj = new JSONObject();
        try {
            parentJsonObj.put("wishlist", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = String.valueOf(parentJsonObj);
        Log.i("Json String Wishlist", jsonString);

        editor.putString("Wishlist", jsonString);
        editor.apply();
    }

    /**
     * Save to Cart List onDestroy() NavigationHomeActivity -> this activity
     * Save the Cart List items to SharedPrefs
     * Convert the ArrayList into a JSON
     * Save the JSON as String
     *
     * @serialData CartBook is static ArrayList of Cart
     */

    private void putCartListDataIntoSharedPref() {
        // save details to SharedPrefs
        SharedPreferences sharedPreferences = getSharedPreferences(CART_SHAREDPREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Store.CartBook.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bookname", Store.CartBook.get(i).getBookname());
                jsonObject.put("bookimgurl", Store.CartBook.get(i).getBookname());
                jsonObject.put("bisbn", Store.CartBook.get(i).getBisbn());
                jsonObject.put("numberOfItems", Store.CartBook.get(i).getNumberOfItems());
                jsonObject.put("bookprice", Store.CartBook.get(i).getCost());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject parentJsonObj = new JSONObject();
        try {
            parentJsonObj.put("cartlist", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = String.valueOf(parentJsonObj);
        Log.i("Json String cart", jsonString);

        editor.putString("CartList", jsonString);
        editor.apply();
    }

    private void getOrderDetails() {
        String jsonString = getSharedPreferences(ORDERS_SHAREDPREF_KEY, Context.MODE_PRIVATE).getString("Orderlist", "");
        if (!jsonString.equals("")) {
            // if json data is present
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("orderlist");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String bookName = jObj.getString("bookname");
                    String bookImageUrl = jObj.getString("bookimgurl");
                    String bookPrice = jObj.getString("bookprice");
                    int numOfItems = jObj.getInt("numberOfItems");
                    String bisbn = jObj.getString("bisbn");

                    // create a PoJo and assign Data from the JSON
                    CartWishModel cartWishModel = new CartWishModel(bookName, bookPrice, bookImageUrl);
                    cartWishModel.setNumberOfItems(numOfItems);
                    cartWishModel.setBisbn(bisbn);

                    // if book already present in wishlist
                    // skip that book
                    if (!Store.checkIfBookPresentInOrders(bookName)) {
                        Store.addBookOrders(cartWishModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getWishlistDetails() {
        String jsonString = getSharedPreferences(WISHLIST_SHAREDPREF_KEY, Context.MODE_PRIVATE).getString("Wishlist", "");
        if (!jsonString.equals("")) {
            // if json data is present
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("wishlist");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String bookName = jObj.getString("bookname");
                    String bookImageUrl = jObj.getString("bookimgurl");
                    String bookPrice = jObj.getString("bookprice");
                    int numOfItems = jObj.getInt("numberOfItems");
                    String bisbn = jObj.getString("bisbn");

                    // create a PoJo and assign Data from the JSON
                    CartWishModel cartWishModel = new CartWishModel(bookName, bookPrice, bookImageUrl);
                    cartWishModel.setNumberOfItems(numOfItems);
                    cartWishModel.setBisbn(bisbn);

                    // if book already present in wishlist
                    // skip that book
                    if (!Store.checkIfBookPresentInWish(bookName)) {
                        Store.addBookToWishList(cartWishModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the cart details from SharedPrefs
     * It is saved as JSON String
     * Parse it and assign to ArrayList<>
     */
    private void getCartDetails() {
        String jsonString = getSharedPreferences(CART_SHAREDPREF_KEY, Context.MODE_PRIVATE).getString("CartList", "");
        if (!jsonString.equals("")) {
            // if json data is present
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("cartlist");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String bookName = jObj.getString("bookname");
                    String bookImageUrl = jObj.getString("bookimgurl");
                    String bookPrice = jObj.getString("bookprice");
                    int numOfItems = jObj.getInt("numberOfItems");
                    jObj.getString("bisbn");
                    // create a PoJo and assign Data from the JSON
                    CartWishModel cartWishModel = new CartWishModel(bookName, bookPrice, bookImageUrl);
                    cartWishModel.setNumberOfItems(numOfItems);
                    cartWishModel.setBisbn("bisbn");

                    // if book already present in cart
                    // skip that book
                    if (!Store.checkIfBookPresentInCart(bookName)) {
                        Store.addBookToCart(cartWishModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Request the JSON for CategoriesActivity
     */
    private void makeCategoriesRequest() {
        StringRequest stringRequest = new StringRequest(CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Categories resp", response);
                parseCategoriesJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setShouldCache(true);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * Parse the JSON response of Categories
     * Create a POJO out of it
     * Send it to the CategoriesViewPager activity
     */
    private void parseCategoriesJson(String response) {
        try {
            // clear before u enter
            BookCategories.clear();
            Store.bookCategories.clear();
            JSONObject parentJSON = new JSONObject(response);
            JSONArray categoriesArray = parentJSON.getJSONArray("categories");

            // get the Categories List
            for (int i = 0; i < categoriesArray.length(); i++) {
                BookCategories.add(categoriesArray.getString(i));
            }

            // get the Book details
            JSONObject booksObj = parentJSON.getJSONObject("books");

            String bname;
            String imageurl;
            String bisbn;
            String mrp;
            String sp;
            // assign it to hash map

            // loop through categories
            for (int j = 0; j < BookCategories.size(); j++) {

                JSONArray categoryBookArray = booksObj.getJSONArray(BookCategories.get(j));
//                Log.e("BOok array length", categoryBookArray.length() + "");
//                Log.d("Category \n", BookCategories.get(j));

                ArrayList<CartWishModel> cartWishModelArrayList = new ArrayList<>();
                // loop through this BookCategories
                for (int k = 0; k < categoryBookArray.length(); k++) {

                    JSONObject eachBook = categoryBookArray.getJSONObject(k);
                    bname = eachBook.getString("bname");
                    imageurl = eachBook.getString("ppath");
                    bisbn = eachBook.getString("bisbn");
                    mrp = eachBook.getString("mrp");
                    sp = eachBook.getString("sp");
                    // assign categories to this object
                    String category = BookCategories.get(j);
                    //Log.d("Book Names BITCH", bname);

                    // add details to the object
                    CartWishModel cartWishModel = new CartWishModel();
                    cartWishModel.setBisbn(bisbn);
                    cartWishModel.setbUrl(imageurl);
                    cartWishModel.setMrp(mrp);
                    cartWishModel.setCost(sp);
                    cartWishModel.setBookname(bname);
                    cartWishModel.setCategoryName(category);
                    cartWishModelArrayList.add(cartWishModel);
                    //    Log.d("cart wish mode size", cartWishModelArrayList.size() + "");
                }
                Store.bookCategories.put(BookCategories.get(j), cartWishModelArrayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
