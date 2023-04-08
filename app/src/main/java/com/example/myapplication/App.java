package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.myapplication.data.db.TasksDatabase;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
