package com.cmora.froglab_2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;

public class PreferencesActivity extends AppCompatActivity {
    private GenomeList genome_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("PREFERENCEACTIVITY", "Inside preferences");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            genome_list = (GenomeList) extras.getSerializable("genome_list");
        }

        for(int i = 0; i < genome_list.length(); i++){
            Log.d("PREFERENCEACTIVITY", genome_list.get(i).toString());
        }


        PreferencesFragment frag = new PreferencesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, frag)
                .commit();

        //lp.setValue("Example");
    }
}