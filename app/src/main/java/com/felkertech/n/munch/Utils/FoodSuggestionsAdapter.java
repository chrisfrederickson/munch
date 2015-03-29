package com.felkertech.n.munch.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by N on 3/24/2015.
 */
public class FoodSuggestionsAdapter extends ArrayAdapter<String> {
    private ArrayList<String> mData;
    private static final String TAG = "munch::FoodSuggestions";

    public FoodSuggestionsAdapter(Context ctx, int textView) {
        super(ctx, textView);
        mData = new ArrayList<String>();
    }

    public int getCount() {
        return mData.size();
    }
    public String getItem(int index) {
        return mData.get(index);
    }
    public Filter getFilter() {
        Log.d(TAG, "getFilter()");
        Filter myFilter = new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint != null) {
                    //TODO Send query to web server
                    try {
                        //TODO Retrieve data
                        mData = new ArrayList<>(); //FIXME
                        mData.add("Apple");
                        mData.add("Chicken");
                        mData.add("Cheese");
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    results.values = mData;
                    results.count = mData.size();
                    Log.d(TAG, results.count+" " +results.values.toString());
                }
                return results;
            }
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
