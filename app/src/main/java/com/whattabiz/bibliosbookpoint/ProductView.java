package com.whattabiz.bibliosbookpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;

/**
 * Invoked When User Clicks the i'th List Item
 */
public class ProductView extends AppCompatActivity {

    private LinearLayout cartbtn;
    private ImageView cartic, addwish, bookImage;
    private Animation iconRotate;
    private Boolean isWishIconSelected = false;
    private Button add, sub, order;
    private TextView count;
    private Integer num = 1;
    private TextView bookName;
    private int buttonClickCount = 0;
    private TextView discount, sellingPriceTextView, mrpPriceTextView, addToCartText;
    private Button toPromo;
    private String bname, bUrl, bSPrice, bMPrice, bisbn;
    private boolean isBookPresentInCart = false;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Book Info");

        discount = (TextView) findViewById(R.id.discount);
        sellingPriceTextView = (TextView) findViewById(R.id.selling_price_tv);
        mrpPriceTextView = (TextView) findViewById(R.id.mrp_price_tv);
        bookImage = (ImageView) findViewById(R.id.bookimg);
        cartic = (ImageView) findViewById(R.id.cartbtnic);
        iconRotate = AnimationUtils.loadAnimation(this, R.anim.iconrot);
        addwish = (ImageView) findViewById(R.id.wishlist);
        order = (Button) findViewById(R.id.orderbtn);
        cartbtn = (LinearLayout) findViewById(R.id.addbtn);
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        count = (TextView) findViewById(R.id.count);
        addToCartText = (TextView) findViewById(R.id.add_to_cart_text);
        bookName = (TextView) findViewById(R.id.productview_bookname);

        // add the search view
        addSearchView();

        // get details from click event
        bname = getIntent().getStringExtra("BookName");
        bUrl = getIntent().getStringExtra("BookImageUrl");
        bSPrice = getIntent().getStringExtra("BookSellingPrice");
        bMPrice = getIntent().getStringExtra("BookMrpPrice");
        bisbn = getIntent().getStringExtra("BookBisbn");
        bookName.setText(bname);
        sellingPriceTextView.setText(getResources().getString(R.string.rupee) + bSPrice);
        mrpPriceTextView.setText(getResources().getString(R.string.rupee) + bMPrice);

        this.setTitle(bname);

        discount.setVisibility(GONE);

        new CartWishModel(bname, bSPrice, bUrl);

        /* Set the book image */
        Picasso.with(this)
                .load(bUrl)
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(bookImage);

        /* Check for the book in Cart */
        if (Store.getCartBook().isEmpty()) {
            Log.e("Cart Status", "Empty");
        } else {
            isBookPresentInCart = Store.checkIfBookPresentInCart(bname);
            Log.d("iSBookPresent?", String.valueOf(isBookPresentInCart));
        }
        if (checkIfBookPresentInCart(bname)) {
            addwish.setBackgroundResource(R.drawable.wishyes);
            isWishIconSelected = true;
        } else {
            addwish.setBackgroundResource(R.drawable.wishno);
            isWishIconSelected = false;
        }
        // check if the book is already present in the Cart
        if (isBookPresentInCart) {
            // disable the add to cart button
            cartbtn.setClickable(false);
            addToCartText.setText("Added to Cart");
            cartic.setBackgroundResource(R.drawable.check);
        }
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // crate
                CartWishModel cartWishModel = new CartWishModel();
                cartWishModel.setBisbn(bisbn);
                cartWishModel.setbUrl(bUrl);
                cartWishModel.setNumberOfItems(num);
                cartWishModel.setBookname(bname);
                cartWishModel.setCost(bSPrice);
                Log.d("BISBN", bisbn);
                // only for product view order
                // clear the store orders
                Store.Orders.clear();
                Store.Orders.add(cartWishModel);
                startActivity(new Intent(ProductView.this, summary.class));
            }
        });

        addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if wish icon is selected
                // initially it is not selected
                if (!isWishIconSelected) {
                    addwish.setBackgroundResource(R.drawable.wishyes);
                    // add this book details to Singleton
                    CartWishModel cartWishModel = new CartWishModel();
                    cartWishModel.setBisbn(bisbn);
                    cartWishModel.setbUrl(bUrl);
                    cartWishModel.setNumberOfItems(num);
                    cartWishModel.setBookname(bname);
                    cartWishModel.setCost(bSPrice);
                    Store.addBookToWishList(cartWishModel);
                    Toast.makeText(ProductView.this, bname + " added to Wishlist", Toast.LENGTH_SHORT).show();

                    for (CartWishModel s : Store.WishList) {
                        Log.d("WL Items: ", s.getBookname());
                    }

                    isWishIconSelected = true;
                } else {
                    addwish.setBackgroundResource(R.drawable.wishno);
                    Store.removeBookFromWishList(bname);

                    for (CartWishModel s : Store.WishList) {
                        Log.d("WL Items: ", s.getBookname());
                    }
                    if (Store.WishList.isEmpty()) Log.i("Wishlist Status", "EMpty");

                    Toast.makeText(ProductView.this, bname + " removed from Wishlist", Toast.LENGTH_SHORT).show();
                    isWishIconSelected = false;
                }
            }
        });

        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if book is not present in cart
                // act like a typical add to cart button
                if (!isBookPresentInCart) {
                    CartWishModel cartWishModel = new CartWishModel();
                    cartWishModel.setBisbn(bisbn);
                    cartWishModel.setbUrl(bUrl);
                    cartWishModel.setNumberOfItems(num);
                    cartWishModel.setMrp(bMPrice);
                    cartWishModel.setBookname(bname);
                    cartWishModel.setCost(bSPrice);
                    cartWishModel.setNumberOfItems(num);
                    cartWishModel.setBisbn(bisbn);
                    Log.d("Number of items", cartWishModel.getNumberOfItems() + "");
                    Store.addBookToCart(cartWishModel);

                /* Animation Part */
                    // Rotate animation when clicked
                    iconRotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            cartic.setBackgroundResource(R.drawable.check);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    cartic.startAnimation(iconRotate);
                    Toast.makeText(ProductView.this, "x" + num + " Added to Cart !", Toast.LENGTH_LONG).show();
                    addToCartText.setText("Added to Cart");
                    cartbtn.setClickable(false);
                    buttonClickCount++;
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < 10) {
                    num++;
                    count.setText(num.toString());
                }
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    count.setText(num.toString());
                }
            }
        });
        toPromo = (Button) findViewById(R.id.to_promo);
        toPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ProductView.this.finish();
                startActivity(new Intent(getApplicationContext(), promoSelectActivity.class));
            }
        });
    }

    private void addSearchView() {
        // lots a find view by id's
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(ProductView.this, SearchResultsActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_items, menu);
        // get the menu item
        // assign it to SearchView Object
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.wishlist_toolbar) {
            startActivity(new Intent(ProductView.this, WishMainActivity.class));
        } else if (id == R.id.cart_toolbar) {
            startActivity(new Intent(ProductView.this, CartActivity.class));
        } else if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return true;
    }

    private boolean checkIfBookPresentInCart(String bookName) {
        return Store.checkIfBookPresentInWishList(bookName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkIfBookPresentInCart(bname)) {
            addwish.setBackgroundResource(R.drawable.wishyes);
            isWishIconSelected = true;
        } else {
            addwish.setBackgroundResource(R.drawable.wishno);
            isWishIconSelected = false;
        }
        // check if the book is already present in the Cart
        if (isBookPresentInCart) {
            // disable the add to cart button
            cartbtn.setClickable(false);
            addToCartText.setText("Added to Cart");
            cartic.setBackgroundResource(R.drawable.check);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkIfBookPresentInCart(bname)) {
            addwish.setBackgroundResource(R.drawable.wishyes);
            isWishIconSelected = true;
        } else {
            addwish.setBackgroundResource(R.drawable.wishno);
            isWishIconSelected = false;
        }
        // check if the book is already present in the Cart
        if (isBookPresentInCart) {
            // disable the add to cart button
            cartbtn.setClickable(false);
            addToCartText.setText("Added to Cart");
            cartic.setBackgroundResource(R.drawable.check);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            // just close the activity with transition
            supportFinishAfterTransition();
        }
    }

}
