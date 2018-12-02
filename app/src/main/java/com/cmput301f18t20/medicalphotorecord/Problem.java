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

import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class Problem implements Serializable {
    @JestId
    protected final String UUID = java.util.UUID.randomUUID().toString();
    protected ArrayList<Record> records = new ArrayList<>();
    protected String description, title, createdByUserID;
    protected Date
            dateCreated = new Date(System.currentTimeMillis()),
            dateLastModified = new Date(System.currentTimeMillis());

    /**
     * Problem constructor: set userId and title
     * @param createdByUserID
     * @param title
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws TitleTooLongException
     */
    public Problem(String createdByUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(createdByUserID);
        this.setTitle(title);
    }

    /**
     * UUID getter
     * @return UUID of problem object
     */
    public String getUUID() {
        return UUID;
    }


    /**
     * Get record object for the problem via index
     * @param recordIndex
     * @return record object
     */
    public Record getRecord(int recordIndex) {
        return this.records.get(recordIndex);
    }

    /**
     * Set record object for the problem via index
     * @param record
     * @param recordIndex
     */
    public void setRecord(Record record, int recordIndex) {
        this.records.set(recordIndex, record);
    }

    /**
     * Get all records for that problem
     * @return record list
     */
    public ArrayList<Record> getRecords() {
        return records;
    }

    /**
     * Add record to problem object
     * @param record
     */
    public void addRecord(Record record) {
        records.add(record);
    }

    /**
     * Remove record from the problem via object
     * @param record
     */
    public void removeRecord(Record record) {
        records.remove(record);
    }

    /**
     * Remove record from the problem via index
     * @param recordIndex
     */
    public void removeRecord(int recordIndex) {
        records.remove(recordIndex);
    }

    /**
     * Get all patient records for that problem
     * @return patient record list
     */
    /* separates the patientRecords from the records and returns them */
    public ArrayList<PatientRecord> getAllPatientRecords() {
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

    /**
     * Get all photos for all records for the problem
     * Returns all photos for all records associated with the problem in order of added record.
     * @return photo list
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

    /**
     * Get geolocation for all records of the problem object
     * @return list of all geolocations
     */
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

    /**
     * Get the size of record list
     * @return size of record list
     */
    public int getRecordCount() {
        return records.size();
    }

    /**
     * descripion getter
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description
     * @throws ProblemDescriptionTooLongException > 300 characters
     */
    public void setDescription(String description) throws ProblemDescriptionTooLongException {
        if (description.length() > 300){
            throw new ProblemDescriptionTooLongException();
        }
        this.description = description;
    }

    /**
     * userID getter
     * @return createdByUserID
     */
    public String getCreatedByUserID() {
        return createdByUserID;
    }

    /**
     * userID setter
     * @param createdByUserID
     * @throws UserIDMustBeAtLeastEightCharactersException
     */
    public void setCreatedByUserID(String createdByUserID)
            throws UserIDMustBeAtLeastEightCharactersException {
        if (createdByUserID.length() >= 8) {
            this.createdByUserID = createdByUserID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    /**
     * title getter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * title setter
     * @param title
     * @throws TitleTooLongException > 30 characters
     */
    public void setTitle(String title) throws TitleTooLongException {
        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    /**
     * date created getter
     * @return dateCreated
     */
    public Date getDate() {
        return dateCreated;
    }

    /**
     * date created setter
     * @param dateCreated
     */
    public void setDate(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    /**
     * date last modified getter
     * @return dateLastModified
     */
    public Date getDateLastModified() {
        return dateLastModified;
    }

    /**
     * date last modified setter
     * @param dateLastModified
     */
    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    /**
     * for displaying problem object
     * @return formated string: title
     */
    public String toString(){
        return this.title;
    }

    public void clearArrays() {
        this.records.clear();
    }

}
