package com.felkertech.n.munch.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by N on 3/28/2015.
 */
public abstract class InternetManager {
    public boolean isOnline(Context cxt) {
        ConnectivityManager connMgr = (ConnectivityManager)
                cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
