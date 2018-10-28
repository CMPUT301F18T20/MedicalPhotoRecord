package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.ArrayList;

public class Problem extends Entry {
    protected String description;
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
        ArrayList<Photo> returnPhotoArray = new ArrayList<>();
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();

        //iterate through all records stored
        for (Record record : this.records) {

            //if they are a patient record they contain photos, so add them to the patientRecords array
            if (record.getClass() == PatientRecord.class) {
                patientRecords.add((PatientRecord) record);
            }
        }

        //iterate through all patient records stored
        for (PatientRecord record : patientRecords) {

            //add all the photos in patient records
            returnPhotoArray.addAll(record.getPhotos());
        }

        return returnPhotoArray;
    }

    public ArrayList<Location> getAllGeoFromRecords() {
        return null; //TODO
    }

    public int getRecordCount() {
        return records.size();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
