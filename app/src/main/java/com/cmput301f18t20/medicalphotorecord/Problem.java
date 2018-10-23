package com.cmput301f18t20.medicalphotorecord;

import java.util.Date;

public class Problem extends RecordManager {
    protected String title, description;
    protected Date date;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
