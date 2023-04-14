package com.example.myapplication.ui.tasks;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.R;
import com.example.myapplication.domain.model.TaskDomain;
import com.example.myapplication.domain.usecases.TasksUseCases;
import com.example.myapplication.ui.model.TaskUi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TasksViewModel extends AndroidViewModel {
    private final TasksUseCases useCase;
    private final MediatorLiveData<List<TaskUi>> eduTasks;
    private final MediatorLiveData<List<TaskUi>> workTasks;
    private final MutableLiveData<String> taskName;
    private final MutableLiveData<String> taskDescription;
    private final MutableLiveData<Integer> taskType;
    private final MutableLiveData<Integer> taskUrgency;
    private final MutableLiveData<Integer> taskShifting;
    private final MutableLiveData<Long> taskDate;
    private final MutableLiveData<Long> taskStart;
    private final MutableLiveData<Long> taskEnd;
    private final MutableLiveData<Integer> filterTaskTag;
    private final MutableLiveData<Integer> filterTaskUrgency;
    private final MutableLiveData<Integer> filterTaskShifting;
    private final MutableLiveData<Long> filterCalendar;
    private final LiveData<Pair<String, Integer>> notification;

    public TasksViewModel(Application application) {
        super(application);
        long time = Calendar.getInstance().getTimeInMillis();
        useCase = new TasksUseCases(application);
        LiveData<List<TaskDomain>> eduTasksFromDomain = useCase.getEduTasks();
        LiveData<List<TaskDomain>> workTasksFromDomain = useCase.getWorkTasks();
        eduTasks = new MediatorLiveData<>();
        workTasks = new MediatorLiveData<>();
        eduTasks.addSource(eduTasksFromDomain, taskDomain -> eduTasks.postValue(convertDomainToUi(taskDomain)));
        workTasks.addSource(workTasksFromDomain, taskDomain -> workTasks.postValue(convertDomainToUi(taskDomain)));
        taskName = new MutableLiveData<>("");
        taskDescription = new MutableLiveData<>("");
        taskType = new MutableLiveData<>();
        taskUrgency = new MutableLiveData<>();
        taskShifting = new MutableLiveData<>();
        taskDate = new MutableLiveData<>();
        taskStart = new MutableLiveData<>();
        taskEnd = new MutableLiveData<>();
        filterTaskTag = new MutableLiveData<>(0);
        filterTaskUrgency = new MutableLiveData<>(0);
        filterTaskShifting = new MutableLiveData<>(0);
        filterCalendar = new MutableLiveData<>(time - (time + 10800000) % 86400000);
        notification = useCase.getNotification();
    }

    public LiveData<List<TaskUi>> getEduTasks() {
        return eduTasks;
    }
    public LiveData<List<TaskUi>> getWorkTasks() {
        return workTasks;
    }
    public LiveData<Pair<String, Integer>> getNotification() {
        return notification;
    }
    public LiveData<Integer> getFilterTaskTag() {
        return filterTaskTag;
    }
    public LiveData<Integer> getFilterTaskUrgency() {
        return filterTaskUrgency;
    }
    public LiveData<Integer> getFilterTaskShifting() {
        return filterTaskShifting;
    }
    public LiveData<Long> getFilterCalendar() {
        return filterCalendar;
    }

    public void setTaskName(String name) {
        taskName.setValue(name);
    }
    public void setTaskDescription(String description) {
        taskDescription.setValue(description);
    }
    public void setTaskType(Integer pos) {
        taskType.setValue(pos);
    }
    public void setTaskUrgency(Integer pos) {
        taskUrgency.setValue(pos);
    }
    public void setTaskShifting(Integer pos) {
        taskShifting.setValue(pos);
    }
    public void setTaskDate(Long date) {
        taskDate.setValue(date);
    }
    public void setTaskStart(Long time) {
        taskStart.setValue(time);
    }
    public void setTaskEnd(Long time) {
        taskEnd.setValue(time);
    }
    public void setFilterTaskTag(Integer pos) {
        filterTaskTag.setValue(pos);
    }
    public void setFilterTaskUrgency(Integer pos) {
        filterTaskUrgency.setValue(pos);
    }
    public void setFilterTaskShifting(Integer pos) {
        filterTaskShifting.setValue(pos);
    }

    public void setFilterCalendar(Long date) {
        filterCalendar.setValue(date);
    }
    public void setFilters() {
        useCase.setFilterTaskTag(filterTaskTag.getValue());
        useCase.setFilterTaskUrgency(filterTaskUrgency.getValue());
        useCase.setFilterTaskShifting(filterTaskShifting.getValue());
    }

    public void setCalendar() {
        useCase.setFilterCalendar(filterCalendar.getValue());
    }

    public String insertTask() {
        long cur = Calendar.getInstance().getTimeInMillis();
        long start = taskStart.getValue() + taskDate.getValue();
        long end = taskEnd.getValue() + taskDate.getValue();
        String name = taskName.getValue();
        if (name.isEmpty()) {
            return "0";
        } else if (cur <= start && start <= end) {
            String response = useCase.insertTask(
                    name,
                    taskDescription.getValue(),
                    start,
                    end,
                    taskType.getValue(),
                    taskUrgency.getValue(),
                    taskShifting.getValue());
            if (Objects.equals(response, "true")) {
                return "1";
            }
            return response;
        } else {
            return "2";
        }
    }
    public String updateTask(int id) {
        long cur = Calendar.getInstance().getTimeInMillis();
        long start = taskStart.getValue() + taskDate.getValue();
        long end = taskEnd.getValue() + taskDate.getValue();
        String name = taskName.getValue();
        if (name.isEmpty()) {
            return "0";
        } else if (cur <= start && start <= end) {
            return useCase.updateTask(
                    id,
                    name,
                    taskDescription.getValue(),
                    start,
                    end,
                    taskType.getValue(),
                    taskUrgency.getValue(),
                    taskShifting.getValue());
        }  else {
            return "2";
        }
    }
    public void deleteTask(int id) {
        useCase.deleteTask(id);
    }
    public void refresh() {
        useCase.refresh();
    }

    public void saveTasks() {
        useCase.saveTasks();
    }

    public void uploadTasks(String file) {
        useCase.uploadTasks(file);
    }

    public void clearNotification() {
        useCase.clearNotification();
    }

    private List<TaskUi> convertDomainToUi(List<TaskDomain> tasks) {
        List<TaskUi> ans = new ArrayList<>();
        for (TaskDomain t : tasks) {
            Log.d("sasd", t.toString());
            int tag;
            int urgency;
            int shifting;
            if (t.getTag() == 0) {
                tag = R.drawable.unchecked;
            } else if (t.getTag() == 1) {
                tag = R.drawable.unchecked_red;
            } else {
                tag = R.drawable.checked;
            }
            if (t.getUrgency() == 0) {
                urgency = R.drawable.empty;
            } else {
                urgency = R.drawable.ic_high_priority;
            }
            shifting = R.drawable.empty;
            Long date = t.getStartTime() - (t.getStartTime() + 10800000) % 86400000;
            Integer startMinute = Math.toIntExact((((t.getStartTime() / 1000 + 10800) % 86400) / 60) % 60);
            Integer startHour = Math.toIntExact(((((t.getStartTime() / 1000 + 10800) % 86400) / 60) - startMinute) / 60);
            Integer finishMinute = Math.toIntExact((((t.getFinishTime() / 1000 + 10800) % 86400) / 60) % 60);
            Integer finishHour = Math.toIntExact(((((t.getFinishTime() / 1000 + 10800) % 86400) / 60) - finishMinute) / 60);
            ans.add(
                    new TaskUi(
                            t.getId(), t.getName(), t.getDescription(), date, startHour, startMinute, finishHour, finishMinute, t.getType(), t.getUrgency(), t.getShifting(), tag, urgency, shifting
                    )
            );
        }
        return ans;
    }
}