package com.example.myapplication.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.data.db.TasksDao;
import com.example.myapplication.data.db.TasksDatabase;
import com.example.myapplication.data.model.TaskData;

import java.util.List;

public class TasksRepository {
    private final TasksDao tasksDao;
    private final LiveData<List<TaskData>> eduTasks;
    private final LiveData<List<TaskData>> workTasks;


    public TasksRepository(Application application) {
        TasksDatabase db = TasksDatabase.getDatabase(application);
        tasksDao = db.tasksDao();
        eduTasks = tasksDao.getEduTasks();
        workTasks = tasksDao.getWorkTasks();
    }

    public LiveData<List<TaskData>> getEduTasks() {
        return eduTasks;
    }
    public LiveData<List<TaskData>> getWorkTasks() {
        return workTasks;
    }

    public void insertTask(String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting) {
        TasksDatabase.databaseWriteExecutor.execute(() -> tasksDao.insertTask(
                new TaskData(name, description, startTime, finishTime, type, urgency, shifting, 0)
        ));
    }

    public void updateTask(int id, String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting) {
        TasksDatabase.databaseWriteExecutor.execute(() -> tasksDao.updateTask(
                id, name, description, startTime, finishTime, type, urgency, shifting));
    }

    public void deleteTask(int id) {
        TasksDatabase.databaseWriteExecutor.execute(() -> tasksDao.deleteTask(id));
    }
}
