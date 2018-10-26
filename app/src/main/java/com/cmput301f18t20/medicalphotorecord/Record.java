package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.Date;

public class Record {
    String title, comment, CreatedByUserID;
    Date date;
    PhotoList photoList;
    //TODO: body location photos
    Location geolocation;

    Record(String UserIDOfCreator) {
        CreatedByUserID = UserIDOfCreator;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedByUserID() {
        return CreatedByUserID;
    }

    public Date getDate() {
        return date;
    }

    public PhotoList getPhotoList() {
        return photoList;
    }

    public Location getGeolocation() {
        return geolocation;
    }

    public void setTitle(String title) throws TitleTooLongException {
        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGeolocation(Location geolocation) {
        /* limit for longitude is +- 180, latitude is +-90.
        TODO: setter should throw error on violating those */
        this.geolocation = geolocation;
    }
}
