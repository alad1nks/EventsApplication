package com.example.myapplication.domain.usecases;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.TaskData;
import com.example.myapplication.data.repository.TasksRepository;
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

public class TasksUseCases {
    private final TasksRepository repository;
    private final MediatorLiveData<List<TaskDomain>> eduTasks;
    private final MediatorLiveData<List<TaskDomain>> workTasks;
    private final LiveData<List<TaskData>> eduTasksFromDatabase;
    private final LiveData<List<TaskData>> workTasksFromDatabase;
    private final MutableLiveData<Pair<String, Integer>> notification;
    private final MutableLiveData<Integer> filterTaskTag;
    private final MutableLiveData<Integer> filterTaskUrgency;
    private final MutableLiveData<Integer> filterTaskShifting;
    private final MutableLiveData<Long> filterCalendar;
    private final Application application;
    public TasksUseCases(Application application) {
        this.application = application;
        long time = Calendar.getInstance().getTimeInMillis();
        notification = new MutableLiveData<>();
        filterTaskTag = new MutableLiveData<>(0);
        filterTaskUrgency = new MutableLiveData<>(0);
        filterTaskShifting = new MutableLiveData<>(0);
        filterCalendar = new MutableLiveData<>(time - (time + 10800000) % 86400000);
        repository = new TasksRepository(application);
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
                    notification.postValue(Pair.create(i.getName(), i.getId()));
                }
            } else if (now <= finish) {
                tag = 1;
            } else {
                tag = 2;
            }
            if ((start >= date && start <= date + 86400000) && (filterTag - 1 == tag || filterTag == 0) && (filterUrgency - 1 == urgency || filterUrgency == 0) && (filterShifting - 1 == shifting || filterShifting == 0)) {
                if (now <= finish) {
                    result1.add(
                            new TaskDomain(
                                    i.getId(), i.getName(), i.getDescription(), start, finish, tag, i.getType(), i.getUrgency(), i.getShifting()
                            )
                    );
                } else {
                    result2.add(
                            new TaskDomain(
                                    i.getId(), i.getName(), i.getDescription(), start, finish, tag, i.getType(), i.getUrgency(), i.getShifting()
                            )
                    );
                }
            }
        }
        result1.addAll(result2);
        return result1;
    }

    public String insertTask(String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting) {
        if (type == 0) {
            if (eduTasks.getValue() != null) {
                List<TaskDomain> edu = eduTasks.getValue();
                for (TaskDomain i : edu) {
                    Long start = i.getStartTime();
                    Long finish = i.getFinishTime();
                    if ((startTime <= start && finishTime >= start) || (startTime >= start && startTime <= finish)) {
                        return i.getName();
                    }
                }
            }

        } else {
            if (workTasks.getValue() != null) {
                List<TaskDomain> work = workTasks.getValue();
                for (TaskDomain i : work) {
                    Long start = i.getStartTime();
                    Long finish = i.getFinishTime();
                    if ((startTime <= start && finishTime >= start) || (startTime >= start && startTime <= finish)) {
                        return i.getName();
                    }
                }
            }
        }
        repository.insertTask(name, description, startTime, finishTime, type, urgency, shifting);
        return "1";
    }
    public String updateTask(int id, String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting) {
        if (type == 0) {
            if (eduTasks.getValue() != null) {
                List<TaskDomain> edu = eduTasks.getValue();
                for (TaskDomain i : edu) {
                    Long start = i.getStartTime();
                    Long finish = i.getFinishTime();
                    if (((startTime <= start && finishTime >= start) || (startTime >= start && startTime <= finish)) && i.getId() != id) {
                        return i.getName();
                    }
                }
            }
        } else {
            if (workTasks.getValue() != null) {
                List<TaskDomain> work = workTasks.getValue();
                for (TaskDomain i : work) {
                    Long start = i.getStartTime();
                    Long finish = i.getFinishTime();
                    if (((startTime <= start && finishTime >= start) || (startTime >= start && startTime <= finish)) && i.getId() != id) {
                        return i.getName();
                    }
                }
            }
        }
        repository.updateTask(id, name, description, startTime, finishTime, type, urgency, shifting);
        return "1";
    }
    public void deleteTask(int id) {
        repository.deleteTask(id);
    }

    public LiveData<List<TaskDomain>> getEduTasks() {
        return eduTasks;
    }

    public LiveData<List<TaskDomain>> getWorkTasks() {
        return workTasks;
    }
    public LiveData<Pair<String, Integer>> getNotification() {
        return notification;
    }

    public void refresh() {
        eduTasks.postValue(convertDataToDomain(Objects.requireNonNull(eduTasksFromDatabase.getValue())));
        if (workTasksFromDatabase.getValue() != null) {
            workTasks.postValue(convertDataToDomain(workTasksFromDatabase.getValue()));
        }
    }

    public void clearNotification() {
        notification.postValue(null);
    }

    @SuppressLint("SimpleDateFormat")
    public void saveTasks() {
        StringBuilder response = new StringBuilder();
        if (repository.getEduTasks().getValue() != null) {
            for (TaskData i : repository.getEduTasks().getValue()) {
                response.append("DTSTART:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getStartTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getStartTime())))
                        .append("\nDTEND:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getFinishTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getFinishTime())))
                        .append("\nSUMMARY:").append(i.getName())
                        .append("\nTAG:").append("0")
                        .append("\nDESCRIPTION:").append(i.getDescription()).append("\n");
            }
        }
        if (repository.getWorkTasks().getValue() != null) {
            Log.d("sgdsg", "dgsd");
            for (TaskData i : repository.getWorkTasks().getValue()) {
                response.append("DTSTART:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getStartTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getStartTime())))
                        .append("\nDTEND:").append(new SimpleDateFormat("yyyyMMdd").format(new Date(i.getFinishTime()))).append("T").append(new SimpleDateFormat("hhmmss").format(new Date(i.getFinishTime())))
                        .append("\nSUMMARY:").append(i.getName())
                        .append("\nTAG:").append("1")
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
        Long startTime = null;
        Long finishTime = null;
        String tag = null;
        String description = null;
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
            } else if (i.contains("TAG:")) {
                tag = i.substring(4);
            } else if (i.contains("DESCRIPTION:")) {
                description = "";
            }
            if (name != null && startTime != null && finishTime != null && description != null) {
                if (tag != null) {
                    repository.insertTask(name, description, startTime, finishTime, Integer.parseInt(tag), 0, 0);
                    tag = null;
                } else {
                    repository.insertTask(name, description, startTime, finishTime, 0, 0, 0);
                }
                name = null;
                startTime = null;
                finishTime = null;
                description = null;
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
