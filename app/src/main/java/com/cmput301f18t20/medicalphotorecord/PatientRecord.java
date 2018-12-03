/*
 * Class name: PatientRecord
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

import java.util.ArrayList;
import java.util.UUID;

import Exceptions.TitleTooLongException;
import Exceptions.TooManyPhotosForSinglePatientRecord;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

/**
 * Patient record class, sub class of record, contains geolocation and body locations
 *
 * @version 1.0
 * @see User
 * @see Patient
 * @see Record
 * @since 1.0
 */
public class PatientRecord extends Record {

    protected Location geolocation;
    final protected static int MAX_PHOTOS = 10;
    protected String bodyLocation;

    /** Constructor to build a patient record with user id and title
     * @param creatorUserID, title
     * @throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException
     */
    public PatientRecord(String creatorUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        super(creatorUserID, title);
    }

    /**
     * Geolocation getter
     * @return geolocation object
     */
    public Location getGeolocation() {
        return geolocation;
    }


    /**
     * Geolocation setter
     * @param geolocation
     */
    public void setGeolocation(Location geolocation) {
        /* limit for longitude is +- 180, latitude is +-90.
        TODO: setter should throw error on violating those */
        this.geolocation = geolocation;
    }

    /**
     * Body location getter based on index of the list
     * @return bodyLocation object
     */
    public String getBodyLocation() {
        return this.bodyLocation;
    }

    /**
     * Body location setter
     * @param bodyLocation
     */
    public void setBodyLocation(String bodyLocation) {
        this.bodyLocation= bodyLocation;
    }

    /**
     * Used for display patient record object
     * @return formated string: userId | title | description | date
     */
    @Override
    public String toString() {
        return this.createdByUserID + " | " + this.title + " | " + this.description + " | " + this.dateCreated.toString();
    }
}
