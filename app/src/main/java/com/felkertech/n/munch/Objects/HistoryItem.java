package com.felkertech.n.munch.Objects;

import android.graphics.drawable.Drawable;

import com.felkertech.n.munch.Utils.StreamTypes;

/**
 * Created by N on 3/7/2015.
 */
public class HistoryItem extends StreamItem {
    private int calories;
    public HistoryItem(String t, String st, int calories, int ri) {
        super(StreamTypes.TYPE_ITEM, t, st, calories+" calories", ri);
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }
}
