package com.example.myapplication.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
    @Query("UPDATE tasks SET name = :name, description = :description, startTime = :startTime, finishTime = :finishTime, type = :type, urgency = :urgency, shifting = :shifting WHERE id = :id")
    void updateTask(int id, String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting);
    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteTask(int id);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTask(TaskData task);
}
