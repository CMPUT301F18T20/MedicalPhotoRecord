package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class RecordList {
    protected ArrayList<Record> Records = new ArrayList<>();

    public void setRecords(ArrayList<Record> records) {
        Records = records;
    }

    public ArrayList<Record> getRecords() {
        return Records;
    }

    public void addRecord(Record record) {
        Records.add(record);
    }

    public void deleteRecord(Record record) {
        // TODO: this will need to be a lot smarter, likely have to do with tasks like
        // TODO: "delete all records with a certain string in their title"
        Records.remove(record);
    }

    public void clearRecords() {
        Records.clear();
        //TODO: commit changes to disk/network
    }

    public int getRecordCount() {
        return Records.size();
    }
}
