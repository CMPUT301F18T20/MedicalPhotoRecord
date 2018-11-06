package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import io.searchbox.annotations.JestId;

public class Record {
    @JestId
    protected String createdByUserID;

    protected String title;
    protected Date date = new Date(System.currentTimeMillis());

    Record() {
        super();
    }

    Record(String creatorUserID)  {
        this.createdByUserID = creatorUserID;
    }

    public Record(String createdByUserID, String title) throws NonNumericUserIDException {
        this.setCreatedByUserID(createdByUserID);
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(String createdByUserID) throws NonNumericUserIDException {
        if (StringUtils.isNumeric(createdByUserID)) {
            this.createdByUserID = createdByUserID;
        } else {
            throw new NonNumericUserIDException();
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
    String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }
}
