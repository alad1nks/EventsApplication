package com.example.myapplication.ui.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public final class TaskUi{
    private final int id;
    private final String name;
    private final int tag;
    private final int urgency;
    private final int shifting;

    public TaskUi(int id, String name, int tag, int urgency, int shifting) {
        this.id = id;
        this.name = name;
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
                && Objects.equals(tag, taskUi.tag)
                && Objects.equals(urgency, taskUi.urgency)
                && Objects.equals(shifting, taskUi.shifting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tag, urgency, shifting);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskUi{id=" + id + ",name=" + name + ",tag" + tag + ",urgency" + urgency + ",shifting" + shifting + "}";
    }
}
