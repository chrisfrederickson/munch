package com.felkertech.n.munch.Objects;

import com.felkertech.n.munch.Utils.StreamTypes;

/**
 * Created by N on 4/5/2015.
 */
public class CalendarItem extends StreamItem {
    int calories;
    public CalendarItem(String d, int c) {
        super(StreamTypes.TYPE_CALENDAR, d, "", "", 0);
        calories = c;
    }

    public int getCalories() {
        return calories;
    }
}
