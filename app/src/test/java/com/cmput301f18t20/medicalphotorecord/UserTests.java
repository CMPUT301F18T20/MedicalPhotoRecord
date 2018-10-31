package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Just testing basic getters and setters; not really needed but just for completeness
public class UserTests {

    @Test
    public void test_Setters_Getters(){

        // Check for user class
        String userId = "0000";
        String userEmail = "user_email@email.com";
        String userPhoneNumber = "1111111111";

        User user = new User("1111", "1111@email.com", "1111");
        user.setUserID(userId);
        user.setEmail(userEmail);
        user.setPhoneNumber(userPhoneNumber);

        assertEquals("compare user class user id", userId, user.getUserID());
        assertEquals("compare user class email", userEmail, user.getEmail());
        assertEquals("compare user class phone nubmer", userPhoneNumber, user.getPhoneNumber());

        // Check for patient class
        String patientId = "0001";
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "22222222222";

        Patient patient = new Patient("2222", "2222@email.com", "2222");
        patient.setUserID(patientId);
        patient.setEmail(patientEmail);
        patient.setPhoneNumber(patientPhoneNumber);

        assertEquals("compare patient class user id", patientId, patient.getUserID());
        assertEquals("compare patient class email", patientEmail, patient.getEmail());
        assertEquals("compare patient class phone nubmer", patientPhoneNumber, patient.getPhoneNumber());


        // Check for provider class
        String providerId = "0002";
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "3333333333";

        Provider provider = new Provider("3333", "3333@email.com", "3333");
        provider.setUserID(providerId);
        provider.setEmail(providerEmail);
        provider.setPhoneNumber(providerPhoneNumber);

        assertEquals("compare provider class user id", providerId, provider.getUserID());
        assertEquals("compare provider class email", providerEmail, provider.getEmail());
        assertEquals("compare provider class phone nubmer", providerPhoneNumber, provider.getPhoneNumber());

        // Check setUserId for numerically user id value
        User user1 = new User("1111", "1111@email.com", "1111");
        Patient patient1 = new Patient("2222", "2222@email.com", "2222");
        Provider provider1 = new Provider();

        try{
            user.setUserID("abc");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }

        try{
            patient.setUserID("abc");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }

        try{
            provider.setUserID("abc");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }
    }

    @Test
    public void testConstructor(){

        // Check constructor for numerically user id value
        try{
            User user = new User("abc", "1111@email.com", "1111");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }

        try{
            Patient patient = new Patient("abc", "2222@email.com", "2222");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }

        try{
            Provider provider = new Provider2("abc", "3333@email.com", "3333");
        }catch (NonNumericUserIDException e){
            assertEquals("User id needs to be a numerical value", e.getMessage());
        }
    }
}
