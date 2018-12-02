/*
 * Class name: SearchableObject
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 1:00 PM
 *
 * Last Modified: 02/12/18 1:00 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import java.util.Date;
import java.util.Observable;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class SearchableObject {
    protected String title, createdByUserID;
    protected Date dateCreated = new Date(System.currentTimeMillis());

    /**
     * userID getter
     * @return createdByUserID
     */
    public String getCreatedByUserID() {
        return createdByUserID;
    }

    /**
     * userID setter
     * @param createdByUserID UserID of user who created this object
     * @throws UserIDMustBeAtLeastEightCharactersException when UserID is < 8 characters
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
     * @param title set title of the object
     * @throws TitleTooLongException > 30 characters
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
     * @param dateCreated set dateCreated of the object
     */
    public void setDate(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    /**
     * return formated string: title | date created | user id
     * @return formatted string representation of the object
     */
    public String toString(){
        return this.title + " | " + this.dateCreated.toString() + " | " + this.createdByUserID ;
    }
}
