package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class RecordManager {
    protected ArrayList<Record> Records = new ArrayList<>();

    public void setRecords(ArrayList<Record> records) {
        Records = records;
    }

    public ArrayList<Record> getRecords(ArrayList<Record> records) {
        return Records;
    }

    public void addRecord(Record record) {
        Records.add(record);
    }

    public void deleteRecord(Record record) {
        //TODO: this will need to be a lot smarter
        Records.remove(record);
    }

    public void clearRecords() {
        Records.clear();
    }



}
