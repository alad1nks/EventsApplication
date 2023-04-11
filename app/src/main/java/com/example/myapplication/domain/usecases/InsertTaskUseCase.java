package com.example.myapplication.domain.usecases;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.TaskData;
import com.example.myapplication.data.repository.TasksRepositoryImpl;
import com.example.myapplication.domain.model.TaskDomain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class InsertTaskUseCase {
    private final TasksRepositoryImpl repository;
    private final MediatorLiveData<List<TaskDomain>> eduTasks;
    private final MediatorLiveData<List<TaskDomain>> workTasks;
    private final LiveData<List<TaskData>> eduTasksFromDatabase;
    private final LiveData<List<TaskData>> workTasksFromDatabase;
    private final MutableLiveData<Integer> notificationId;
    private final MutableLiveData<Integer> filterTaskTag;
    private final MutableLiveData<Integer> filterTaskUrgency;
    private final MutableLiveData<Integer> filterTaskShifting;
    private final MutableLiveData<Long> filterCalendar;
    private final Application application;
    public InsertTaskUseCase(Application application) {
        this.application = application;
        long time = Calendar.getInstance().getTimeInMillis();
        notificationId = new MutableLiveData<>();
        filterTaskTag = new MutableLiveData<>(0);
        filterTaskUrgency = new MutableLiveData<>(0);
        filterTaskShifting = new MutableLiveData<>(0);
        filterCalendar = new MutableLiveData<>(time - (time + 10800000) % 86400000);
        repository = new TasksRepositoryImpl(application);
        eduTasksFromDatabase = repository.getEduTasks();
        eduTasks = new MediatorLiveData<>();
        workTasksFromDatabase = repository.getWorkTasks();
        workTasks = new MediatorLiveData<>();
        eduTasks.addSource(eduTasksFromDatabase, tasksData -> eduTasks.postValue(convertDataToDomain(tasksData)));
        eduTasks.addSource(filterTaskShifting, filters -> {
            if (eduTasksFromDatabase.getValue() != null) {
                eduTasks.postValue(convertDataToDomain(eduTasksFromDatabase.getValue()));
            }
        });
        eduTasks.addSource(filterCalendar, calendar -> {
            if (eduTasksFromDatabase.getValue() != null) {
                eduTasks.postValue(convertDataToDomain(eduTasksFromDatabase.getValue()));
            }
        });
        workTasks.addSource(workTasksFromDatabase, tasksData -> workTasks.postValue(convertDataToDomain(tasksData)));
        workTasks.addSource(filterTaskShifting, tasksData -> {
            if (workTasksFromDatabase.getValue() != null) {
                workTasks.postValue(convertDataToDomain(workTasksFromDatabase.getValue()));
            }
        });
        workTasks.addSource(filterCalendar, calendar -> {
            if (workTasksFromDatabase.getValue() != null) {
                workTasks.postValue(convertDataToDomain(workTasksFromDatabase.getValue()));
            }
        });

    }

    private List<TaskDomain> convertDataToDomain(List<TaskData> tasks) {
        List<TaskDomain> result1 = new ArrayList<>();
        List<TaskDomain> result2 = new ArrayList<>();
        int filterUrgency = filterTaskUrgency.getValue();
        int filterShifting = filterTaskShifting.getValue();
        int filterTag = filterTaskTag.getValue();
        long date = filterCalendar.getValue();
        long now = Calendar.getInstance().getTimeInMillis();
        for (TaskData i : tasks) {
            Long start = i.getStartTime();
            Long finish = i.getFinishTime();
            int urgency = i.getUrgency();
            int shifting = i.getShifting();
            int tag;
            if (now <= start) {
                tag = 0;
                if (start - now > 290000 && start - now < 310000) {
                    notificationId.postValue(i.getId());
                }
            } else if (now <= finish) {
                tag = 1;
            } else {
                tag = 2;
            }
            if ((start >= date && start <= date + 86400000) && (filterTag - 1 == tag || filterTag == 0) && (filterUrgency - 1 == urgency || filterUrgency == 0) && (filterShifting - 1 == shifting || filterShifting == 0)) {
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
    public LiveData<Integer> getNotificationId() {
        return notificationId;
    }

    public void refresh() {
        eduTasks.postValue(convertDataToDomain(Objects.requireNonNull(eduTasksFromDatabase.getValue())));
        if (workTasksFromDatabase.getValue() != null) {
            workTasks.postValue(convertDataToDomain(workTasksFromDatabase.getValue()));
        }
    }

    public void clearNotification() {
        notificationId.postValue(null);
    }

    @SuppressLint("SimpleDateFormat")
    public void saveTasks() {
        StringBuilder response = new StringBuilder();
        if (eduTasks.getValue() != null) {
            for (TaskDomain i : eduTasks.getValue()) {
                response.append("DTSTART:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getStartTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getStartTime())))
                        .append("\nDTEND:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getFinishTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getStartTime())))
                        .append("\nSUMMARY:").append(i.getName())
                        .append("\nDESCRIPTION:").append(i.getDescription()).append("\n");
            }
        }
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "");
            File filepath = new File(root, "work-studies.ics");
            FileWriter writer = new FileWriter(filepath);
            writer.append(response);
            writer.flush();
            writer.close();
            Toast.makeText(application.getApplicationContext(), "Расписание сохранено", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void uploadTasks(String file) {
        String name = null;
        String description = null;
        Long startTime = null;
        Long finishTime = null;
        for(String i : file.split("\n")) {
            Log.d("uploadTasks", i);
            if (i.contains("DTSTART:")) {
                String time = i.substring(8);
                String syears = time.substring(0, 4);
                String smonth = time.substring(4, 6);
                String sdays = time.substring(6, 8);
                String shours = time.substring(9, 11);
                String sminutes = time.substring(11, 13);
                String sseconds = time.substring(13, 15);
                startTime = timeConversion(syears+smonth+sdays+shours+sminutes+sseconds);
            } else if (i.contains("DTEND:")) {
                String time = i.substring(6);
                String syears = time.substring(0, 4);
                String smonth = time.substring(4, 6);
                String sdays = time.substring(6, 8);
                String shours = time.substring(9, 11);
                String sminutes = time.substring(11, 13);
                String sseconds = time.substring(13, 15);
                finishTime = timeConversion(syears+smonth+sdays+shours+sminutes+sseconds);
            } else if (i.contains("SUMMARY:")) {
                name = i.substring(8);
            } else if (i.contains("DESCRIPTION:")) {
                description = i.substring(12);
            }
            if (name != null && startTime != null && finishTime != null) {
                if (description != null) {
                    repository.insertTask(name, description, startTime, finishTime, 0, 0, 0);
                } else {
                    repository.insertTask(name, "", startTime, finishTime, 0, 0, 0);
                }
                name = null;
                description = null;
                startTime = null;
                finishTime = null;
            }
        }
        Toast.makeText(application.getApplicationContext(), "Расписание загружено", Toast.LENGTH_LONG).show();
    }

    public void setFilterTaskTag(Integer pos) {
        filterTaskTag.postValue(pos);
    }
    public void setFilterTaskUrgency(Integer pos) {
        filterTaskUrgency.postValue(pos);
    }
    public void setFilterTaskShifting(Integer pos) {
        filterTaskShifting.postValue(pos);
    }
    public void setFilterCalendar(Long date) {
        filterCalendar.postValue(date);
    }

    private static long timeConversion(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH);
        long unixTime = 0;
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        try {
            unixTime = Objects.requireNonNull(dateFormat.parse(time)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime;
    }

}
