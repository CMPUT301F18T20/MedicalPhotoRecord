package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.Date;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Just testing basic getters and setters; not really needed but just for completeness
public class UserTests {
    protected static String Correct_userId = "00001111";
    protected static String Correct_userId2 = "11110000";
    protected static String Correct_userId3 = "00001113331";

    @Test(expected = Test.None.class )
    public void test_Setters_Getters() throws UserIDMustBeAtLeastEightCharactersException {

        // Check for user class
        String userId = "103103103";
        String userEmail = "user_email@email.com";
        String userPhoneNumber = "1111111111";
        Date userDateLastModified = new Date(System.currentTimeMillis());

        User user = new User(Correct_userId, "1111@email.com", "1111");
        user.setUserID(userId);
        user.setEmail(userEmail);
        user.setPhoneNumber(userPhoneNumber);
        user.setDateLastModified(userDateLastModified);

        assertEquals("compare user class user id", userId, user.getUserID());
        assertEquals("compare user class email", userEmail, user.getEmail());
        assertEquals("compare user class phone nubmer", userPhoneNumber, user.getPhoneNumber());
        assertEquals("compare user class dateLastModified",
                userDateLastModified, user.getDateLastModified());

        // Check for patient class
        String patientId = "00010001";
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "22222222222";
        Date patientDateLastModified = new Date(System.currentTimeMillis());


        Patient patient = new Patient(Correct_userId2, "2222@email.com", "2222");
        patient.setUserID(patientId);
        patient.setEmail(patientEmail);
        patient.setPhoneNumber(patientPhoneNumber);
        patient.setDateLastModified(patientDateLastModified);

        assertEquals("compare patient class user id", patientId, patient.getUserID());
        assertEquals("compare patient class email", patientEmail, patient.getEmail());
        assertEquals("compare patient class phone nubmer", patientPhoneNumber, patient.getPhoneNumber());
        assertEquals("compare user class dateLastModified",
                patientDateLastModified, patient.getDateLastModified());

        // Check for provider class
        String providerId = "00020001";
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "3333333333";
        Date providerDateLastModified = new Date(System.currentTimeMillis());


        Provider provider = new Provider(Correct_userId3, "3333@email.com", "3333");
        provider.setUserID(providerId);
        provider.setEmail(providerEmail);
        provider.setPhoneNumber(providerPhoneNumber);
        provider.setDateLastModified(providerDateLastModified);

        assertEquals("compare provider class user id", providerId, provider.getUserID());
        assertEquals("compare provider class email", providerEmail, provider.getEmail());
        assertEquals("compare provider class phone nubmer", providerPhoneNumber, provider.getPhoneNumber());
        assertEquals("compare provider class dateLastModified",
                providerDateLastModified, provider.getDateLastModified());

    }

    @Test
    public void testConstructor() throws UserIDMustBeAtLeastEightCharactersException {

        // Check setUserId for numerically user id value
        User user = new User(Correct_userId, "1111@email.com", "1111");
        Patient patient = new Patient(Correct_userId2, "2222@email.com", "2222");
        Provider provider = new Provider(Correct_userId);

        try{
            user.setUserID("abc");
            fail("Exception not triggered");
        }catch (UserIDMustBeAtLeastEightCharactersException e){
            assertEquals("User id needs to be at least 8 characters", e.getMessage());
        }

        try{
            patient.setUserID("abc");
            fail("Exception not triggered");
        }catch (UserIDMustBeAtLeastEightCharactersException e){
            assertEquals("User id needs to be at least 8 characters", e.getMessage());
        }

        try{
            provider.setUserID("abc");
            fail("Exception not triggered");
        }catch (UserIDMustBeAtLeastEightCharactersException e){
            assertEquals("User id needs to be at least 8 characters", e.getMessage());
        }
    }
}
