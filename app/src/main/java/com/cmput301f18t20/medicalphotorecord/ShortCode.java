package com.cmput301f18t20.medicalphotorecord;

public class ShortCode {
    private String shortSecurityCode;
    private SecurityToken securityToken;

    public ShortCode(String shortSecurityCode, SecurityToken securityToken) {
        this.shortSecurityCode = shortSecurityCode;
        this.securityToken = securityToken;
    }

    public SecurityToken getSecurityToken() {
        return securityToken;
    }
}
