package com.example.myapplication.preferences;

public interface AppPreference {
    Boolean loggedIn();
    void logIn();
    void logOut();

    void saveName(String name);
    String getName();
    void saveSurname(String surname);
    String getSurname();
    void saveUniversity(String university);
    String getUniversity();
}
