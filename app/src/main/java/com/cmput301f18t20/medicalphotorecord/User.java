package com.cmput301f18t20.medicalphotorecord;

import org.apache.commons.lang3.StringUtils;

import io.searchbox.annotations.JestId;

public class User {
    @JestId
    protected String UserID;

    protected String email, phoneNumber;
    protected static QueryTool queryTool = new QueryTool();

    User() {}

    public User(String Userid, String email, String phoneNumber) {
        this.UserID = Userid;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String UserID) {
        try {
            this.setUserID(UserID);
        } catch (NonNumericUserIDException e) {
            //TODO: Handle exception
        }
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) throws NonNumericUserIDException {
        if (StringUtils.isNumeric(userID)) {
            UserID = userID;
        } else {
            throw new NonNumericUserIDException();
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
