package com.example.todolist;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Card implements CardInterface {
    private String taskName;
    private String date;
    private String startTime;
    private String endTime;

    public Card(String taskName, String date, String startTime, String endTime) {
        this.taskName = taskName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String makeDate() {
        // Use SimpleDateFormat to parse the date and then format it as needed
        try {
            Date originalDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.date);
            assert originalDate != null;
            return new SimpleDateFormat("dd").format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the exception appropriately
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String makeMonth() {
        try {
            Date originalDate = new SimpleDateFormat("dd-MM-yy").parse(this.date);
            return new SimpleDateFormat("MMM").format(originalDate).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the exception appropriately
        }
    }

    @Override
    public String makeTime() {
        return this.startTime + "-" + this.endTime;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
