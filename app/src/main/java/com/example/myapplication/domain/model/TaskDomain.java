package com.example.myapplication.domain.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class TaskDomain {
    private final int id;
    private final String name;
    private final String description;
    private final Long startTime;
    private final Long finishTime;
    private final int tag;
    private final int type;
    private final int urgency;
    private final int shifting;

    public TaskDomain(int id, String name, String description, Long startTime, Long finishTime, int tag, int type, int urgency, int shifting) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.tag = tag;
        this.type = type;
        this.urgency = urgency;
        this.shifting = shifting;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Long getStartTime() {
        return startTime;
    }
    public Long getFinishTime() {
        return finishTime;
    }
    public int getTag() {
        return tag;
    }
    public int getType() {
        return type;
    }
    public int getUrgency() {
        return urgency;
    }
    public int getShifting() {
        return shifting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDomain taskDomain = (TaskDomain) o;
        return id == taskDomain.id
                && Objects.equals(name, taskDomain.name)
                && Objects.equals(tag, taskDomain.tag)
                && Objects.equals(type, taskDomain.type)
                && Objects.equals(urgency, taskDomain.urgency)
                && Objects.equals(shifting, taskDomain.shifting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tag, type, urgency, shifting);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskUi{id=" + id + ",name=" + name + ",tag" + tag + ",urgency" + urgency + ",shifting" + shifting + "}";
    }
}
