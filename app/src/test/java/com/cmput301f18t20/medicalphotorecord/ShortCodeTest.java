package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static Enums.USER_TYPE.PATIENT;

public class ShortCodeTest {

    @Test
    public void getSecurityToken() {

        //create the security token to be stored
        SecurityToken securityToken =
                new SecurityToken("", PATIENT);

        //create the ShortCode
        ShortCode securityCodeSecurityTokenPair =
                new ShortCode("", securityToken);


    }
}