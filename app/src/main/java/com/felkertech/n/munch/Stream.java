package com.felkertech.n.munch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.felkertech.n.munch.Objects.AdviceCard;
import com.felkertech.n.munch.Objects.HistoryItem;
import com.felkertech.n.munch.Objects.StreamItem;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Stream extends ActionBarActivity {
    Toolbar mToolbar;
    RecyclerView mRecycler;
    RecyclerView.LayoutManager mGridLayout;
    RecyclerView.Adapter mAdapter;
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
                Toast.makeText(getApplicationContext(), "Add a new item", Toast.LENGTH_SHORT).show();
            }
        });
        fab.attachToRecyclerView(mRecycler);
    }
    public void initDrawer() {
        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_launcher)
                .addProfiles(
                        /*new ProfileDrawerItem().withName("Me").withEmail("").withIcon(getResources().getDrawable(R.drawable.ic_launcher))*/
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                    }
                })
                .build();

        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("History").withIcon(R.drawable.ic_launcher),
                        new PrimaryDrawerItem().withName("Recommendations").withIcon(R.drawable.ic_launcher),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_launcher)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch(position) {
                            case 0: //History
                                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                                return;
                            //1 is a divider
                            case 2: //Settings
                                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                                return;
                            case 3: //About
                                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                                return;
                        }
                    }
                })
                .build();
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
        //getMenuInflater().inflate(R.menu.menu_stream, menu);
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
    public void refresh() {
        //Come up with ways to generate content
        ArrayList<StreamItem> items = new ArrayList<StreamItem>();
        items.add(new AdviceCard("Red Vegetables",
                "Have red vegetables like lettuce or broccoli for lunch in order to improve your potassium levels",
                "Red power!",
                R.drawable.ic_launcher));
        items.add(new HistoryItem("Apple", "High in Fiber", 150, R.drawable.ic_launcher));
        items.add(new HistoryItem("Chicken", "Rich in Protein", 200, R.drawable.ic_launcher));
        items.add(new HistoryItem("Potato Chips", "High in Saturated Fat", 340, R.drawable.ic_launcher));
        Log.d(TAG, "Passing "+items.size()+" items into Adapter");
        //Now pass it
        //items.toArray(new StreamItem[items.size()])
        mAdapter = new HistoryAdapter(items, Stream.this);
        mRecycler.setAdapter(mAdapter);
    }
    public Drawable gDraw(int draw) {
        return getResources().getDrawable(draw);
    }
}
