package com.cmput301f18t20.medicalphotorecord;

import java.util.Date;

public class Entry {
    protected String title, createdByUserID;
    protected Date date = new Date(System.currentTimeMillis());

    public Entry(String createdByUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(createdByUserID);
        this.setTitle(title);
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
