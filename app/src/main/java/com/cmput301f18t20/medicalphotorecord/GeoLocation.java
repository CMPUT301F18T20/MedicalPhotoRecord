/*
 * Class name: GeoLocation
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/27/18 6:21 PM
 *
 * Last Modified: 11/27/18 7:50 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import java.io.Serializable;

/**
 * GeoLocation class
 *
 * @version 1.0
 * @see User
 * @see Problem
 * @see Record
 * @since 1.0
 */
public class GeoLocation implements Serializable {

    protected final String UUID = java.util.UUID.randomUUID().toString();
    private String recordUUID;
    private String problemUUID;
    private double longitude;
    private double latitude;
    private String address;

    //Constructor method
    public GeoLocation() {}

    /**
     * Photo constructor: automatically save bitmap as string so that photo object can be saved online & offline
     * @param recordUUID
     * @param problemUUID
     * @param latitude,longtitude,address
     */

    public GeoLocation(String recordUUID, String problemUUID, double latitude, double longitude, String address){
        this.recordUUID = recordUUID;
        this.problemUUID = problemUUID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    //methods
    /**
     * getUUID
     * Used for get the UUID of the geo
     * @return UUID
     */
    public String getUUID(){ return this.UUID;}

    /**
     * getRecordUUID
     * Used for get the RecordUUID of the geo
     * @return RecordUUID
     */
    public String getRecordUUID(){
        return this.recordUUID;
    }

    /**
     * getProblemUUID
     * Used for get the ProblemUUID of the geo
     * @return ProblemUUID
     */
    public String getProblemUUID() { return this.problemUUID;}

    /**
     * getLongitude
     * Used for get the Longitude of the geo
     * @return longitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * getLatitude
     * Used for get the Latitude of the geo
     * @return Latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * getAddress
     * Used for get the Address of the geo
     * @return Address
     */
    public String getAddress() {
        return this.address;
    }


    /**
     * setLongitude
     * Used for set the Longitude
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    /**
     * setLatitude
     * Used for set the Latitude
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    /**
     * setAddress
     * Used for set the address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * setRecordUUID
     * Used for set the recordUUId
     * @param recordUUID
     */
    public void setRecordUUID(String recordUUID){
        this.recordUUID = recordUUID;
    }


    /**
     * setProblemUUID
     * Used for set the ProblemUUID
     * @param problemUUID
     */
    public void setProblemUUID(String problemUUID){
        this.problemUUID = problemUUID;
    }

}
