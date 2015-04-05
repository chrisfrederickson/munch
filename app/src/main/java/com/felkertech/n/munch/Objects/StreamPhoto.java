package com.felkertech.n.munch.Objects;

import android.graphics.Bitmap;
import android.net.Uri;

import com.felkertech.n.munch.Utils.StreamTypes;
import com.felkertech.n.munch.database.FoodTableEntry;

/**
 * Created by N on 3/31/2015.
 */
public class StreamPhoto extends StreamItem {
    private FoodTableEntry entry;
    public StreamPhoto(FoodTableEntry fte) {
        super(StreamTypes.TYPE_GALLERY, fte.getFood(), "", "", 0);
        entry = fte;
    }

    public FoodTableEntry getEntry() {
        return entry;
    }
}
