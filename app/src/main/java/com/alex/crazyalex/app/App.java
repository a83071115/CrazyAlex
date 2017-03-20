package com.alex.crazyalex.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Administrator on 2017/3/19.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(getSharedPreferences("user_settings",MODE_PRIVATE).getInt("theme",0)==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
