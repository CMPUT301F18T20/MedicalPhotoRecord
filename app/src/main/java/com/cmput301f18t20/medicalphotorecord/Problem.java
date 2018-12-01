/*
 * Class name: Problem
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 7:47 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class Problem implements Serializable {
    @JestId
    protected final String UUID = java.util.UUID.randomUUID().toString();
    protected ArrayList<Record> records = new ArrayList<>();
    protected String description, title, createdByUserID;
    protected Date
            dateCreated = new Date(System.currentTimeMillis()),
            dateLastModified = new Date(System.currentTimeMillis());

    /**
     * @param createdByUserID
     * @param title
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws TitleTooLongException
     */
    public Problem(String createdByUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(createdByUserID);
        this.setTitle(title);
    }

    /**
     * UUID getter
     * @return UUID of problem object
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * descripion getter
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description
     * @throws ProblemDescriptionTooLongException: thrown when > 300 characters
     */
    public void setDescription(String description) throws ProblemDescriptionTooLongException {
        if (description.length() > 300){
            throw new ProblemDescriptionTooLongException();
        }
        this.description = description;
    }

    /**
     * userID getter
     * @return createdByUserID
     */
    public String getCreatedByUserID() {
        return createdByUserID;
    }

    /**
     * userID setter
     * @param createdByUserID
     * @throws UserIDMustBeAtLeastEightCharactersException: thrown when < 8 characters
     */
    public void setCreatedByUserID(String createdByUserID)
            throws UserIDMustBeAtLeastEightCharactersException {
        if (createdByUserID.length() >= 8) {
            this.createdByUserID = createdByUserID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    /**
     * title getter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * title setter
     * @param title
     * @throws TitleTooLongException:  thrown when > 30 characters
     */
    public void setTitle(String title) throws TitleTooLongException {
        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    /**
     * date created getter
     * @return dateCreated
     */
    public Date getDate() {
        return dateCreated;
    }

    /**
     * date created setter
     * @param dateCreated
     */
    public void setDate(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * date last modified getter
     * @return dateLastModified
     */
    public Date getDateLastModified() {
        return dateLastModified;
    }

    /**
     * date last modified setter
     * @param dateLastModified
     */
    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    /**
     * for displaying problem object
     * @return formated string: title
     */
    public String toString(){
        return this.title;
    }

}
