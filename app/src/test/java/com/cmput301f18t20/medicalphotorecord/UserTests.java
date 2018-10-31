package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Just testing basic getters and setters; not really needed but just for completeness
public class UserTests {

    @Test
    public void test_Setters_Getters() throws NonNumericUserIDException {

        // Check for user class
        String userId = "0000";
        String userEmail = "user_email@email.com";
        String userPhoneNumber = "1111111111";

        User user = new User();
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

        Patient patient = new Patient();
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

        Provider provider = new Provider();
        provider.setUserID(providerId);
        provider.setEmail(providerEmail);
        provider.setPhoneNumber(providerPhoneNumber);

        assertEquals("compare provider class user id", providerId, provider.getUserID());
        assertEquals("compare provider class email", providerEmail, provider.getEmail());
        assertEquals("compare provider class phone nubmer", providerPhoneNumber, provider.getPhoneNumber());

    }
}
