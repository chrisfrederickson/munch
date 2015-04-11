package com.felkertech.n.munch.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.felkertech.n.munch.R;

import java.util.List;

/**
 * Created by N on 3/28/2015.
 */
public abstract class AppManager {
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    public static boolean isUserAGoat(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.coffeestainstudios.goatsimulator", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    public static int getAppropriateIcon(String name) {
        if(name == null)
            name = "";
        name = name.toLowerCase();
        if(name.contains("pineapple"))
            return R.drawable.pineapple;
        if(name.contains("apple"))
            return R.drawable.apple55;
        if(name.contains("turkey") || name.contains("chicken"))
            return R.drawable.turkey7;
        if(name.contains("beef") || name.contains("steak") || name.contains("lamb"))
            return R.drawable.steak;
        if(name.contains("tea")) {
            return R.drawable.tea24;
        }
        if(name.contains("cake") || name.contains("pie")) {
            return R.drawable.sweet9;
        }
        if(name.contains("sushi")) {
            return R.drawable.sushi3;
        }
        if(name.contains("coke") || name.contains("soda") || name.contains("coffee"))
            return R.drawable.soda7;
        if(name.contains("omlette") || name.contains("egg"))
            return R.drawable.silhouette81;
        if(name.contains("sandwich"))
            return R.drawable.sandwich;
        if(name.contains("pizza"))
            return R.drawable.pizza3;
        if(name.contains("lemon"))
            return R.drawable.lemon10;
        if(name.contains("dog"))
            return R.drawable.hot33;
        if(name.contains("carrot"))
            return R.drawable.healthy_food5;
        if(name.contains("banana"))
            return R.drawable.healthy_food4;
        if(name.contains("burger"))
            return R.drawable.hamburger;
        if(name.contains("grape"))
            return R.drawable.grapes1;
        if(name.contains("pear"))
            return R.drawable.fruit51;
        if(name.contains("orange"))
            return R.drawable.fruit42;
        if(name.contains("milk"))
            return R.drawable.fresh7;
        if(name.contains("fish"))
            return R.drawable.fish52;
        if(name.contains("vegetable") || name.contains("broccoli"))
            return R.drawable.broccoli;
        if(name.contains("bread"))
            return R.drawable.bread14;
        if(name.contains("drink") || name.contains("water"))
            return R.drawable.drink81;
        if(name.contains("beer"))
            return R.drawable.drink24;
        if(name.contains("ramen") || name.contains("rice") || name.contains("noodle"))
            return R.drawable.chinese_food1;
        return R.drawable.plants6;
    }
}
