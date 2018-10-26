package com.cmput301f18t20.medicalphotorecord;

import org.apache.commons.lang3.StringUtils;

public class User {
    protected String UserID, email, phoneNumber;
    protected static QueryTool query = new QueryTool();

    User() {}

    User(String UserID) {
        try {
            this.setUserID(UserID);
        } catch (NonNumericUserIDException e) {
            //TODO: Handle exception
        }
    }

    public String getUserID() {
        return UserID;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public QueryTool getQuery() {
        return query;
    }

    public void setUserID(String userID) throws NonNumericUserIDException {
        if (StringUtils.isNumeric(userID)) {
            UserID = userID;
        } else {
            throw new NonNumericUserIDException();
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void deleteAccount() {}
}
