package com.example.myapplication.ui.tasks;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.R;
import com.example.myapplication.domain.model.TaskDomain;
import com.example.myapplication.domain.usecases.InsertTaskUseCase;
import com.example.myapplication.ui.model.TaskUi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksViewModel extends AndroidViewModel {
    private final InsertTaskUseCase useCase;
    private final LiveData<List<TaskDomain>> eduTasksFromDomain;
    private final LiveData<List<TaskDomain>> workTasksFromDomain;
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

    public TasksViewModel(Application application) {
        super(application);
        useCase = new InsertTaskUseCase(application);
        eduTasksFromDomain = useCase.getEduTasks();
        workTasksFromDomain = useCase.getWorkTasks();
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
    }

    public LiveData<List<TaskUi>> getEduTasks() {
        return eduTasks;
    }
    public LiveData<List<TaskUi>> getWorkTasks() {
        return workTasks;
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

    public Boolean insertTask() {
        Long cur = Calendar.getInstance().getTimeInMillis();
        Long start = taskStart.getValue() + taskDate.getValue();
        Long end = taskEnd.getValue() + taskDate.getValue();
        Log.d("insertTask", start.toString() + " " + end.toString() + " " + cur);
        if (cur <= start && start <= end) {
            useCase.insertTask(
                    taskName.getValue(),
                    taskDescription.getValue(),
                    start,
                    end,
                    taskType.getValue(),
                    taskUrgency.getValue(),
                    taskShifting.getValue());
            return true;
        }
        return false;
    }

    public void refresh() {
        useCase.refresh();
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
            ans.add(
                    new TaskUi(
                            t.getId(), t.getName(), tag, urgency, shifting
                    )
            );
        }
        return ans;
    }
}