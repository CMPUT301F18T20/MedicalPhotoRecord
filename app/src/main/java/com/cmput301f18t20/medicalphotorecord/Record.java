package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.Date;

public class Record extends Entry {
    String comment;
    PhotoList photoList;
    //TODO: body location photos
    Location geolocation;

    Record(String creatorUserID) {
        try {
            this.setCreatedByUserID(creatorUserID);
        } catch (NonNumericUserIDException e) {
            //TODO: Handle exception
        }
    }

    public String getComment() {
        return comment;
    }

    public PhotoList getPhotoList() {
        return photoList;
    }

    public Location getGeolocation() {
        return geolocation;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public void setGeolocation(Location geolocation) {
        /* limit for longitude is +- 180, latitude is +-90.
        TODO: setter should throw error on violating those */
        this.geolocation = geolocation;
    }
}
