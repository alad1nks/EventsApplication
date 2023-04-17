package com.example.myapplication.ui.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public final class TaskUi{
    private final int id;
    private final String name;
    private final String description;
    private final Long date;
    private final Integer startHour;
    private final Integer startMinute;
    private final Integer finishHour;
    private final Integer finishMinute;
    private final int typePos;
    private final int urgencyPos;
    private final int shiftingPos;
    private final int tag;
    private final int urgency;
    private final int shifting;

    public TaskUi(int id, String name, String description, Long date, Integer startHour, Integer startMinute, Integer finishHour, Integer finishMinute, int typePos, int urgencyPos, int shiftingPos, int tag, int urgency, int shifting) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.finishHour = finishHour;
        this.finishMinute = finishMinute;
        this.typePos = typePos;
        this.urgencyPos = urgencyPos;
        this.shiftingPos = shiftingPos;
        this.tag = tag;
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
    public Long getDate() {
        return date;
    }
    public Integer getStartHour() {
        return startHour;
    }
    public Integer getStartMinute() {
        return startMinute;
    }
    public Integer getFinishHour() {
        return finishHour;
    }
    public Integer getFinishMinute() {
        return finishMinute;
    }
    public int getTypePos() {
        return typePos;
    }
    public int getUrgencyPos() {
        return urgencyPos;
    }
    public int getShiftingPos() {
        return shiftingPos;
    }
    @DrawableRes public int getTag() {
        return tag;
    }
    @DrawableRes public int getUrgency() {
        return urgency;
    }
    @DrawableRes public int getShifting() {
        return shifting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskUi taskUi = (TaskUi) o;
        return id == taskUi.id
                && Objects.equals(name, taskUi.name)
                && Objects.equals(description, taskUi.description)
                && Objects.equals(date, taskUi.date)
                && Objects.equals(startHour, taskUi.startHour)
                && Objects.equals(startMinute, taskUi.startMinute)
                && Objects.equals(finishHour, taskUi.finishHour)
                && Objects.equals(finishMinute, taskUi.finishMinute)
                && Objects.equals(tag, taskUi.tag)
                && Objects.equals(urgency, taskUi.urgency)
                && Objects.equals(shiftingPos, taskUi.shiftingPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, date, startHour, startMinute, finishHour, finishMinute, tag, urgency, shiftingPos);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskUi{id=" + id + ",name=" + name + ",tag" + tag + ",urgency" + urgency + ",shifting" + shifting + "}";
    }
}
