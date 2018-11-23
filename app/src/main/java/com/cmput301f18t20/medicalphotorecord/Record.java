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
    protected String ElasticSearchID;
    protected String associatedProblemUUID, createdByUserID, comment, title, description;
    protected Date date = new Date(System.currentTimeMillis());
    protected final String UUID = java.util.UUID.randomUUID().toString();

    public Record(String creatorUserID, String title)
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        this.setCreatedByUserID(creatorUserID);
        this.setTitle(title);
    }

    public String getUUID() {
        return UUID;
    }

    //TODO NEEDS TESTING
    public String getAssociatedProblemUUID() {
        return associatedProblemUUID;
    }

    //TODO NEEDS TESTING
    public void setAssociatedProblemUUID(String associatedProblemUUID) {
        this.associatedProblemUUID = associatedProblemUUID;
    }
    //TODO NEEDS TESTING
    public String getElasticSearchID() {
        return ElasticSearchID;
    }
    //TODO NEEDS TESTING
    public void setElasticSearchID(String elasticSearchID) {
        ElasticSearchID = elasticSearchID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String toString(){
        return this.title;
    }
}
