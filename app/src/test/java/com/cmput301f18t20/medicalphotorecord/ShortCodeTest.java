package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static Enums.USER_TYPE.PATIENT;
import static org.junit.Assert.assertEquals;

public class ShortCodeTest {

    @Test
    public void getSecurityToken() {

        //create the security token to be stored
        SecurityToken securityToken =
                new SecurityToken("", PATIENT);

        //create the ShortCode
        ShortCode shortCode =
                new ShortCode(securityToken);

        //check the short code has properly stored the security token
        assertEquals(shortCode.getSecurityToken().getUserSecurityToken(),
                securityToken.getUserSecurityToken());
    }
}