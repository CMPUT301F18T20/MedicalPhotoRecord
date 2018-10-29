package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.ArrayList;

public class Problem extends Entry {
    protected String description;
    protected ArrayList<Record> records = new ArrayList<>();

    Problem() {
        super();
    }

    Problem(String creatorUserID) throws NonNumericUserIDException {
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

    /* separates the patientRecords from the records and returns them */
    protected ArrayList<PatientRecord> getAllPatientRecords() {
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();

        //iterate through all records stored
        for (Record record : this.records) {

            //if they are a patient record they contain photos, so add them to the patientRecords array
            if (record.getClass() == PatientRecord.class) {
                patientRecords.add((PatientRecord) record);
            }
        }

        return patientRecords;
    }

    /* returns all photos for all records associated with the problem
     * in order of added record.
     */
    public ArrayList<Photo> getAllPhotosFromRecordsInOrder() {
        ArrayList<Photo> returnPhotoArray = new ArrayList<>();
        ArrayList<PatientRecord> patientRecords = this.getAllPatientRecords();

        //iterate through all patient records stored
        for (PatientRecord record : patientRecords) {

            //add all the photos in patient records
            returnPhotoArray.addAll(record.getPhotos());
        }

        return returnPhotoArray;
    }

    /* returns all geo for all records associated with the problem
     * in order of added record.
     */
    public ArrayList<Location> getAllGeoFromRecords() {
        //TODO this should return a hashmap of HASHMAP<Location, PatientRecord>
        //TODO so that we can display some information (like title) in the map view
        ArrayList<Location> returnGeoArray = new ArrayList<>();
        ArrayList<PatientRecord> patientRecords = this.getAllPatientRecords();

        //iterate through all patient records stored
        for (PatientRecord record : patientRecords) {

            //add all the locations in patient records
            returnGeoArray.add(record.getGeolocation());
        }

        return returnGeoArray;
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
