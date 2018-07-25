package com.example.viveksharma.navigationdrawer;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ListView lv;
    private ActionBarDrawerToggle toggle;
    private List<String> myList;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myList = new ArrayList<String>();
        for(int i=0;i<20;i++){
            myList.add("Item"+i);
        }
        Log.d(TAG, "onCreate: "+myList);
        dl = (DrawerLayout)findViewById(R.id.drawerlayout);
        lv = (ListView)findViewById(R.id.drawerlistview);
        lv.setAdapter(new ArrayAdapter<>(this,R.layout.drawerlistitem,myList));
        // called for selection
        lv.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );
        // Create Toggle
        toggle = new ActionBarDrawerToggle(this,dl,R.string.drawer_open,R.string.drawer_close);


        // Draws the menu button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.syncState();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate: ");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);

    }

    // Will be called when menu icon is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: "+"In");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position){
        ((TextView) findViewById(R.id.textview)).setText("You selected " + myList.get(position));
        dl.closeDrawer(lv);
    }


}
