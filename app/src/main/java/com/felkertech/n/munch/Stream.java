package com.felkertech.n.munch;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.felkertech.n.munch.Activities.ApplicationSettings;
import com.felkertech.n.munch.Activities.CameraPro;
import com.felkertech.n.munch.Activities.FoodEntry;
import com.felkertech.n.munch.Objects.AdviceCard;
import com.felkertech.n.munch.Objects.CalendarItem;
import com.felkertech.n.munch.Objects.DateItem;
import com.felkertech.n.munch.Objects.Hist;
import com.felkertech.n.munch.Objects.HistItem;
import com.felkertech.n.munch.Objects.HistoryItem;
import com.felkertech.n.munch.Objects.StreamItem;
import com.felkertech.n.munch.Objects.StreamPhoto;
import com.felkertech.n.munch.Photography.FroyoAlbumDirFactory;
import com.felkertech.n.munch.Utils.API;
import com.felkertech.n.munch.Utils.AppManager;
import com.felkertech.n.munch.database.FeedReaderDbHelper;
import com.felkertech.n.munch.database.FoodTableEntry;
import com.felkertech.n.munch.services.AlarmReceiver;
import com.felkertech.n.munch.services.NotificationService;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class Stream extends ActionBarActivity {
    Toolbar mToolbar;
    RecyclerView mRecycler;
    RecyclerView.LayoutManager mGridLayout;
    HistoryAdapter mAdapter;
    StreamItem[] mData;
    public static String TAG = "munch::Stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        initToolbar();
        initRecycler();
        initFAB();
        initDrawer();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());


        //Debugging notifications
/*        Intent intent2 = new Intent(this, NotificationService.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra(NotificationService.MEAL_KEY, NotificationService.BREAKFAST);
        startService(intent2);*/

        AlarmReceiver ar = new AlarmReceiver();
        ar.setAlarm(this);
    }
    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        mToolbar.setTitle("My History");
        setSupportActionBar(mToolbar);
    }
    public void initRecycler() {
        mRecycler = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mGridLayout = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mGridLayout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mGridLayout);
        refresh();
    }
    public void initFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Add a new item", Toast.LENGTH_SHORT).show();
                //Open camera
//                Intent i = new Intent(Stream.this, CameraPro.class);
//                startActivity(i);
                gallery_camera_dialog();
            }
        });
        fab.attachToRecyclerView(mRecycler);
    }
    public void initDrawer() {
        AccountHeader ahb = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.banner_supreme_pizza)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withTranslucentStatusBar(true)
                .withAccountHeader(ahb)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("History").withIcon(R.drawable.ic_book_grey600_24dp),
                        new PrimaryDrawerItem().withName("Recommendations").withIcon(R.drawable.ic_inbox_grey600_24dp),
                        new PrimaryDrawerItem().withName("Calendar Heat Map").withIcon(R.drawable.ic_event_note_grey600_24dp),
                        new PrimaryDrawerItem().withName("Gallery").withIcon(R.drawable.ic_image_grey600_24dp),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings_grey600_24dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        handleDrawer(position);
                        return false;
                    }
                })
                .withSelectedItem(0)
                .build();
    }
    public void handleDrawer(int position) {
        switch(position) {
            case 1: //History
                refresh();
                return;
            case 2: //Recommendations
                refreshRecommendations();
                return;
            //0 is a divider
            case 3: //Calendar
                //Might want to make an activity
                //Or use a function to display a layout
                //Shows past weeks on a calendar -- color is darker for nutritious days
                //Tap one will lead you to the history and scroll to that position
                refreshCalendar();
                return;
            case 4: //Gallery
                //Load photos from directory and display them, will go to the foodinfo screen
                //Populate with photolayout
                //Do a refresh operation;
                refreshGallery();
                return;
            case 6: //Settings
//                                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ApplicationSettings.class);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.enter_left, R.anim.do_nothing);
                    startActivity(i, options.toBundle());
                } else
                    startActivity(i);
                return;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Resuming activity, go to drawer item " + (mDrawerCurrentSelection+1));
        handleDrawer(mDrawerCurrentSelection+1);
    }

    public void x() {
        //Looking to build cards
        //Build a demo
        /*final LinearLayout sampleCard = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.history_advice, null);
        //Dismiss/Learn
        sampleCard.findViewById(R.id.button_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleCard.setVisibility(View.GONE);
            }
        });
        sampleCard.findViewById(R.id.button_learn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d = new AlertDialog.Builder(Stream.this)
                        .setTitle(((TextView) sampleCard.findViewById(R.id.title)).getText())
                        .setMessage("Green foods FTW")
                        .create();
                d.show();
            }
        });
        ((LinearLayout) findViewById(R.id.stream)).addView(sampleCard, 0);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insert) {
            Intent i = new Intent(this, FoodEntry.class);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.enter_left, R.anim.do_nothing);
                startActivity(i, options.toBundle());
            } else
                startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Occasionally, the camera will close this activity to run intent.
    //On return nothing is saved in photopath
    int mDrawerCurrentSelection;
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("MCURRENTPHOTOPATH", mCurrentPhotoPath);
        savedInstanceState.putInt("DRAWERCURRENTSELECTION", mDrawerCurrentSelection);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mCurrentPhotoPath = savedInstanceState.getString("MCURRENTPHOTOPATH");
        mDrawerCurrentSelection = savedInstanceState.getInt("DRAWERCURRENTSELECTION");
    }
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B:
                Log.d(TAG, "ACTION_TAKE_PHOTO_B "+resultCode+" == "+RESULT_OK);
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "HandleBigCameraPhoto");
                    Log.d(TAG, mCurrentPhotoPath+"");
                    handleBigCameraPhoto();
                }
                break;
        }
//        overridePendingTransition(R.anim.enter_left, R.anim.exit_right);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void refresh() {
        if(mAdapter != null)
            mAdapter.hideAll();
        mToolbar.setTitle("My History");
        //TODO Come up with ways to generate content
        ArrayList<StreamItem> items = new ArrayList<StreamItem>();
        /*items.add(new AdviceCard("Fruit Salad",
                "Fruit salads are great ways to get vitamins",
                "Red power!",
                R.drawable.banner_n2_fruit_salad));*/
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(wdb);
        ArrayList<FoodTableEntry> entries = mDbHelper.readAll(rdb);
        Date yesterday = new Date(115, 2, 19);
        if(entries.size() == 0) {
            /*mDbHelper.insert(wdb, new FoodTableEntry(1, yesterday.getTime(), "Potato Chips"));
            mDbHelper.insert(wdb, new FoodTableEntry(2, new Date().getTime(), "Chicken"));
            mDbHelper.insert(wdb, new FoodTableEntry(3, new Date().getTime(), "Apple"));*/
            items.add(new AdviceCard("Get Started",
                            "Tap on the camera button or the + to get started",
                            "",
                            R.drawable.banner_n2_fruit_salad));
            Log.d(TAG, "Tough Luck");
        }
        entries = mDbHelper.readAll(rdb);
        Iterator<FoodTableEntry> i = entries.iterator();
        Date header = new Date();
        while(i.hasNext()) {
            FoodTableEntry fte = i.next();
            Date test = new Date();
            test.setTime(fte.getTimestamp());
            Log.d(TAG, test.getDate()+" "+header.getDate());
            if(test.getDate() != header.getDate()) {
                header = test;
                items.add(new DateItem(test));
            }
            items.add(new HistoryItem(fte.getFood(), fte.getSubtitle(), fte.getCalories(), AppManager.getAppropriateIcon(fte.getFood()), fte));
        }

        /*items.add(new HistoryItem("Apple", "High in Fiber", 150, R.drawable.ic_launcher));
        items.add(new HistoryItem("Chicken", "Rich in Protein", 200, R.drawable.ic_launcher));
        items.add(new HistoryItem("Potato Chips", "High in Saturated Fat", 340, R.drawable.ic_launcher));*/
        Log.d(TAG, "Passing "+items.size()+" items into Adapter");
        //Now pass it
        //items.toArray(new StreamItem[items.size()])
        mAdapter = new HistoryAdapter(items, Stream.this);
        mRecycler.setAdapter(mAdapter);
    }
    public void refreshRecommendations() {
        mAdapter.hideAll();
        mToolbar.setTitle("Recommendations");
        //Generate some cards, adapt based on time of day
        final int hour = new Date().getHours();
        final ArrayList<StreamItem> items = new ArrayList<StreamItem>();

        //Create history
        final FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        final SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        final SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(wdb);
        ArrayList<FoodTableEntry> entries = mDbHelper.readAll(rdb);
        Iterator<FoodTableEntry> i = entries.iterator();
        Date header = new Date();
        long todaytime = header.getTime();
        long cutoff = 1000*60*60*24*3; //3 Days
        final Hist h = new Hist();
        while(i.hasNext()) {
            FoodTableEntry fte = i.next();
            Date test = new Date();
            test.setTime(fte.getTimestamp());
            HistItem hi = new HistItem(fte.getId(), fte.getTimestamp(), fte.getAmount());
            Log.d(TAG, hi.toString());
            Log.d(TAG, todaytime+", "+cutoff+", "+test.getTime());
            if(todaytime - test.getTime() < cutoff) {
                h.insert(hi);
            }
        }
        Log.d(TAG, "H: "+h.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, h.toString());
                String URL = API.recommend(getApplicationContext(), h);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(URL);
                HttpParams p = new BasicHttpParams();
                p.setParameter("hist", h.toString());
                try {
                    HttpResponse response = client.execute(request);
                    final String responseBody = EntityUtils.toString(response.getEntity());
                    Log.d(TAG, URL);
                    Log.d(TAG, responseBody);
                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message inputMessage) {
                            try {
                                JSONArray ja = new JSONArray(responseBody);
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject j = ja.getJSONObject(i);
                                    String d = j.getString("desc");
                                    int id = j.getInt("id");
                                    String img = j.getString("img");
                                    String name = j.getString("name");
                                    items.add(new AdviceCard(name, d, img, -1));
                                }
                                Log.d(TAG, "Passing "+items.size()+" items into Adapter");
                                //Now pass it
                                mAdapter = new HistoryAdapter(items, Stream.this);
                                mRecycler.setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 2);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        if(AppManager.isUserAGoat(this))
            items.add(new AdviceCard("Salt Deposits", "Do you have a craving for any minerals?", "", R.drawable.goat));



        //TODO FIGURE OUT WAYS TO GENERATE CONTENT
        /*if(hour > 5 && hour < 11) {
            //BREAKFAST
            items.add(new AdviceCard("Eggs",
                    "Eggs are a great way to get energy in the morning",
                    "Rich in protein",
                    R.drawable.ic_launcher));
        } else if(hour >= 11 && hour < 14) {
            //LUNCH
            items.add(new AdviceCard("Lunch",
                    "Lunch is good. Try eating food.",
                    "Rich in protein",
                    R.drawable.ic_launcher));
        } else if(hour >= 14 && hour < 20) {
            //DINNER
            items.add(new AdviceCard("Dinner",
                    "Dinner is good. Try eating food.",
                    "Rich in protein",
                    R.drawable.ic_launcher));
        } else {
            //LATE-NIGHT
            items.add(new AdviceCard("Sleep",
                    "Late night meals mess up your metabolism",
                    "Studies show that eating late at night affect your ability to sleep and perform the next day",
                    R.drawable.ic_launcher));
        }*/

        Log.d(TAG, "Passing "+items.size()+" items into Adapter");
        //Now pass it
        mAdapter = new HistoryAdapter(items, Stream.this);
        mRecycler.setAdapter(mAdapter);
    }
    public void refreshCalendar() {
        mAdapter.hideAll();
        mToolbar.setTitle("Calendar");
        ArrayList<StreamItem> items = new ArrayList<>();
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();

        mDbHelper.onCreate(wdb);
        ArrayList<FoodTableEntry> entries = mDbHelper.readAll(rdb);
        Iterator<FoodTableEntry> i = entries.iterator();
        Date header = new Date();
        int calories = 0;
        while(i.hasNext()) {
            //Store all the data by date
            FoodTableEntry fte = i.next();
            Date test = new Date();
            test.setTime(fte.getTimestamp());
            calories += fte.getCalories();
            if(test.getDate() != header.getDate()) {
                //New data appears, post
                DateItem d = new DateItem(header);
                header = test;
//                items.add(new DateItem(test));
                items.add(new CalendarItem(d.convertDate(test), calories));
//                items.add(new HistoryItem(fte.getFood(), fte.getSubtitle(), fte.getCalories(), AppManager.getAppropriateIcon(fte.getFood()), fte));
                calories = 0;
            }
        }
        DateItem d = new DateItem(header);
        items.add(new CalendarItem("Before "+d.convertDate(header), calories));
        Log.d(TAG, "Passing "+items.size()+" items into Adapter");
        //Now pass it
        mAdapter = new HistoryAdapter(items, Stream.this);
        mRecycler.setAdapter(mAdapter);
    }
    public void refreshGallery() {
        mAdapter.hideAll();
        mToolbar.setTitle("Gallery");
        ArrayList<StreamItem> items = new ArrayList<StreamItem>();
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
        //FIXME Should not create database all the time as it resets
        mDbHelper.onCreate(wdb);
        ArrayList<FoodTableEntry> entries = mDbHelper.readAll(rdb);
        Iterator<FoodTableEntry> i = entries.iterator();
        Date header = new Date();
        while(i.hasNext()) {
            FoodTableEntry fte = i.next();
            items.add(new StreamPhoto(fte));
        }
        Log.d(TAG, "Passing "+items.size()+" items into Adapter");
        //Now pass it
        mAdapter = new HistoryAdapter(items, Stream.this);
        mRecycler.setAdapter(mAdapter);
    }
    public Drawable gDraw(int draw) {
        return getResources().getDrawable(draw);
    }
    public Drawable gDraw(String draw) {
        Context context = getApplicationContext();
        int id = context.getResources().getIdentifier("picture0001", "drawable", context.getPackageName());
        return getResources().getDrawable(id);
    }
    public int gDrawId(String draw) {
        Context context = getApplicationContext();
        int def = R.drawable.ic_launcher;
        int id = context.getResources().getIdentifier(draw, "drawable", context.getPackageName());
        //Return R.drawable.ic_launcher if cannot be found
        if(id == 0) {
            return def;
        }
        return id;
    }

    /*** CAMERA MANAGER ***/
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;
    private String mCurrentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "PHOTO_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    FroyoAlbumDirFactory mAlbumStorageDirFactory;

    public void gallery_camera_dialog() {
        Log.d(TAG, "Opening camera");
        if (AppManager.isIntentAvailable(getApplicationContext(), MediaStore.ACTION_IMAGE_CAPTURE))
            dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }
    private void dispatchTakePictureIntent(int actionCode) {
        mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            switch(actionCode) {
                case ACTION_TAKE_PHOTO_B:
                    File f = null;
                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        Log.d(TAG, mCurrentPhotoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                        Log.d(TAG, e.getMessage()+" Photo Not Saved");
                        Toast.makeText(this, "Photo not saved: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            } // switch
        } catch(Exception e) {
            Toast.makeText(this, "Camera broke: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "opening cam");
        startActivityForResult(takePictureIntent, actionCode);
    }
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
//        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }
    private void handleBigCameraPhoto() {
        Log.d(TAG, "Handle");
        try {
            Log.d(TAG, "handleBigCameraPhoto " + mCurrentPhotoPath.toString());
        } catch(Exception e) {
            Log.d(TAG, e.getMessage()+" ");
            return;
        }
        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        Intent upload = new Intent(this, FoodEntry.class);
        upload.putExtra("FOOD_PHOTO", mCurrentPhotoPath.toString());
        Log.d(TAG, mCurrentPhotoPath);
        startActivity(upload);
        this.sendBroadcast(mediaScanIntent);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        Toast.makeText(this, "Cannot create directory", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
            Toast.makeText(this, "Cannot READ/WRITE to external storage.", Toast.LENGTH_SHORT).show();
        }
        return storageDir;
    }
    private String getAlbumName() {
        return "Munch";
    }
}
