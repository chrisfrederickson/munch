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
        public static final String TABLE_NAME = "FOOD3";
        public static final String COLUMN_NAME_ENTRY_ID = "ENTRYID";
        public static final String COLUMN_NAME_FOOD_ITEM = "ITEM";
        public static final String COLUMN_NAME_TIMESTAMP = "TIMESTAMP";
        public static final String COLUMN_NAME_FOODURI = "FOODURI";
        public static final String COLUMN_NAME_TAGLINE = "TAGLINE";
        public static final String COLUMN_NAME_CALORIES = "CALORIES";
        public static final String COLUMN_NAME_WATER = "WATER";
        public static final String COLUMN_NAME_PROTEIN = "PROTEIN";
        public static final String COLUMN_NAME_LIPID = "LIPID";
        public static final String COLUMN_NAME_CARB = "CARB";
        public static final String COLUMN_NAME_FIBER = "FIBER";
        public static final String COLUMN_NAME_SUGAR = "SUGAR";
        public static final String COLUMN_NAME_CALCIUM = "CALCIUM";
        public static final String COLUMN_NAME_IRON = "IRON";
        public static final String COLUMN_NAME_MAGNESIUM = "MAGNESIUM";
        public static final String COLUMN_NAME_PHOSPHORUS = "PHOSPHORUS";
        public static final String COLUMN_NAME_POTASSIUM = "POTASSIUM";
        public static final String COLUMN_NAME_SODIUM = "SODIUM";
        public static final String COLUMN_NAME_ZINC = "ZINC";
        public static final String COLUMN_NAME_COPPER = "COPPER";
        public static final String COLUMN_NAME_MANGANESE = "MANGANESE";
        public static final String COLUMN_NAME_SELENIUM = "SELENIUM";
        public static final String COLUMN_NAME_VIT_C = "VIT_C";
        public static final String COLUMN_NAME_THIAMIN = "THIAMIN";
        public static final String COLUMN_NAME_RIBOFLAVIN = "RIBOFLAVIN";
        public static final String COLUMN_NAME_NIACIN = "NIACIN";
        public static final String COLUMN_NAME_PHANTO_ACID = "PHANTO_ACID";
        public static final String COLUMN_NAME_VIT_B6 = "VIT_B6";
        public static final String COLUMN_NAME_FOLATE = "FOLATE";
        public static final String COLUMN_NAME_CHOLINE = "CHOLINE";
        public static final String COLUMN_NAME_VIT_B12 = "VIT_B12";
        public static final String COLUMN_NAME_VIT_A = "VIT_A";
        public static final String COLUMN_NAME_RETINOL = "RETINOL";
        public static final String COLUMN_NAME_ALPHA_CAROT = "ALPHA_CAROT";
        public static final String COLUMN_NAME_BETA_CAROT = "BETA_CAROT";
        public static final String COLUMN_NAME_BETA_CRYPT = "BETA_CRYPT";
        public static final String COLUMN_NAME_LYCOPHENE = "LYCOPHENE";
        public static final String COLUMN_NAME_LUTEIN_ZEAXANTHIN = "LUTEIN_ZEAXANTHIN";
        public static final String COLUMN_NAME_VIT_E = "VIT_E";
        public static final String COLUMN_NAME_VIT_D = "VIT_D";
        public static final String COLUMN_NAME_VIT_K = "VIT_K";
        public static final String COLUMN_NAME_SATURATED_FAT = "SATURATED_FAT";
        public static final String COLUMN_NAME_MONOSATURATED_FAT = "MONOSATURATED_FAT";
        public static final String COLUMN_NAME_POLYSATURATED_FAT = "POLYSATURATED_FAT";
        public static final String COLUMN_NAME_CHOLESTEROL = "CHOLESTEROL";
    }
}
