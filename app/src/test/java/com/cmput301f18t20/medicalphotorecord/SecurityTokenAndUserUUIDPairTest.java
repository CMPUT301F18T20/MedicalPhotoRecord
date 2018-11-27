package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityTokenAndUserUUIDPairTest {

    @Test
    public void getUserSecurityToken() {
        SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair
                = new SecurityTokenAndUserUUIDPair("");

        //make sure security token is generated on init
        assertNotEquals("security token was null",
                null, securityTokenAndUserUUIDPair.getUserSecurityToken());

    }

    @Test
    public void getUserUUID() {
        String userUUID = "myGreatUserUUID";
        SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair
                = new SecurityTokenAndUserUUIDPair(userUUID);

        //make sure userUUID is properly stored
        assertEquals("UserUUID changed", userUUID, securityTokenAndUserUUIDPair.getUserUUID());
    }
}