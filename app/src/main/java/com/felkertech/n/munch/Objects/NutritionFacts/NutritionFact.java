package com.felkertech.n.munch.Objects.NutritionFacts;

/**
 * Created by N on 3/7/2015.
 */
public class NutritionFact {
    public String key;
    public String value;

    public NutritionFact(String k, String v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
