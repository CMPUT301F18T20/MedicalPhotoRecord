/*
 * Class name: Record
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

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class Record implements Serializable {

    @JestId
    protected final String UUID = java.util.UUID.randomUUID().toString();
    protected String associatedProblemUUID, createdByUserID, comment, title, description;
    protected Date
            dateCreated = new Date(System.currentTimeMillis()),
            dateLastModified = new Date(System.currentTimeMillis());
    protected GeoLocation geoLocation;

    /**
     * Record constructor: set user id and title
     * @param creatorUserID
     * @param title
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws TitleTooLongException
     */
    public Record(String creatorUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(creatorUserID);
        this.setTitle(title);
    }

    /**
     * uuid getter
     * @return UUID
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * problem uuid getter
     * @return associatedProblemUUID
     */
    //TODO NEEDS TESTING
    public String getAssociatedProblemUUID() {
        return associatedProblemUUID;
    }

    /**
     * problem uuid setter
     * @param associatedProblemUUID
     */
    //TODO NEEDS TESTING
    public void setAssociatedProblemUUID(String associatedProblemUUID) {
        this.associatedProblemUUID = associatedProblemUUID;
    }

    /**
     * description getter
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * comment getter: from provider's added record
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * commet setter: from provider's added record
     * @param comment
     * @throws CommentTooLongException: thrown when > 300 characters
     */
    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    /**
     * userId getter
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
     * @throws TitleTooLongException: thrown when > 30 characters
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
     * geolocation getter
     * @return geoLocation
     */
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    /**
     * geolocation setter
     * @param geoLocation
     */
    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * for display of record object
     * @return formated string: title | date created | user id
     */
    public String toString(){
        return this.title + " | " + this.dateCreated.toString() + " | " + this.createdByUserID ;
    }
}
