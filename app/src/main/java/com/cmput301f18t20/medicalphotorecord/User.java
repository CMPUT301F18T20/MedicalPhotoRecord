package com.cmput301f18t20.medicalphotorecord;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.annotations.JestId;

public class User {
    @JestId
    protected String UserID;

    protected String email, phoneNumber;
    protected static QueryTool queryTool = new QueryTool();

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

    public QueryTool getQuery() {
        return queryTool;
    }
}
