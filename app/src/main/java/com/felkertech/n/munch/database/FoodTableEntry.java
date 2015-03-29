package com.felkertech.n.munch.database;

import android.net.Uri;

import com.felkertech.n.munch.Objects.Food;

/**
 * Created by N on 3/20/2015.
 */
public class FoodTableEntry {
    private int id;
    private long timestamp;
//    private Food food;
    private String food;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private int sodium;
    private Uri URI;

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
        this.carbs = 0;
        this.fat = 0;
        this.sodium = 0;
        this.URI = null;
    }
    public FoodTableEntry(int id, long timestamp, String food, int calories, int protein, int carbs, int fat, int sodium, String URI) {
        this.id = id;
        this.timestamp = timestamp;
        this.food = food;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.sodium = sodium;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getSodium() {
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
        if(getFat() > 20) {
            return "High in fat";
        } else if(getFat() < 1)
            return "Low in fat";

        if(getCarbs() > 30)
            return "High in carbohydrates";
        else if(getCarbs() < 1)
            return "Low in carbohydrates";

        if(getProtein() > 25)
            return "High in protein";
        else if(getProtein() < 1)
            return "Low in protein";

        if(getSodium() > 250)
            return "High in sodium";
        else if(getSodium() < 10)
            return "Low in sodium";

        return getFat() + " grams of fat";
    }
}
