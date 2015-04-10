package com.felkertech.n.munch.database;

import android.net.Uri;
import android.util.Log;

import com.felkertech.n.munch.Objects.Food;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by N on 3/20/2015.
 */
public class FoodTableEntry {
    private int id;
    private long timestamp;
//    private Food food;
    private String food;
    private String tagline;
    private int amount;
    private float calories;
    private float water;
    private float protein;
    private float lipid;
    private float carb;
    private float fiber;
    private float sugar;
    private float calcium;
    private float iron;
    private float magnesium;
    private float phosphorus;
    private float potassium;
    private float sodium;
    private float zinc;
    private float copper;
    private float manganese;
    private float selenium;
    private float vit_c;
    private float thiamin;
    private float riboflavin;
    private float niacin;
    private float phanto_acid;
    private float vit_b6;
    private float folate;
    private float choline;
    private float vit_b12;
    private float vit_a;
    private float retinol;
    private float alpha_carot;
    private float beta_carot;
    private float beta_crypt;
    private float lycophene;
    private float lutein_zeaxanthin;
    private float vit_e;
    private float vit_d;
    private float vit_k;
    private float saturated_fat;
    private float monosaturated_fat;
    private float polysaturated_fat;
    private float cholesterol;

    private Uri URI;

    public static String TAG = "munch::FoodTableEntry";

    /**
     * Takes a response from the server and creates an entry from it
     * @param responseBody
     */
    public FoodTableEntry(String responseBody) {
        try {
            JSONObject data = new JSONObject(responseBody);
            this.id = data.getInt("id");
            this.timestamp = new Date().getTime();
            this.food = data.getString("name");
            this.calories = getFloat("calories", data);
            this.water = getFloat("water", data);
            this.protein = getFloat("protien", data);
            this.lipid = getFloat("lipid", data);
            this.carb = getFloat("carb", data);
            this.fiber = getFloat("fiber", data);
            this.sugar = getFloat("sugar", data);
            this.calcium = getFloat("calcium", data);
            this.iron = getFloat("iron", data);
            this.magnesium = getFloat("magnesium", data);
            this.phosphorus = getFloat("phosphorous", data);
            this.potassium = getFloat("potassium", data);
            this.sodium = getFloat("sodium", data);
            this.zinc = getFloat("zinc", data);
            this.copper = getFloat("copper", data);
            this.manganese = getFloat("manganese", data);
            this.selenium = getFloat("selenium", data);
            this.vit_c = getFloat("vit_c", data);
            this.thiamin = getFloat("thiamin", data);
            this.riboflavin = getFloat("riboflavin", data);
            this.niacin = getFloat("niacin", data);
            this.phanto_acid = getFloat("phanto_acid", data);
            this.vit_b6 = getFloat("vit_b6", data);
            this.folate = getFloat("folate", data);
            this.choline = getFloat("choline", data);
            this.vit_b12 = getFloat("vit_b12", data);
            this.vit_a = getFloat("vit_a", data);
            this.retinol = getFloat("retinol", data);
            this.alpha_carot = getFloat("alpha_carot", data);
            this.beta_carot = getFloat("beta_carot", data);
            this.beta_crypt = getFloat("beta_crypt", data);
            this.lycophene = getFloat("lycophene", data);
            this.lutein_zeaxanthin = getFloat("lutein_zeaxanthin", data);
            this.vit_e = getFloat("vit_e", data);
            this.vit_d = getFloat("vit_d", data);
            this.vit_k = getFloat("vit_k", data);
            this.saturated_fat = getFloat("saturated_fat", data);
            this.monosaturated_fat = getFloat("monosaturated_fat", data);
            this.polysaturated_fat = getFloat("polysaturated_fat", data);
            this.cholesterol = getFloat("cholesterol", data);

            this.URI = Uri.parse(data.getString("img"));
            Log.d(TAG, URI.toString());
            this.tagline = getSubtitle();
        } catch (JSONException e) {
            e.printStackTrace();
            this.id = -1;
        }
    }
    private float getFloat(String title, JSONObject j) {
        if(j.has(title)) {
            try {
                if(j.getString(title).equals("unknown"))
                    return 0;
                else
                    return (float) j.getDouble(title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public FoodTableEntry(int id, long timestamp, Food food) {
        this.id = id;
        this.timestamp = timestamp;
        this.food = food.getName();
    }
    public FoodTableEntry(int id, long timestamp, String food) {
        this.id = id;
        this.timestamp = timestamp;
        this.food = food;
        this.calories = 0;
        this.protein = 0;
        this.sodium = 0;
        this.URI = null;
    }
    public FoodTableEntry(int id, long timestamp, String food, String URI, String tagline, int amount,
                          float calories, float water, float protein, float lipid, float carb, float fiber, float sugar, float calcium, float iron, float magnesium,
                          float phosphorus, float potassium, float sodium, float zinc, float copper, float manganese, float selenium, float vit_c, float thiamin,
                          float riboflavin, float niacin, float phanto_acid, float vit_b6, float folate, float choline, float vit_b12, float vit_a, float retinol,
                          float alpha_carot, float beta_carot, float beta_crypt, float lycophene, float lutein_zeaxanthin, float vit_e, float vit_d, float vit_k,
                          float saturated_fat, float monosaturated_fat, float polysaturated_fat, float cholesterol) {
        this.id = id;
        this.timestamp = timestamp;
        this.tagline = tagline;
        this.food = food;
        this.amount = amount;
        this.calories = calories;
        this.water = water;
        this.protein = protein;
        this.lipid = lipid;
        this.carb = carb;
        this.fiber = fiber;
        this.sugar = sugar;
        this.calcium = calcium;
        this.iron = iron;
        this.magnesium = magnesium;
        this.phosphorus = phosphorus;
        this.potassium = potassium;
        this.sodium = sodium;
        this.zinc = zinc;
        this.copper = copper;
        this.manganese = manganese;
        this.selenium = selenium;
        this.vit_c = vit_c;
        this.thiamin = thiamin;
        this.riboflavin = riboflavin;
        this.niacin = niacin;
        this.phanto_acid = phanto_acid;
        this.vit_b6 = vit_b6;
        this.folate = folate;
        this.choline = choline;
        this.vit_b12 = vit_b12;
        this.vit_a = vit_a;
        this.retinol = retinol;
        this.alpha_carot = alpha_carot;
        this.beta_carot = beta_carot;
        this.beta_crypt = beta_crypt;
        this.lycophene = lycophene;
        this.lutein_zeaxanthin = lutein_zeaxanthin;
        this.vit_e = vit_e;
        this.vit_d = vit_d;
        this.vit_k = vit_k;
        this.saturated_fat = saturated_fat;
        this.monosaturated_fat = monosaturated_fat;
        this.polysaturated_fat = polysaturated_fat;
        this.choline = cholesterol;
        if(URI != null)
            this.URI = Uri.parse(URI);
        else
            this.URI = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public Uri getURI() {
        return URI;
    }
    public boolean hasUri() {
        return URI != null;
    }

    public void setURI(Uri URI) {
        this.URI = URI;
    }

    public String getSubtitle() {
        //Find highest thing, make that a caption
        if(getLipid() > 20) {
            return "High in fat";
        } else if(getLipid() < 1)
            return "Low in fat";

        if(getCarb() > 30)
            return "High in carbohydrates";
        else if(getCarb() < 1)
            return "Low in carbohydrates";

        if(getProtein() > 25)
            return "High in protein";
        else if(getProtein() < 1)
            return "Low in protein";

        if(getSodium() > 250)
            return "High in sodium";
        else if(getSodium() < 10)
            return "Low in sodium";

        return getLipid() + " grams of fat";
    }

    public float getWater() {
        return water;
    }

    public float getLipid() {
        return lipid;
    }

    public float getCarb() {
        return carb;
    }

    public float getFiber() {
        return fiber;
    }

    public float getSugar() {
        return sugar;
    }

    public float getCalcium() {
        return calcium;
    }

    public float getIron() {
        return iron;
    }

    public float getMagnesium() {
        return magnesium;
    }

    public float getPhosphorus() {
        return phosphorus;
    }

    public float getPotassium() {
        return potassium;
    }

    public float getZinc() {
        return zinc;
    }

    public float getCopper() {
        return copper;
    }

    public float getManganese() {
        return manganese;
    }

    public float getSelenium() {
        return selenium;
    }

    public float getVit_c() {
        return vit_c;
    }

    public float getThiamin() {
        return thiamin;
    }

    public float getRiboflavin() {
        return riboflavin;
    }

    public float getNiacin() {
        return niacin;
    }

    public float getPhanto_acid() {
        return phanto_acid;
    }

    public float getVit_b6() {
        return vit_b6;
    }

    public float getFolate() {
        return folate;
    }

    public float getCholine() {
        return choline;
    }

    public float getVit_b12() {
        return vit_b12;
    }

    public float getVit_a() {
        return vit_a;
    }

    public float getRetinol() {
        return retinol;
    }

    public float getAlpha_carot() {
        return alpha_carot;
    }

    public float getBeta_carot() {
        return beta_carot;
    }

    public float getBeta_crypt() {
        return beta_crypt;
    }

    public float getLycophene() {
        return lycophene;
    }

    public float getLutein_zeaxanthin() {
        return lutein_zeaxanthin;
    }

    public float getVit_e() {
        return vit_e;
    }

    public float getVit_d() {
        return vit_d;
    }

    public float getVit_k() {
        return vit_k;
    }

    public float getSaturated_fat() {
        return saturated_fat;
    }

    public float getMonosaturated_fat() {
        return monosaturated_fat;
    }

    public float getPolysaturated_fat() {
        return polysaturated_fat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public String getTagline() {
        return tagline;
    }

    public int getAmount() {
        return amount;
    }
}
