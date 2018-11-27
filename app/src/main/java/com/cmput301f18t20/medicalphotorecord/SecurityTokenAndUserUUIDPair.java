package com.cmput301f18t20.medicalphotorecord;

public class SecurityTokenAndUserUUIDPair {

    private String UserUUID;
    private String UserSecurityToken = java.util.UUID.randomUUID().toString(); //assign random UUID

    public SecurityTokenAndUserUUIDPair(String UserUUID) {
        this.UserUUID = UserUUID;
    }

    public String getUserSecurityToken() {
        return UserSecurityToken;
    }

    public String getUserUUID() {
        return UserUUID;
    }
}
