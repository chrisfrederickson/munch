package com.felkertech.n.munch.Objects;

import android.graphics.drawable.Drawable;

/**
 * Created by N on 3/7/2015.
 */
public class StreamItem {
    int itemClass;
    String title;
    String secondaryTitle;
    String tertiaryTitle;
    int relevantImage;
    public StreamItem(int ic, String t, String st, String tt, int ri) {
        itemClass = ic;
        title = t;
        secondaryTitle = st;
        tertiaryTitle = tt;
        relevantImage = ri;
    }

    public int getItemClass() {
        return itemClass;
    }

    public String getTitle() {
        return title;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public String getTertiaryTitle() {
        return tertiaryTitle;
    }

    public int getRelevantImage() {
        return relevantImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
