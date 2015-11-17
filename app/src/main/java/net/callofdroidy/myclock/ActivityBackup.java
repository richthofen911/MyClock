package net.callofdroidy.myclock;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ActivityBackup extends AppCompatActivity {

    ListView listView_profiles;
    ArrayAdapter adapterProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        //get the Dir of all SharedPreferences files and show a list
        File fileDir = new File(getApplicationInfo().dataDir, "shared_prefs");
        if(fileDir.exists() && fileDir.isDirectory()){
            String[] fileList = fileDir.list();
            adapterProfile = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, fileList);
        }
        listView_profiles = (ListView) findViewById(R.id.listview_profile_history);
        listView_profiles.setAdapter(adapterProfile);

        findViewById(R.id.btn_backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveThisProfile(SharedPreferences currentProfile){
        String profileName = new SimpleDateFormat("kk:mm:ss", Locale.CANADA).format(new Date().getTime());
        SharedPreferences newProfile = getApplication().getSharedPreferences(profileName, 0);
        SharedPreferences.Editor newProfileEditor = newProfile.edit();
        for (Map.Entry<String, ?> entry: currentProfile.getAll().entrySet()){
            newProfileEditor.putString(entry.getKey(), (String) entry.getValue());
        }
    }
}
