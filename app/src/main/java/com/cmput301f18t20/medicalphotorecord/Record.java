package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.Date;

public class Record {
    protected String comment, title, createdByUserID;
    protected Date date = new Date(System.currentTimeMillis());

    Record(String creatorUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(creatorUserID);
        this.setTitle(title);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public String getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(String createdByUserID)
            throws UserIDMustBeAtLeastEightCharactersException {
        if (createdByUserID.length() >= 8) {
            this.createdByUserID = createdByUserID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws TitleTooLongException {
        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
