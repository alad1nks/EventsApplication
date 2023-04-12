package com.example.myapplication.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPreferenceImpl implements AppPreference {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AppPreferenceImpl(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }


    @Override
    public Boolean loggedIn() {
        return sharedPreferences.getBoolean("loggedIn", false);
    }

    @Override
    public void logIn() {
        editor.putBoolean("loggedIn", true).apply();
    }

    @Override
    public void saveName(String name) {
        editor.putString("name", name).apply();
    }

    @Override
    public String getName() {
        return sharedPreferences.getString("name", "");
    }

    @Override
    public void saveSurname(String surname) {
        editor.putString("surname", surname).apply();
    }

    @Override
    public String getSurname() {
        return sharedPreferences.getString("surname", "");
    }

    @Override
    public void saveUniversity(String university) {
        editor.putString("university", university).apply();
    }

    @Override
    public String getUniversity() {
        return sharedPreferences.getString("university", "");
    }
}
