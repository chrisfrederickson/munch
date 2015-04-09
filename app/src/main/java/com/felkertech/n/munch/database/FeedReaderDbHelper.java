package com.felkertech.n.munch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by N on 3/20/2015.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Munch.db";
    public static final String TAG = "munch::dbHelper";

    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " BIGINT";
    private static final String INT_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = /* IF NOT EXISTS */
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + LONG_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_WATER + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CARB + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR + FLOAT_TYPE+ COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CALCIUM + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_IRON + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_MAGNESIUM + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PHOSPHORUS + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_POTASSIUM + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ZINC + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_COPPER + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_MANGANESE + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SELENIUM + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_C + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_THIAMIN + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RIBOFLAVIN + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_NIACIN + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PHANTO_ACID + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B6 + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FOLATE + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLINE + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B12 + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_A + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RETINOL + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ALPHA_CAROT + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CAROT + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CRYPT + FLOAT_TYPE+ COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LYCOPHENE + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LUTEIN_ZEAXANTHIN + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_E + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_D + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_K + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SATURATED_FAT + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_MONOSATURATED_FAT + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_POLYSATURATED_FAT + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLESTEROL + FLOAT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public SQLiteDatabase write;
    public SQLiteDatabase read;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        //Make sure table is deleted first
//        db.execSQL(SQL_DELETE_ENTRIES); //FIXME This may not be a good idea
        Log.d(TAG, "Wave hi");
        db.execSQL(SQL_CREATE_ENTRIES);
        /*insert(new FoodTableEntry(0, new Date().getTime(), "Potato Chips"));
        insert(new FoodTableEntry(1, new Date().getTime(), "Chicken"));
        insert(new FoodTableEntry(2, new Date().getTime(), "Apple"));*/
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void insert(SQLiteDatabase db, FoodTableEntry fte) {
        Log.d(TAG, "Inserting " + fte.getFood());
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, fte.getId());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM, fte.getFood());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, fte.getTimestamp());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE, fte.getTagline());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES, fte.getCalories());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_WATER, fte.getWater());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN, fte.getProtein());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID, fte.getLipid());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CARB, fte.getCarb());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER, fte.getFiber());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR, fte.getSugar());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CALCIUM, fte.getCalcium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IRON, fte.getIron());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MAGNESIUM, fte.getMagnesium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHOSPHORUS, fte.getPhosphorus());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_POTASSIUM, fte.getPotassium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM, fte.getSodium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ZINC, fte.getZinc());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_COPPER, fte.getCopper());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MANGANESE, fte.getManganese());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SELENIUM, fte.getSelenium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_C, fte.getVit_c());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_THIAMIN, fte.getThiamin());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RIBOFLAVIN, fte.getRiboflavin());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NIACIN, fte.getNiacin());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHANTO_ACID, fte.getPhanto_acid());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B6, fte.getVit_b6());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOLATE, fte.getFolate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLINE, fte.getCholine());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B12, fte.getVit_b12());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_A, fte.getVit_a());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RETINOL, fte.getRetinol());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ALPHA_CAROT, fte.getAlpha_carot());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CAROT, fte.getBeta_carot());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CRYPT, fte.getBeta_crypt());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LYCOPHENE, fte.getLycophene());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LUTEIN_ZEAXANTHIN, fte.getLutein_zeaxanthin());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_E, fte.getVit_e());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_D, fte.getVit_d());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_K, fte.getVit_k());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SATURATED_FAT, fte.getSaturated_fat());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MONOSATURATED_FAT, fte.getMonosaturated_fat());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_POLYSATURATED_FAT, fte.getPolysaturated_fat());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLESTEROL, fte.getCholesterol());
        if(fte.getURI() == null)
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI, "");
        else
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI, fte.getURI().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);
        Log.d(TAG, newRowId+" seems successful");
    }
    public ArrayList<FoodTableEntry> readAll(SQLiteDatabase db) {
        read = getReadableDatabase();
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES,
                FeedReaderContract.FeedEntry.COLUMN_NAME_WATER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CARB,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CALCIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_IRON,
                FeedReaderContract.FeedEntry.COLUMN_NAME_MAGNESIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PHOSPHORUS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_POTASSIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ZINC,
                FeedReaderContract.FeedEntry.COLUMN_NAME_COPPER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_MANGANESE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SELENIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_C,
                FeedReaderContract.FeedEntry.COLUMN_NAME_THIAMIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RIBOFLAVIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NIACIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PHANTO_ACID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B6,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FOLATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLINE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B12,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_A,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RETINOL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ALPHA_CAROT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CAROT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CRYPT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LYCOPHENE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LUTEIN_ZEAXANTHIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_E,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_D,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_K,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SATURATED_FAT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_MONOSATURATED_FAT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_POLYSATURATED_FAT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLESTEROL
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                FeedReaderContract.FeedEntry._ID+">?", // The columns for the WHERE clause
                new String[]{"0"},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        Log.d(TAG, c.getCount()+" entries in readall");
        ArrayList<FoodTableEntry> entries = new ArrayList<>();
        if(c.getCount() == 0) {
            return entries;
        }
        c.moveToFirst();
        while(c.getPosition() < c.getCount()) {
            entries.add(
                    new FoodTableEntry(
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)),
                            c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP)),
                            c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM)),
                            c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI)),
                            c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_WATER)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CARB)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CALCIUM)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_IRON)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_MAGNESIUM)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PHOSPHORUS)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_POTASSIUM)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ZINC)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_COPPER)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_MANGANESE)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SELENIUM)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_C)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_THIAMIN)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RIBOFLAVIN)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NIACIN)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PHANTO_ACID)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B6)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOLATE)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLINE)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B12)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_A)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RETINOL)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ALPHA_CAROT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CAROT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CRYPT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_LYCOPHENE)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_LUTEIN_ZEAXANTHIN)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_E)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_D)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_K)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SATURATED_FAT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_MONOSATURATED_FAT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_POLYSATURATED_FAT)),
                            c.getFloat(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLESTEROL))
                    )
            );
            Log.d(TAG, "Read entry "+c.getPosition()+" / "+entries.size()+" - "+
                    c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM)));
            Log.d(TAG, "Of time "+c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP)));
            if(c.getPosition() < c.getCount()) {
                c.moveToNext();
                Log.d(TAG, "Moved to " + c.getPosition() + " / " + c.getCount());
            }
        }
        return entries;
    }

}
