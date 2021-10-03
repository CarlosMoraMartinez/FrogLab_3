package com.cmora.froglab_2;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
//This activity shows total scores; does not use PointsFragment, which is used during games
public class PointsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PointsActivity", "OnCreate 1");
        setContentView(R.layout.points_layout);
        Animation points_anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.points_activity_icon);
        ImageView iv1 = findViewById(R.id.icon_points_bastard);
        ImageView iv2 = findViewById(R.id.icon_points_punnet);
        ImageView iv3 = findViewById(R.id.icon_points_total);
        iv1.setAnimation(points_anim);
        iv2.setAnimation(points_anim);
        iv3.setAnimation(points_anim);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int points_bastard = pref.getInt("points_bastard", 0);
        int points_punnet = pref.getInt("points_punnet", 0);
        int points_total = points_bastard + points_punnet;
        TextView tv1 = findViewById(R.id.text_points_bastard_num);
        TextView tv2 = findViewById(R.id.text_points_punnet_num);
        TextView tv3 = findViewById(R.id.text_points_total_num);
        tv1.setText(Integer.toString(points_bastard));
        tv2.setText(Integer.toString(points_punnet));
        tv3.setText(Integer.toString(points_total));

    }
}