package com.example.myapplication.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.data.model.TaskData;

import java.util.List;

@Dao
public interface TasksDao {
    @Query("SELECT * FROM tasks WHERE type = 4")
    public LiveData<List<TaskData>> getTasks();

    @Query("SELECT * FROM tasks WHERE type = 0 ORDER BY startTime, finishTime")
    LiveData<List<TaskData>> getEduTasks();
    @Query("SELECT * FROM tasks WHERE type = 1 ORDER BY startTime, finishTime")
    LiveData<List<TaskData>> getWorkTasks();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertTask(TaskData task);
}
