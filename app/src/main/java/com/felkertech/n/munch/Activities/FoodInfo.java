package com.felkertech.n.munch.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Utils.AppManager;
import com.felkertech.n.munch.database.FeedReaderContract;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FoodInfo extends ActionBarActivity {
    private String mFood;
    private Toolbar mToolbar;
    private String photoUri = "";

    private static String TAG = "munch::FoodInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
//        ((Toolbar) findViewById(R.id.my_awesome_toolbar)).setAlpha(0);
//        mToolbar.setBackgroundColor(getResources().getColor(R.color.pink_500));
//        mToolbar.getBackground().setAlpha(0);


        Intent i = getIntent();
        if(!(i == null)) {
            //Receive food, look up its details, then modify
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM)) {
                mFood = i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM);
                ((TextView) findViewById(R.id.title)).setText(mFood);
                Drawable mDrawable = getResources().getDrawable(AppManager.getAppropriateIcon(mFood));
                int color2 = Color.parseColor("#FFFFFF");
                PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
                mDrawable.setColorFilter(color2, mMode);
                ((ImageView) findViewById(R.id.food_icon)).setImageDrawable(mDrawable);

            } if(i.hasExtra("FOOD_TYPE")) { //For things like 'Green Vegetables'
                mFood = i.getStringExtra("FOOD_TYPE");
                if(mFood.equals("Salt Deposits")) {
                    photoUri = "http://i0.kym-cdn.com/entries/icons/original/000/017/028/goaty1.PNG";
                    /*Ion.with((ImageView) findViewById(R.id.hero))
                            .load(photoUri);*/
                    findViewById(R.id.nutrition).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.descriptor)).setText("'Alpine Ibexes climb nearly 90 degree angles to lick salt deposits of of mountainsides. " +
                            "They crave that mineral.' (Source)\n#world");
                } else if(mFood.equals("Get Started")) {
                    photoUri = "http://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Fruktsallad_%28Fruit_salad%29.jpg/1280px-Fruktsallad_%28Fruit_salad%29.jpg";
                    /*Ion.with((ImageView) findViewById(R.id.hero))
                            .load(photoUri);*/
                    findViewById(R.id.nutrition).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.descriptor)).setText("Tap on the camera icon or the + icon to add a new food to your history.");
                }
                else {
                    //TODO Lookup food via string
                }

                ((TextView) findViewById(R.id.title)).setText(mFood);
                findViewById(R.id.descriptor).setBackgroundColor(getResources().getColor(android.R.color.white));
                findViewById(R.id.nutrition).setVisibility(View.GONE);
                findViewById(R.id.food_icon).setVisibility(View.GONE);
            }
            if(i.hasExtra("FOOD_PHOTO")) {
                photoUri = i.getStringExtra("FOOD_PHOTO");
            }

            //Do stuff if you receive food info with intent
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES)) {
                ((TextView) findViewById(R.id.food_calories)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES, 0)+"");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN)) {
                ((TextView) findViewById(R.id.food_protein)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM)) {
                ((TextView) findViewById(R.id.food_sodium)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM, 0)+"mg");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID)) {
                ((TextView) findViewById(R.id.food_fat)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CARB)) {
                ((TextView) findViewById(R.id.food_carbs)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CARB, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR)) {
                ((TextView) findViewById(R.id.food_sugar)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_WATER)) {
                ((TextView) findViewById(R.id.food_water)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_WATER, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER)) {
                ((TextView) findViewById(R.id.food_fiber)).setText(i.getFloatExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER, 0)+"g");
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI)) { //Will it work if value assigned but null?
                photoUri = i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI);
                Log.d(TAG, "Has image");
            } else {
                //TODO IMAGES
            }
            if(i.hasExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE)) {
                ((TextView) findViewById(R.id.descriptor)).setText(i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE));
            }
            if(!photoUri.isEmpty()) {
                try {
                        Log.d(TAG, "Load "+i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI)+" or "+photoUri);
                        Ion.with(((ImageView) findViewById(R.id.hero)))
                                .load(photoUri)
                                .setCallback(new FutureCallback<ImageView>() {
                                    @Override
                                    public void onCompleted(Exception e, ImageView result) {
                                        Palette.generateAsync(getPhoto(), new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch descriptor = palette.getVibrantSwatch();
                                                for(Palette.Swatch p: palette.getSwatches()) {
                                                    Log.d(TAG, p.getPopulation()+" populations");
//                                                    Log.d(TAG, p.getBodyTextColor()+"");
                                                    Log.d(TAG, palette.getVibrantColor(0xffffff)+"-");
                                                }
//                                                findViewById(R.id.descriptor).setBackgroundColor(palette.getVibrantColor(0xffffff));
                                                Log.d(TAG, palette.getSwatches().toArray().toString());
                                                Palette.Swatch titles = palette.getVibrantSwatch();
                                                if(titles == null)
                                                    titles = palette.getDarkVibrantSwatch();
                                                if(titles == null)
                                                    titles = palette.getLightVibrantSwatch();
                                                if(titles != null && false) {
                                                    Log.d(TAG, "Set title color " + titles);
                                                    Log.d(TAG, "For " + findViewById(R.id.descriptor) + " palette " + titles);
                                                    ((TextView) findViewById(R.id.descriptor)).setTextColor(titles.getBodyTextColor());
                                                    ((TextView) findViewById(R.id.title)).setTextColor(titles.getTitleTextColor());
                                                    Drawable mDrawable = getResources().getDrawable(AppManager.getAppropriateIcon(mFood));
                                /*mDrawable.setColorFilter(new
                                        PorterDuffColorFilter(0xffffff00, PorterDuff.Mode.DST));*/
                                                    //Via http://stackoverflow.com/questions/9643603/modifying-the-color-of-an-android-drawable
                                                    int color2 = titles.getTitleTextColor();
                                                    PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
                                                    mDrawable.setColorFilter(color2, mMode);

                                                    ((ImageView) findViewById(R.id.food_icon)).setImageDrawable(mDrawable);
                                                } else {
                                                    Drawable mDrawable = getResources().getDrawable(AppManager.getAppropriateIcon(mFood));
                                                    int color2 = Color.parseColor("#FFFFFF");
                                                    PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
                                                    mDrawable.setColorFilter(color2, mMode);
                                                    ((ImageView) findViewById(R.id.food_icon)).setImageDrawable(mDrawable);
                                                }
                                            }
                                        });
                                    }
                                });
//                        ((ImageView) findViewById(R.id.hero)).setImageURI(Uri.parse(i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI)));
                        photoUri = i.getStringExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI);

                } catch(Exception e) {

                }
            }
        } else {
            //TODO IMAGES
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mToolbar.setTitle("");
        getMenuInflater().inflate(R.menu.menu_food_info, menu);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        Log.d(TAG, "Destroying foodinfo");
        overridePendingTransition(R.anim.do_nothing, R.anim.exit_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_edit:
                edit();
                return true;
            case R.id.action_share:
                share();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void goBack(View view) {
//        NavUtils.navigateUpFromSameTask(this);
        finish();
//        return;
        /*Intent i = NavUtils.getParentActivityIntent(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.enter_left, R.anim.do_nothing);
            startActivity(i, options.toBundle());
        } else
            startActivity(i);*/
    }
    private void share() {
        //TODO Create share intent, pass some info along
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Log.d(TAG, getPhotoUri().toString());
        share.putExtra(Intent.EXTRA_STREAM, getPhotoUri());
        startActivity(Intent.createChooser(share, "Share Image"));
    }
    private void edit() {
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(getPhotoUri(), "image/*");
        editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d(TAG, getPhotoUri().toString());
        startActivity(Intent.createChooser(editIntent, null));
    }
    /**
     * Turns imageview into bitmap, with whatever value it has
     * @return Bitmap representation of imageview contents
     */
    public Bitmap getPhoto() {
        ImageView photo = (ImageView) findViewById(R.id.hero);
        photo.buildDrawingCache();
        return photo.getDrawingCache();
    }

    /**
     * Saves a bitmap locally and retrieves the URI
     * @return Uri of photo
     */
    public Uri getPhotoUri() {
        if(!photoUri.isEmpty()) {
            if(photoUri.contains("file:"))
                return Uri.parse(photoUri);
            return Uri.parse("file:///"+photoUri);
        }
        Log.d(TAG, "PhotoBitmap " + getPhoto());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        getPhoto().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO Improve the file structure
        Log.d(TAG, "file:///"+Environment.getExternalStorageDirectory().getPath()+"/temporary_file.jpg");
        return Uri.parse("file:///"+Environment.getExternalStorageDirectory().getPath()+"/temporary_file.jpg");
    }
}