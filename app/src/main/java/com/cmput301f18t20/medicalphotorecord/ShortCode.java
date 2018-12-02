package com.cmput301f18t20.medicalphotorecord;

import android.support.annotation.NonNull;

import java.util.Random;

/**
 * A short code that gets put into the elasticsearch index when the user wants to log in on another
 * device.  If the user enters the value held in "shortSecurityCode" member, it will give
 * them "securityToken" which will allow them to login to the UserID held in the securityToken.
 * @See SecurityToken
 */
public class ShortCode {
    //model objects
    private String shortSecurityCode = createSecurityCode();
    private SecurityToken securityToken;
    
    //static members outlining parameters to createSecurityCode()
    public static final int securityCodeLength = 5;
    private static final Random random = new Random();
    private static final String charSet = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";

    /**
     * A constructor for Short Code which must be provided an associated securityToken
     * @param securityToken SecurityToken object to track
     */
    public ShortCode(@NonNull SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    /** fetch the stored security token
     * @return associated security token
     */
    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    /** fetch the short security code
     * @return associated short security code
     */
    public String getShortSecurityCode() {
        return shortSecurityCode;
    }

    /** Generates the "shortSecurityCode" member as specified by "securityCodeLength" and "charSet"
     * @return a valid "securityCodeLength" digit code composed of "charSet"
     */
    private static String createSecurityCode() {
        StringBuilder token = new StringBuilder(securityCodeLength);
        for (int i = 0; i < securityCodeLength; i++) {
            token.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return token.toString();
    }
}
