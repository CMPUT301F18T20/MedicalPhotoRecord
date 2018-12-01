/*
 * Class name: User
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 7:49 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import java.util.Date;
import java.util.UUID;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class User {
    @JestId
    protected final String UUID = java.util.UUID.randomUUID().toString();
    protected String UserID, email, phoneNumber;
    protected Date dateLastModified = new Date(System.currentTimeMillis());

    /**
     * User constructor: sets user id
     * @param Userid
     * @throws UserIDMustBeAtLeastEightCharactersException: thrown when < 8 characters
     */
    public User(String Userid) throws UserIDMustBeAtLeastEightCharactersException {
        this.setUserID(Userid);

    }

    /**
     * User constructor: sets userId, email, phone
     * @param Userid
     * @param email
     * @param phoneNumber
     * @throws UserIDMustBeAtLeastEightCharactersException: thrown when < 8 characters
     */
    public User(String Userid, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        this(Userid);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * uuid getter
     * @return UUID
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * user id getter
     * @return UserID
     */
    public String getUserID() {
        return UserID;
    }

    /**
     * user id setter
     * @param userID
     * @throws UserIDMustBeAtLeastEightCharactersException: thrown when < 8 characters
     */
    public void setUserID(String userID) throws UserIDMustBeAtLeastEightCharactersException {
        if (userID.length() >= 8) {
            this.UserID = userID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    /**
     * email getter
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * email setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * phone number getter
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * phone number setter
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * for display user object
     * @return formatted string: userID | email | phone number
     */
    public String toString(){
        return this.UserID + " " + this.email + " " + this.phoneNumber;
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
}
