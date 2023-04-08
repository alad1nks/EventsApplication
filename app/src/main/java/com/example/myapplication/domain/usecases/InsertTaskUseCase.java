package com.example.myapplication.domain.usecases;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.myapplication.data.model.TaskData;
import com.example.myapplication.data.repository.TasksRepositoryImpl;
import com.example.myapplication.domain.model.TaskDomain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InsertTaskUseCase {
    private final TasksRepositoryImpl repository;
    private final MediatorLiveData<List<TaskDomain>> eduTasks;
    private final MediatorLiveData<List<TaskDomain>> workTasks;
    private final LiveData<List<TaskData>> eduTasksFromDatabase;
    private final LiveData<List<TaskData>> workTasksFromDatabase;
    public InsertTaskUseCase(Application application) {
        repository = new TasksRepositoryImpl(application);
        eduTasksFromDatabase = repository.getEduTasks();
        eduTasks = new MediatorLiveData<>();
        workTasksFromDatabase = repository.getWorkTasks();
        workTasks = new MediatorLiveData<>();
        eduTasks.addSource(eduTasksFromDatabase, tasksData -> eduTasks.postValue(convertDataToDomain(tasksData)));
        workTasks.addSource(workTasksFromDatabase, tasksData -> workTasks.postValue(convertDataToDomain(tasksData)));
    }

    private List<TaskDomain> convertDataToDomain(List<TaskData> tasks) {
        List<TaskDomain> result1 = new ArrayList<>();
        List<TaskDomain> result2 = new ArrayList<>();
        for (TaskData i : tasks) {
            Long start = i.getStartTime();
            Long finish = i.getFinishTime();
            Long now = Calendar.getInstance().getTimeInMillis();
            Log.d("times", start.toString() + " " + finish.toString() + " " + now.toString());
            int tag;
            if (now <= start) {
                tag = 0;
            } else if (now <= finish) {
                tag = 1;
            } else {
                tag = 2;
            }
            if (now <= finish) {
                result1.add(new TaskDomain(
                        i.getId(), i.getName(), i.getDescription(), start, finish, tag, i.getType(), i.getUrgency(), i.getShifting()
                ));
            } else {
                result2.add(new TaskDomain(
                        i.getId(), i.getName(), i.getDescription(), start, finish, tag, i.getType(), i.getUrgency(), i.getShifting()
                ));
            }

        }
        result1.addAll(result2);
        return result1;
    }

    public void insertTask(String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting) {
        repository.insertTask(name, description, startTime, finishTime, type, urgency, shifting);
    }

    public LiveData<List<TaskDomain>> getEduTasks() {
        return eduTasks;
    }

    public LiveData<List<TaskDomain>> getWorkTasks() {
        return workTasks;
    }

}
