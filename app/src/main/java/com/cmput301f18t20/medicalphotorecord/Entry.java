package com.cmput301f18t20.medicalphotorecord;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Entry {
    protected String title, createdByUserID;
    protected Date date = new Date(System.currentTimeMillis());

    Entry() {}

    public Entry(String createdByUserID, String title) {
        this.createdByUserID = createdByUserID;
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    Entry(String creatorUserID) throws NonNumericUserIDException {
        this.setCreatedByUserID(creatorUserID);
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
}
