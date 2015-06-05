
package com.felkertech.n.munch.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Utils.API;
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

public class FoodEntry extends AppCompatActivity {
    private AutoCompleteTextView entry;
    private FloatingActionButton fab;
    private ImageView photo;
    private String photoUri = new String();
    private String photoUrl = new String();
    private static String TAG = "munch::FoodEntry";
    FoodSuggestionsAdapter adapter;
    private SuggestionsReceiver receiver;
    private FoodEntry self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);
        entry = (AutoCompleteTextView) findViewById(R.id.food_type);
        adapter = new FoodSuggestionsAdapter(this, android.R.layout.simple_dropdown_item_1line);
        entry.setAdapter(adapter);
        self = this;
        photoUri = "";

        entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if(photoUri.isEmpty())
                    new Thread(new SuggestionsReceiver(self, s.toString())).start();

            }
        });
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

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
                //Set URI flag if the file already exists locally
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

                float amount = 0;
                String amountString = ((TextView) findViewById(R.id.food_amount)).getText().toString();
                if(!amountString.isEmpty())
                    amount = Float.parseFloat(amountString);
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
                final float finalAmount = amount;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Search - convert food name into an id no.
                        int id = getIdForFilteredString(entry.getText().toString());
                        if(id == -1)
                            return;
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
                            photoUrl = fte.getURI().toString();
                            if(!photoUri.isEmpty())
                                fte.setURI(Uri.parse(photoUri));
                            Log.d(TAG, "INSERTING PHOTO "+fte.getURI().toString());
                            mDbHelper.insert(wdb, fte);
//                            Log.d(TAG, "DB: " + mDbHelper.readAll(rdb).size() + "  " + mDbHelper.readAll(rdb).get(0).getFood());

                            Handler mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message inputMessage) {
                                    Log.d(TAG, "Returning");
                                    Log.d(TAG, mDbHelper.readAll(rdb).size()+"");
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
        if(food.isEmpty() && photoUri.isEmpty() && photoUrl.isEmpty()) {
            photo.setVisibility(View.INVISIBLE);
        } else if(!photoUrl.isEmpty()) {
            photo.setVisibility(View.VISIBLE);
            Log.d(TAG, "PhotoSet " + photoUrl);
            Ion.with(photo).load(photoUrl);
        } else if(!photoUri.isEmpty()) {
            photo.setVisibility(View.VISIBLE);
            Log.d(TAG, "PhotoSet " + Uri.parse(photoUri).toString());
            photo.setImageURI(Uri.parse(photoUri));
            //Load via Ion

        } else if(!food.isEmpty()) {

        }
        Log.d(TAG, food+" "+food.isEmpty()+" "+photoUri+" "+photoUrl);
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
    //FIXME TODO
    public int getIdForFilteredString(String food) {
        int i = 0;
        for(String f: adapter.mData) {
            if(f.equals(food)) {
                return adapter.ids.get(i);
            }
            i++;
        }
        return -1;
    }
    private class SuggestionsReceiver implements Runnable {
        FoodEntry mActivity;
        String s;
        public SuggestionsReceiver(FoodEntry activity, String su) {
            mActivity = activity;
            s = su;
        }

        @Override
        public void run() {
            Log.d(TAG, "Big Hero 6");
            int id = getIdForFilteredString(s.toString());
            if(id == -1)
                return;
            String URL = API.info(id, 1);
            try {
                Log.d(TAG, URL);
                HttpClient client = new DefaultHttpClient();
                Log.d(TAG, "Client");
                HttpGet request = new HttpGet(URL);
                Log.d(TAG, "Request");
                HttpResponse response = client.execute(request);
                Log.d(TAG, "Response");
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.d(TAG, responseBody);
                FoodTableEntry f = new FoodTableEntry(responseBody);
                photoUrl = f.getURI().toString();
                photo.post(new Runnable() {
                    public void run() {
                        displayPhotoview();
                    }
                });
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}