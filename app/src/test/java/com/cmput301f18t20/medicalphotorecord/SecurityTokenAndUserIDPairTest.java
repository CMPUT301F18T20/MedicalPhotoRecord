package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import Enums.USER_TYPE;

import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class SecurityTokenAndUserIDPairTest {

    @Test
    public void getUserSecurityToken() {
        SecurityTokenAndUserIDPair securityTokenAndUserIDPair
                = new SecurityTokenAndUserIDPair("", PATIENT);

        //make sure security token is generated on init
        assertNotEquals("security token was null",
                null, securityTokenAndUserIDPair.getUserSecurityToken());

    }

    @Test
    public void getUserID() {
        String userID = "myGreatUserID";
        SecurityTokenAndUserIDPair securityTokenAndUserIDPair
                = new SecurityTokenAndUserIDPair(userID, PROVIDER);

        //make sure userID is properly stored
        assertEquals("UserID changed", userID, securityTokenAndUserIDPair.getUserID());
    }
}