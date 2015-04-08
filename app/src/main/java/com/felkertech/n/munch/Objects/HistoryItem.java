package com.felkertech.n.munch.Objects;

import android.graphics.drawable.Drawable;

import com.felkertech.n.munch.Utils.StreamTypes;
import com.felkertech.n.munch.database.FoodTableEntry;

/**
 * Created by N on 3/7/2015.
 */
public class HistoryItem extends StreamItem {
    private float calories;
    private FoodTableEntry entry;
    public HistoryItem(String t, String st, float calories, int ri, FoodTableEntry fte) {
        super(StreamTypes.TYPE_ITEM, t, st, calories+" calories", ri);
        this.calories = calories;
        this.entry = fte;
    }

    public float getCalories() {
        return calories;
    }

    public FoodTableEntry getEntry() {
        return entry;
    }
}
