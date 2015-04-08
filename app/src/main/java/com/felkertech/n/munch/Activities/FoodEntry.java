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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Utils.API;
import com.felkertech.n.munch.Utils.Constants;
import com.felkertech.n.munch.Utils.FoodSuggestionsAdapter;
import com.felkertech.n.munch.database.FeedReaderDbHelper;
import com.felkertech.n.munch.database.FoodTableEntry;
import com.koushikdutta.ion.Ion;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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

        entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO get photo
                displayPhotoview();
            }
        });

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
                final FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
                final SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
                final SQLiteDatabase wdb = mDbHelper.getWritableDatabase();

                int amount = Integer.parseInt(((TextView) findViewById(R.id.food_amount)).getText().toString());
                String units = ((Spinner) findViewById(R.id.food_units)).getSelectedItem().toString();
                if(units.equals("Cups")) {
                    amount *= 227;
                } else if(units.equals("Ounces")) {
                    amount *= 30;
                } else if(units.equals("Pounds")) {
                    amount *= 450;
                } else if(units.equals("Pints")) {
                    amount *= 2*227;
                } else if(units.equals("Gallons")) {
                    amount *= 8*227;
                } else if(units.equals("Liters")) {
                    amount /= 1000;
                }

                //Populate data through researching it
                final int finalAmount = amount;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Search - convert food name into an id no.
                        //FIXME
                        int id = Integer.parseInt("23326");
                        String URL = API.info(id, finalAmount);
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet(URL);
                        try {
                            HttpResponse response = client.execute(request);
                            String responseBody = EntityUtils.toString(response.getEntity());
                            Log.d(TAG, URL);
                            Log.d(TAG, responseBody);
                            //Now parse
                            FoodTableEntry fte = new FoodTableEntry(responseBody);
                            photoUri = fte.getURI().toString();
                            mDbHelper.insert(wdb, fte);
                            Log.d(TAG, "DB: " + mDbHelper.readAll(rdb).size() + "  " + mDbHelper.readAll(rdb).get(0).getFood());

                            Handler mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message inputMessage) {
                                    finish();
                                }
                            };
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplication(), "URL Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
                //TODO Loader
                Toast.makeText(getApplication(), "Adding data...", Toast.LENGTH_SHORT).show();
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
    public void finish() {
        super.finish();
        Log.d(TAG, "Destroying foodntry");
        overridePendingTransition(R.anim.do_nothing, R.anim.exit_right);
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
            Log.d(TAG, "PhotoSet " + Uri.parse(photoUri).toString());
            Ion.with(photo).load(Uri.parse(photoUri).toString());
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
    public int getIdForFiltereedString(String food) {
        for(String )
    }
}
