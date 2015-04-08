package com.felkertech.n.munch.Utils;

import com.felkertech.n.munch.Objects.Hist;

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
        return getApiBaseUrl() + "suggestion/?entry=" + entry;
    }
    public static String recommend(boolean dr_nuts, boolean dr_dairy, boolean dr_veg, boolean dr_vegan, boolean dr_kosher, boolean dr_gluten, Hist hist) {
        return getApiBaseUrl() + "recommend/?dr_nuts="+dr_nuts+"&dr_dairy="+dr_dairy+"&dr_veg="+dr_veg+"&dr_vegan="+dr_vegan+"&dr_kosher"+dr_kosher+"&dr_gluten="+dr_gluten
                +"hist="+hist.toString();
    }
}
