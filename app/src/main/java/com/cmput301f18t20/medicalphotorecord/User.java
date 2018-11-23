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

import java.util.UUID;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class User {
    @JestId
    protected String ElasticSearchID;
    protected String UserID, email, phoneNumber;
    public UUID uuid = java.util.UUID.randomUUID();

    public User(String Userid) throws UserIDMustBeAtLeastEightCharactersException {
        this.setUserID(Userid);

    }

    public User(String Userid, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        this(Userid);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) throws UserIDMustBeAtLeastEightCharactersException {
        if (userID.length() >= 8) {
            this.UserID = userID;
        } else {
            throw new UserIDMustBeAtLeastEightCharactersException();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString(){
        return this.UserID + " " + this.email + " " + this.phoneNumber;
    }

    public String getElasticSearchID() {
        return ElasticSearchID;
    }

    public void setElasticSearchID(String elasticSearchID) {
        ElasticSearchID = elasticSearchID;
    }


}
