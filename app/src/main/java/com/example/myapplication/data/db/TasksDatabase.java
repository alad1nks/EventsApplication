package com.example.myapplication.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.model.TaskData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TaskData.class}, version = 5, exportSchema = false)
public abstract class TasksDatabase extends RoomDatabase {
    public abstract TasksDao tasksDao();
    private static volatile TasksDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TasksDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TasksDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TasksDatabase.class, "tasks")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
            });
        }
    };
}
