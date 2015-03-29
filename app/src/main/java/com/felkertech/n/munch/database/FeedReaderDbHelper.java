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
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Munch.db";
    public static final String TAG = "munch::dbHelper";

    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " BIGINT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = /* IF NOT EXISTS */
            "CREATE TABLE  IF NOT EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + LONG_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CARBS + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FAT + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI + TEXT_TYPE +
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
        Log.d(TAG, "Inserting "+fte.getFood());
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, fte.getId());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM, fte.getFood());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, fte.getTimestamp());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES, fte.getCalories());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN, fte.getProtein());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CARBS, fte.getCarbs());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FAT, fte.getFat());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM, fte.getSodium());
        if(fte.getURI() == null)
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI, "");
        else
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI, fte.getURI().toString());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);
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
                FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CARBS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FAT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID+">?", // The columns for the WHERE clause
                new String[]{"0"},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<FoodTableEntry> entries = new ArrayList<>();
        if(c.getCount() == 0) {
            return entries;
        }
        c.moveToFirst();
        while(c.getPosition() < c.getCount()) {
            entries.add(
                    new FoodTableEntry(
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID)),
                            c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP)),
                            c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM)),
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES)),
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN)),
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CARBS)),
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FAT)),
                            c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM)),
                            c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI))
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
