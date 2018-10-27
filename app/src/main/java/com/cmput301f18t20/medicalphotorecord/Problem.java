package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Problem extends Entry {
    protected String description;
    protected HashMap<String, Integer> aggregatedKeywordCounts;
    protected ArrayList<Record> records = new ArrayList<>();

    Problem(String creatorUserID) {
        super(creatorUserID);
    }

    public Record getRecord(int recordIndex) {
        return this.records.get(recordIndex);
    }

    public void setRecord(Record record, int recordIndex) {
        this.records.set(recordIndex, record);
        //TODO: commit changes to disk/network
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
        //TODO: commit changes to disk/network
    }

    public void removeRecord(Record record) {
        records.remove(record);
        //TODO: commit changes to disk/network
    }

    public void removeRecord(int recordIndex) {
        records.remove(recordIndex);
        //TODO: commit changes to disk/network
    }

    //TODO: Will fetch from server or from local file
    public void fetchUpdatedRecordList() {
    }

    public ArrayList<Photo> getAllPhotosFromRecordsInOrder() {
        return null; //TODO
    }

    public ArrayList<Location> getAllGeoFromRecords() {
        return null; //TODO
    }

    public int getRecordCount() {
        return records.size();
    }

    public void updateIndex() {
        //TODO: Update keywordCounts based on Title, Description, Date, CreatedByUserID
    }

    public HashMap<String, Integer> getAggregatedKeywordCounts() {
        return aggregatedKeywordCounts;
    }

    public void updateAggregatedIndex() {
        //TODO: Update AggregatedKeywordCounts based on keywordCounts of all child records
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
