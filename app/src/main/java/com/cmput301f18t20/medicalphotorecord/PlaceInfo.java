/*
 * Class name: PlaceInfo
 *
 * Version: Version 1.0
 *
 * Developed by Google API sample
 *
 * Last Modified: 11/27/18 7:50 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */
package com.cmput301f18t20.medicalphotorecord;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * PlaceInfo class, contains name,address,phoneNumber,id,websiteUri,latlng,rating,attributions provided by google API
 *
 * @version 1.0
 * @see GeoLocation
 * @see java.util.Map
 * @see Record
 * @since 1.0
 */
//This Class is a sample from Google place api developer.android.
public class PlaceInfo {

    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri websiteUri;
    private LatLng latlng;
    private float rating;
    private String attributions;

    /**
     * Photo constructor: automatically save bitmap as string so that photo object can be saved online & offline
     * @param name
     * @param address
     * @param phoneNumber: provided phone
     * @param id  : id in google api
     * @param websiteUri: use for redirect website info
     * @param latlng
     * @param rating
     * @param attributions
     */
    public PlaceInfo(String name, String address, String phoneNumber, String id, Uri websiteUri,
                     LatLng latlng, float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latlng = latlng;
        this.rating = rating;
        this.attributions = attributions;
    }

    public PlaceInfo() {

    }

    /**
     * getName getter
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * getAddress getter
     * @return address
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getPhoneNumber getter
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * getId getter
     * @return id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * getWebsiteUri getter
     * @return websiteuri
     */
    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    /**
     * getLatlng getter
     * @return latlng
     */
    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    /**
     * getRating getter
     * @return rating
     */
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * getAttributions getter
     * @return attributions
     */
    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    /**
     * toString convert previous attributs to String
     * @return String
     */
    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latlng=" + latlng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}