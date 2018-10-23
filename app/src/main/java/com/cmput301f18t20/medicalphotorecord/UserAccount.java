package com.cmput301f18t20.medicalphotorecord;

public class UserAccount extends ContactInformation {
    protected int UserID;

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getUserID() {
        return UserID;
    }
}
