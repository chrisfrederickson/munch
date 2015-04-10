package com.felkertech.n.munch.Objects;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by N on 4/7/2015.
 */
public class Hist {
    ArrayList<HistItem> history;
    public static String TAG = "munch::Hist";
    public Hist() {
        history = new ArrayList<>();
    }
    public void insert(HistItem hi) {
        history.add(hi);
    }
    public String toString() {
        String out = "[";
        for(HistItem hi: history) {
            out += hi.toString();
            Log.d(TAG, "Add to hist: "+hi.toString());
        }
        return out+"]";
    }
}
