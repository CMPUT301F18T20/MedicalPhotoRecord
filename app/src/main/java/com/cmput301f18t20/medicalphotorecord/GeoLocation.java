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

public class GeoLocation implements Serializable {

    protected final String UUID = java.util.UUID.randomUUID().toString();
    private String recordUUID;
    private String problemUUID;
    private double longitude;
    private double latitude;
    private String address;

    //Constructor method
    public GeoLocation() {}

    public GeoLocation(String recordUUID, String problemUUID, double latitude, double longitude, String address){
        this.recordUUID = recordUUID;
        this.problemUUID = problemUUID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    //methods
    public String getUUID(){ return this.UUID;}

    public String getRecordUUID(){
        return this.recordUUID;
    }

    public String getProblemUUID() { return this.problemUUID;}

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getAddress() {
        return this.address;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRecordUUID(String recordUUID){
        this.recordUUID = recordUUID;
    }

}


