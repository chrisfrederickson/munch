package com.felkertech.n.munch.Utils;

import android.content.Context;

import com.felkertech.n.munch.Objects.Hist;
import com.felkertech.n.munch.R;

import org.json.JSONArray;

public abstract class API {
    public static String API_BASE_URL = "https://munchapp.herokuapp.com";
    public static String API_BASE_PATH = "/api/v1/nutrition/";

    public static String getApiBaseUrl() {
        return API_BASE_URL + API_BASE_PATH;
    }

    public static String info(int id, float amount) {
        return getApiBaseUrl() + "info/?id=" + id + "&amount=" + amount;
    }
    public static String suggestion(String entry) {
        return getApiBaseUrl() + "suggestion/?entry=" + entry.replaceAll(" ","%20");
    }
    public static String recommend(boolean isMale, int age, boolean dr_nuts, boolean dr_dairy, boolean dr_veg, boolean dr_vegan, boolean dr_kosher, boolean dr_gluten, Hist hist) {
        return getApiBaseUrl() + "recommend/?dr_nuts="+dr_nuts+"&dr_dairy="+dr_dairy+"&dr_veg="+dr_veg+"&dr_vegan="+dr_vegan+"&dr_kosher="+dr_kosher+"&dr_gluten="+dr_gluten
                +"&age="+age+"&gender="+isMale
                +"&hist="+hist.toString();
    }
    public static String recommend(Context ctx, Hist hist) {
        SettingsManager sm = new SettingsManager(ctx);
        String gender = sm.getString(ctx.getString(R.string.sm_sex));
        boolean isMale = gender.equals(sm.getString(ctx.getString(R.string.male_sex)));
        int age = Integer.parseInt(sm.getString(ctx.getString(R.string.sm_age)));
        boolean dr_nuts = sm.getBoolean(ctx.getString(R.string.sm_nuts));
        boolean dr_dairy = sm.getBoolean(ctx.getString(R.string.sm_dairy));
        boolean dr_veg = sm.getBoolean(ctx.getString(R.string.sm_vege));
        boolean dr_vegan = sm.getBoolean(ctx.getString(R.string.sm_vegn));
        boolean dr_kosher = sm.getBoolean(ctx.getString(R.string.sm_kosher));
        boolean dr_gluten = sm.getBoolean(ctx.getString(R.string.sm_gluten));
        return recommend(isMale, age, dr_nuts, dr_dairy, dr_veg, dr_vegan, dr_kosher, dr_gluten, hist);
    }
}
