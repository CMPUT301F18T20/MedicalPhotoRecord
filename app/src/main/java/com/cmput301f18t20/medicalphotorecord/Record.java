package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

public class Record extends PhotoManager {
    String title, date, comment;
    //body location photos? XXX
    Location geolocation;

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }
}
