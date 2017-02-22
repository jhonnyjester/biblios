package com.whattabiz.bibliosbookpoint;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 04-09-2016.
 */
public class Store extends Application {
    public static final ArrayList<CartWishModel> topBooks = new ArrayList<>();
    public static final ArrayList<CartWishModel> recentBooks = new ArrayList<>();
    public static final ArrayList<CartWishModel> CartBook = new ArrayList<>();
    public static final ArrayList<CartWishModel> WishList = new ArrayList<>();
    public static final ArrayList<CartWishModel> placedOrders = new ArrayList<>();
    // required for search
    public static final ArrayList<CartWishModel> categoriesBookName = new ArrayList<>();
    // cool stuff's here
    public static final HashMap<String, ArrayList<CartWishModel>> bookCategories = new HashMap<>();

    public static float CURRENT_TOTAL;
    public static boolean isPromoCodeApplied = false;
    public static Intent promoData = new Intent();

    public static ArrayList<CartWishModel> cardBannerItems = new ArrayList<>();
    public static ArrayList<CartWishModel> suggestedBooks = new ArrayList<>();
    public static ArrayList<CartWishModel> Orders = new ArrayList<>();
    public static String user_id;

    public Store() {
    }

    public static boolean isBookAlreadyInCart(CartWishModel cartWishModel) {
        for (int i = 0; i < CartBook.size(); i++) {
            // book already present
            return CartBook.get(i).getBookname().equals(cartWishModel.getBookname());
        }
        return false;
    }

    public static void addBookToWishList(CartWishModel cartWishModel) {
        WishList.add(cartWishModel);
    }

    public static void addBookOrders(CartWishModel cartWishModel) {
        placedOrders.add(cartWishModel);
    }

    public static void removeBookFromWishList(String bookName) {
        for (int i = 0; i < WishList.size(); i++) {
            if (WishList.get(i).getBookname().contains(bookName)) {
                WishList.remove(i);
            }
        }
    }

    public static ArrayList<CartWishModel> getOrders() {
        return Orders;
    }

    public static void setOrders(ArrayList<CartWishModel> orders) {
        Orders = orders;
    }

    public static void addOrder(CartWishModel cartWishModel) {
        Orders.add(cartWishModel);
    }

    public static boolean isCartListEmpty() {
        return CartBook.isEmpty();
    }

    public static boolean checkIfBookPresentInCart(String bname) {
        if (!CartBook.isEmpty()) {
            for (int i = 0; i < CartBook.size(); i++) {
                if (bname.equals(CartBook.get(i).getBookname())) {
                    // book already present
                    return true;
                }
            }
        }
        // else return False
        return false;
    }

    public static boolean checkIfBookPresentInWish(String bname) {
        if (!WishList.isEmpty()) {
            for (int i = 0; i < WishList.size(); i++) {
                if (bname.equals(WishList.get(i).getBookname())) {
                    // book already present
                    return true;
                }
            }
        }
        // else return False
        return false;
    }

    public static boolean checkIfBookPresentInOrders(String bname) {
        if (!placedOrders.isEmpty()) {
            for (int i = 0; i < placedOrders.size(); i++) {
                if (bname.equals(placedOrders.get(i).getBookname())) {
                    // book already present
                    return true;
                }
            }
        }
        // else return False
        return false;
    }

    public static boolean checkIfBookPresentInWishList(String bookName) {
        if (!WishList.isEmpty()) {
            for (int i = 0; i < WishList.size(); i++) {
                if (WishList.get(i).getBookname().contains(bookName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<CartWishModel> getCartBook() {
        return CartBook;
    }

    public static void setCartBook(ArrayList<CartWishModel> book) {
        CartBook.addAll(book);
    }

    public static void addBookToCart(CartWishModel cartWishModel) {
        CartBook.add(cartWishModel);
    }

    public static boolean checkIfOrderPresent(String bname) {
        if (!Orders.isEmpty()) {
            for (int i = 0; i < Orders.size(); i++) {
                if (bname.equals(Orders.get(i).getBookname())) {
                    // book already present
                    return true;
                }
            }
        }
        return false;
    }
}
