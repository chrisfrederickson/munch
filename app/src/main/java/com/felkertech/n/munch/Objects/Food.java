package com.felkertech.n.munch.Objects;

import com.felkertech.n.munch.Objects.NutritionFacts.NutritionFact;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by N on 3/7/2015.
 */
public class Food {
    public String name;
    public ArrayList<NutritionFact> nutritionFacts;
    public String hint;

    public Food(String name, String hint, ArrayList<NutritionFact> facts) {
        this.name = name;
        this.hint = hint;
        this.nutritionFacts = facts;
    }

    public String getName() {
        return name;
    }

    public ArrayList<NutritionFact> getNutritionFacts() {
        return nutritionFacts;
    }

    public String getHint() {
        return hint;
    }
}
