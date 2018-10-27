package com.cmput301f18t20.medicalphotorecord;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;

public class Entry {
    protected String title, createdByUserID;
    protected Date date = new Date(System.currentTimeMillis());;
    protected HashMap<String, Integer> keywordCounts;

    Entry() {}

    Entry(String creatorUserID) {
        try {
            this.setCreatedByUserID(creatorUserID);
        } catch (NonNumericUserIDException e) {
            //TODO: Handle exception
        }
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

    public HashMap<String, Integer> getKeywordCounts() {
        return keywordCounts;
    }

    /* XXX SetKeywordCounts should be implemented in any subclass as "updateIndex" and
     * be based on all the fields that subclass has
     */
}
