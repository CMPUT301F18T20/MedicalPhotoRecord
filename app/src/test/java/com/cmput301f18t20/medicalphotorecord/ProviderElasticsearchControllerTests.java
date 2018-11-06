package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProviderElasticsearchControllerTests {
    protected static String patientId = "12345678";
    protected static String providerId = "abcdefgh";

    @Test(expected = Test.None.class /* no exception expected */)
    public void testAssignPatient() throws UserIDMustBeAtLeastEightCharactersException {

        // Init variables for later comparision of patient
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "1234567890";
        Patient patient = new Patient(patientId, patientEmail, patientPhoneNumber);

        // Init variables for later comparision of provider
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "1234567891";
        Provider provider = new Provider(providerId, providerEmail, providerPhoneNumber);


        // can fetch a patient by user id using the controller, assume unique user id
        provider.assignPatient(patientId);
        Patient patientGot = provider.getPatient(patientId);

        assertEquals("compare patient userId", patientId, patientGot.getUserID());
        assertEquals("compare patient email", patientEmail, patientGot.getEmail());
        assertEquals("compare patient phone number", patientPhoneNumber, patientGot.getPhoneNumber());


        // Check if patient's list of provider has provider (by userId, assume distinct userId)
        Provider providerGot = patient.getProvider(providerId);

        assertEquals("compare provider userId", providerId, providerGot.getUserID());
        assertEquals("compare provider email", providerEmail, providerGot.getEmail());
        assertEquals("compare provider phone number", providerPhoneNumber, providerGot.getPhoneNumber());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void patientCanFindProvider() throws UserIDMustBeAtLeastEightCharactersException {

        // Init variables for later comparision of patient
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "1234567890";
        Patient patient = new Patient(patientId, patientEmail, patientPhoneNumber);

        // Init variables for later comparision of provider
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "1234567891";
        Provider provider = new Provider(providerId, providerEmail, providerPhoneNumber);

        // can fetch a patient by user id using the controller, assume unique user id
        provider.assignPatient(patientId);

        // Check if patient's list of provider can now see provider (by userId, assume distinct userId)
        Provider providerGot = patient.getProvider(providerId);

        assertEquals("compare provider userId", providerId, providerGot.getUserID());
        assertEquals("compare provider email", providerEmail, providerGot.getEmail());
        assertEquals("compare provider phone number", providerPhoneNumber, providerGot.getPhoneNumber());
    }

}
