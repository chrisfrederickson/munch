package com.felkertech.n.munch.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Utils.FoodSuggestionsAdapter;
import com.felkertech.n.munch.database.FeedReaderDbHelper;
import com.felkertech.n.munch.database.FoodTableEntry;
import com.melnykov.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FoodEntry extends ActionBarActivity {
    private AutoCompleteTextView entry;
    private FloatingActionButton fab;
    private ImageView photo;
    private String photoUri;
    private static String TAG = "munch::FoodEntry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);
        entry = (AutoCompleteTextView) findViewById(R.id.food_type);
        FoodSuggestionsAdapter adapter = new FoodSuggestionsAdapter(this, android.R.layout.simple_dropdown_item_1line);
        entry.setAdapter(adapter);

        photo = (ImageView) findViewById(R.id.photo_taken);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        mToolbar.setTitle("Add Food");
        setSupportActionBar(mToolbar);

        Intent i = getIntent();
        if(i != null) {
            //Look for things
            if(i.hasExtra(getString(R.string.intent_food_name))) {
                entry.setText(getString(R.string.intent_food_name));
                displayPhotoview();
            }
            if(i.hasExtra(getString(R.string.intent_food_photo))) {
                //FIXME Set URI flag if the file already exists locally
                photoUri = i.getStringExtra(getString(R.string.intent_food_photo));
                Log.d(TAG, "Got photo " + photoUri);
                displayPhotoview();
            } else {
                photo.setVisibility(View.INVISIBLE);
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get nutritional info from server
                //Submit to database
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
                SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
                SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
                //TODO Populate data through researching it
                mDbHelper.insert(wdb, new FoodTableEntry(mDbHelper.readAll(rdb).size(), new Date().getTime(), entry.getText().toString(), 700, 700, 700, 700, 700, getPhotoUri().toString()));
                Log.d(TAG, "DB: " + mDbHelper.readAll(rdb).size() + "  " + mDbHelper.readAll(rdb).get(0).getFood());
                displayPhotoview();
                Handler mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message inputMessage) {
                        finish();
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000);
                //TODO Loader
            }
        });

        View edit_image = findViewById(R.id.image_edit);
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Intent.ACTION_EDIT);
                editIntent.setDataAndType(getPhotoUri(), "image/*");
                editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.d(TAG, getPhotoUri().toString());
                startActivity(Intent.createChooser(editIntent, null));
            }
        });
        View share_image = findViewById(R.id.image_share);
        share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, getPhotoUri());
                startActivity(Intent.createChooser(share, "Share Image"));

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_food_entry, menu);
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

    public void displayPhotoview() {
        //Display photo in photoview
        String food = entry.getText().toString();
        Log.d(TAG, food+" "+food.isEmpty()+" "+photoUri);
        if(food.isEmpty() && photoUri.isEmpty()) {
            photo.setVisibility(View.INVISIBLE);
        } else if(!photoUri.isEmpty()) {
            photo.setVisibility(View.VISIBLE);
            Log.d(TAG, "PhotoSet "+Uri.parse(photoUri).toString());
            photo.setImageURI(Uri.parse(photoUri));
        } else if(!food.isEmpty()) {
            //Load via Ion
        }
    }

    /**
     * Turns imageview into bitmap, with whatever value it has
     * @return Bitmap representation of imageview contents
     */
    public Bitmap getPhoto() {
        photo.buildDrawingCache();
        return photo.getDrawingCache();
    }

    /**
     * Saves a bitmap locally and retrieves the URI
     * @return Uri of photo
     */
    public Uri getPhotoUri() {
        if(!photoUri.isEmpty()) {
            return Uri.parse("file:///"+photoUri);
        }
        if(entry.getText().toString().isEmpty())
            return null;
        Log.d(TAG, "PhotoBitmap "+getPhoto());
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
