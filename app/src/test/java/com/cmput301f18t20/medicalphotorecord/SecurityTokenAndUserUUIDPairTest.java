package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import Enums.USER_TYPE;

import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class SecurityTokenAndUserUUIDPairTest {

    @Test
    public void getUserSecurityToken() {
        SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair
                = new SecurityTokenAndUserUUIDPair("", PATIENT);

        //make sure security token is generated on init
        assertNotEquals("security token was null",
                null, securityTokenAndUserUUIDPair.getUserSecurityToken());

    }

    @Test
    public void getUserUUID() {
        String userUUID = "myGreatUserUUID";
        SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair
                = new SecurityTokenAndUserUUIDPair(userUUID, PROVIDER);

        //make sure userUUID is properly stored
        assertEquals("UserUUID changed", userUUID, securityTokenAndUserUUIDPair.getUserUUID());
    }
}