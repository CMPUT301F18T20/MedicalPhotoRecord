package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class SecurityTokenTest {

    @Test
    public void getUserSecurityToken() {
        SecurityToken securityToken
                = new SecurityToken("", PATIENT);

        //make sure security token is generated on init
        assertNotEquals("security token was null",
                null, securityToken.getUserSecurityToken());

    }

    @Test
    public void getUserID() {
        String userID = "myGreatUserID";
        SecurityToken securityToken
                = new SecurityToken(userID, PROVIDER);

        //make sure userID is properly stored
        assertEquals("UserID changed", userID, securityToken.getUserID());
    }
}