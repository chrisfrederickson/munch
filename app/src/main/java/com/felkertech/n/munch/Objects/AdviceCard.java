package com.felkertech.n.munch.Objects;

import android.graphics.drawable.Drawable;

import com.felkertech.n.munch.Utils.StreamTypes;

/**
 * Created by N on 3/7/2015.
 */
public class AdviceCard extends StreamItem {
    public AdviceCard(String t, String st, String tt, int ri) {
        super(StreamTypes.TYPE_ADVICE, t, st, tt, ri);
    }
}
