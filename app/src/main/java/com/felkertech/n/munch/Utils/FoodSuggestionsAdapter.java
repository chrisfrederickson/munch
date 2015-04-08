package com.felkertech.n.munch.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by N on 3/24/2015.
 */
public class FoodSuggestionsAdapter extends ArrayAdapter<String> {
    public ArrayList<String> mData;
    public ArrayList<Integer> ids;
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
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults results = new FilterResults();
                if(constraint != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO Send query to web server
                            Log.d(TAG, "Running client");
                            HttpClient client = new DefaultHttpClient();
                            try {
                                HttpGet request = new HttpGet(API.suggestion(constraint.toString()));
                                HttpResponse response = client.execute(request);
                                String responseBody = EntityUtils.toString(response.getEntity());
                                Log.d(TAG, API.suggestion(constraint.toString()));
                                Log.d(TAG, responseBody);
                                JSONArray ja = new JSONArray(responseBody);
                                mData = new ArrayList<>();
                                ids = new ArrayList<>();
                                for(int i=0;i<ja.length();i++) {
                                    JSONObject jo = ja.getJSONObject(i);
                                    mData.add(jo.getString("name"));
                                    ids.add(jo.getInt("id"));
                                }
                                results.values = mData;
                                results.count = mData.size();
                                Log.d(TAG, results.count+" " +results.values.toString());
                                notifyDataSetChanged();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

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
