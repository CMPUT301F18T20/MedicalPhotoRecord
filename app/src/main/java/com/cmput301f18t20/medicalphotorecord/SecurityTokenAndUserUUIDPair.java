package com.cmput301f18t20.medicalphotorecord;

import Enums.USER_TYPE;

public class SecurityTokenAndUserUUIDPair {

    private String UserUUID;
    private String UserSecurityToken = java.util.UUID.randomUUID().toString(); //assign random UUID
    private USER_TYPE userType;

    public SecurityTokenAndUserUUIDPair(String UserUUID, USER_TYPE userType) {
        this.UserUUID = UserUUID;
        this.userType = userType;
    }

    public String getUserSecurityToken() {
        return UserSecurityToken;
    }

    public String getUserUUID() {
        return UserUUID;
    }

    public USER_TYPE getUserType() {
        return userType;
    }
}
