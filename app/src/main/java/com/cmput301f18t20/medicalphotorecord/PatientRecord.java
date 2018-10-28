package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.security.keystore.KeyNotYetValidException;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientRecord extends Record {
    ArrayList<Photo> photos;
    Location geolocation;
    ArrayList<BodyLocation> bodyLocations;
    protected HashMap<String, HashMap> keywordStorage = new HashMap<>();
    protected HashMap<String, Integer> geoKeywordCounts = new HashMap<>();
    protected HashMap<String, Integer> bodyLocationKeywordCounts = new HashMap<>();

    PatientRecord(String creatorUserID) {
        super(creatorUserID);
    }

    public Photo getPhoto(int photoIndex) {
        return this.photos.get(photoIndex);
    }

    public void setPhoto(Photo photo, int photoIndex) {
        this.photos.set(photoIndex, photo);
        //TODO: commit changes to disk/network
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        //TODO: commit changes to disk/network
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        //TODO: commit changes to disk/network
    }

    public void removePhoto(int photoIndex) {
        photos.remove(photoIndex);
        //TODO: commit changes to disk/network
    }

    public Location getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Location geolocation) {
        /* limit for longitude is +- 180, latitude is +-90.
        TODO: setter should throw error on violating those */
        this.geolocation = geolocation;
    }

    public BodyLocation getBodyLocation(int bodyLocationIndex) {
        return this.bodyLocations.get(bodyLocationIndex);
    }

    public void setBodyLocation(BodyLocation bodyLocation, int bodyLocationIndex) {
        this.bodyLocations.set(bodyLocationIndex, bodyLocation);
        //TODO: commit changes to disk/network
    }

    public ArrayList<BodyLocation> getBodyLocations() {
        return bodyLocations;
    }

    public void addBodyLocation(BodyLocation bodyLocation) {
        bodyLocations.add(bodyLocation);
        //TODO: commit changes to disk/network
    }

    public void removeBodyLocation(BodyLocation bodyLocation) {
        bodyLocations.remove(bodyLocation);
        //TODO: commit changes to disk/network
    }

    public void removeBodyLocation(int bodyLocationIndex) {
        bodyLocations.remove(bodyLocationIndex);
        //TODO: commit changes to disk/network
    }

    @Override
    public void updateIndex() {
        /* Update keywordCounts based on Title, Date, Comment,
        CreatedByUserID
        bodyLocationKeywordCounts based on bodylocation
        geoKeywordCounts based on geo.
        So a filter can decide which sets of keywords are interesting to us */
        //TODO: Update actual counts before packing them into keywordStorage
        keywordStorage.put("Geo", geoKeywordCounts);
        keywordStorage.put("BodyLocation", bodyLocationKeywordCounts);
        keywordStorage.put("EverythingElse", keywordCounts);
    }
}
