package com.example.myapplication.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskData {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public Long startTime;
    public Long finishTime;
    public int type;
    public int urgency;
    public int shifting;
    public int done;

    public TaskData(String name, String description, Long startTime, Long finishTime, int type, int urgency, int shifting, int done) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.type = type;
        this.urgency = urgency;
        this.shifting = shifting;
        this.done = done;
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
    public int getType() {
        return type;
    }
    public int getUrgency() {
        return urgency;
    }
    public int getShifting() {
        return shifting;
    }
    public int getDone() {
        return done;
    }
}
