package com.cmput301f18t20.medicalphotorecord;

import java.util.Date;

public class Problem {
    protected String title, description;
    protected Date dateCreated = new Date(System.currentTimeMillis());
    RecordList recordList;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public RecordList getRecordList() {
        return recordList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
