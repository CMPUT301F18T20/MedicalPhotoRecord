/*
 * Class name: Problem
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 7:47 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class Problem implements Serializable {
    protected String description;
    protected ArrayList<Record> records = new ArrayList<>();
    protected String title, createdByUserID;
    protected Date date = new Date(System.currentTimeMillis());
    public UUID uuid = java.util.UUID.randomUUID();

    public Problem(String createdByUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(createdByUserID);
        this.setTitle(title);
    }

    public Record getRecord(int recordIndex) {
        return this.records.get(recordIndex);
    }

    public void setRecord(Record record, int recordIndex) {
        this.records.set(recordIndex, record);
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public void removeRecord(Record record) {
        records.remove(record);
    }

    public void removeRecord(int recordIndex) {
        records.remove(recordIndex);
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

    public String getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(String createdByUserID)
            throws UserIDMustBeAtLeastEightCharactersException {
        if (createdByUserID.length() >= 8) {
            this.createdByUserID = createdByUserID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws TitleTooLongException {
        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){
        return this.title;
    }

    public UUID getUuid(){ return this.uuid;}
    public void setUuid(UUID uuid) {this.uuid = uuid;}

}
