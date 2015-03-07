package com.felkertech.n.munch.Activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.felkertech.n.munch.R;

public class FoodInfo extends ActionBarActivity {
    private String mFood;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
//        ((Toolbar) findViewById(R.id.my_awesome_toolbar)).setAlpha(0);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.pink_500));
        mToolbar.getBackground().setAlpha(0);
        Intent i = getIntent();
        if(!i.equals(null)) {
            //Receive food, look up its details, then modify
            if(i.hasExtra("FOOD_NAME")) {
                mFood = i.getStringExtra("FOOD_NAME");
                ((TextView) findViewById(R.id.title)).setText(mFood);
            } else if(i.hasExtra("FOOD_TYPE")) { //For things like 'Green Vegetables'
                mFood = i.getStringExtra("FOOD_TYPE");
                ((TextView) findViewById(R.id.title)).setText(mFood);
                findViewById(R.id.descriptor).setBackgroundColor(getResources().getColor(android.R.color.white));
                findViewById(R.id.nutrition).setVisibility(View.GONE);
                findViewById(R.id.food_icon).setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mToolbar.setTitle("");
        //getMenuInflater().inflate(R.menu.menu_food_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void goBack(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }
}
