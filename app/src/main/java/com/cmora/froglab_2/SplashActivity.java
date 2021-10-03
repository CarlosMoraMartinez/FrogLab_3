package com.cmora.froglab_2;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends Activity {

    Handler handler;
    SoundPool soundPool;
    int croack1, croack2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inSpain = mFirebaseRemoteConfig.getString("in_spain");
        //Log.d("CARLOS", "SplashActivity, InSpain: " + Comun.inSpain);
        Log.d("CARLOS", "SplashActivity, onCreate");
        setContentView(R.layout.splashfile);
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        croack1 = soundPool.load(this, R.raw.croack_female, 0);
        croack2 = soundPool.load(this, R.raw.croack_male, 0);


        Log.d("CARLOS", "SplashActivity, using handler");
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(croack1, 3, 1, 1, 0, 1);
                soundPool.play(croack2, 1, 4, 1, 0, 1);
                Intent intent=new Intent(com.cmora.froglab_2.SplashActivity.this,MainActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.menguar2, R.anim.menguar2);
                /*Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.menguar);
                anim.setAnimationListener(SplashActivity.this);
                findViewById(R.layout.splashfile).startAnimation(anim);*/
                finish();
                Log.d("CARLOS", "SplashActivity, end of run");
            }
        },4000);
        Log.d("CARLOS", "SplashActivity, end onCreate");
    }

}