package net.callofdroidy.myclock;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import java.nio.channels.Pipe;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityMain extends AppCompatActivity {

    private TextClock textClock;
    private ArrayAdapter adapterTimestamp;
    private ArrayAdapter adapterLocation;
    private ArrayList<String> timestamps;
    private ListView listView_location;
    private ListView listView_timestamp;
    private SharedPreferences spTimestamps;
    private String[] locations;
    private String currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init locations data set
        locations = getResources().getStringArray(R.array.locations);

        //init timestamp data set
        timestamps = new ArrayList<>();

        spTimestamps = getApplication().getSharedPreferences("timestamps.sp", 0);
        for(String loc: locations){
            timestamps.add(spTimestamps.getString(loc, "Null"));
        }

        textClock = (TextClock) findViewById(R.id.clock_text);
        listView_location = (ListView) findViewById(R.id.listView_loc);
        listView_timestamp = (ListView) findViewById(R.id.listView_time);

        adapterLocation = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, locations){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = 150;
                view.setLayoutParams(params);
                return view;
            }
        };

        adapterTimestamp = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, timestamps){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = 150;
                view.setLayoutParams(params);
                return view;
            }
        };

        listView_location.setAdapter(adapterLocation);
        listView_timestamp.setAdapter(adapterTimestamp);

        listView_location.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return true;
            }
        });

        listView_timestamp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentTimeStamp = new SimpleDateFormat("kk:mm:ss", Locale.CANADA).format(new Date().getTime());
                Log.e("add timestamp", currentTimeStamp);
                timestamps.set(position, currentTimeStamp);
                adapterTimestamp.notifyDataSetChanged();
                spTimestamps.edit().putString((String) adapterLocation.getItem(position), currentTimeStamp).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.backup) {
            Intent intent = new Intent(ActivityMain.this, ActivityBackup.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
