package com.whattabiz.bibliosbookpoint;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Rumaan on 18-Jan-17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public MyFirebaseInstanceIdService() {
        super();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
