package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.Date;

public class Problem extends Entry {
    protected String description;
    ArrayList<Record> recordList;

    public String getDescription() {
        return description;
    }

    public ArrayList<Record> getRecordList() {
        return recordList;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
