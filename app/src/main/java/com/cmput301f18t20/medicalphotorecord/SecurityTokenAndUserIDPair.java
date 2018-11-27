package com.cmput301f18t20.medicalphotorecord;

import Enums.USER_TYPE;

public class SecurityTokenAndUserIDPair {

    private String UserID;
    private String UserSecurityToken = java.util.UUID.randomUUID().toString(); //assign random UUID
    private USER_TYPE userType;

    public SecurityTokenAndUserIDPair(String UserID, USER_TYPE userType) {
        this.UserID = UserID;
        this.userType = userType;
    }

    public String getUserSecurityToken() {
        return UserSecurityToken;
    }

    public String getUserID() {
        return UserID;
    }

    public USER_TYPE getUserType() {
        return userType;
    }
}
