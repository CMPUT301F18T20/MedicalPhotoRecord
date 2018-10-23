package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.Date;

public class Record extends PhotoManager {
    String title, comment;
    Date date;
    //body location photos? XXX
    Location geolocation;

    public String getTitle() {
        return title;
    }

    public Date getDate() {
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

    public void setDate(Date date) {
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
