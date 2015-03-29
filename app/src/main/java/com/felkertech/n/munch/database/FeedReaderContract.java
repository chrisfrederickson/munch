package com.felkertech.n.munch.database;

import android.provider.BaseColumns;

/**
 * Created by N on 3/20/2015.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "FOOD";
        public static final String COLUMN_NAME_ENTRY_ID = "ENTRYID";
        public static final String COLUMN_NAME_FOOD_ITEM = "ITEM";
        public static final String COLUMN_NAME_TIMESTAMP = "TIMESTAMP";
        public static final String COLUMN_NAME_CALORIES = "CALORIES";
        public static final String COLUMN_NAME_PROTEIN = "PROTEIN";
        public static final String COLUMN_NAME_CARBS = "CARBS";
        public static final String COLUMN_NAME_FAT = "FAT";
        public static final String COLUMN_NAME_SODIUM = "SODIUM";
        public static final String COLUMN_NAME_FOODURI = "FOODURI";
    }
}
