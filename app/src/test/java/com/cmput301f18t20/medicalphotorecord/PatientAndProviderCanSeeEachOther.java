package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class PatientAndProviderCanSeeEachOther {
    protected static String patientId = "12345678";
    protected static String providerId = "abcdefgh";

    @Test(expected = Test.None.class /* no exception expected */)
    public void AssignPatientByUserID() throws UserIDMustBeAtLeastEightCharactersException {

        // Init variables for later comparision of patient
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "1234567890";
        Patient patient = new Patient(patientId, patientEmail, patientPhoneNumber);

        // Init variables for later comparision of provider
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "1234567891";
        Provider provider = new Provider(providerId, providerEmail, providerPhoneNumber);

        // can fetch and add patient by user id using the controller, assume unique user id
        provider.assignPatient(patientId);
        Patient patientGot = provider.getPatient(patientId);

        assertEquals("compare patient userId", patientId, patientGot.getUserID());
        assertEquals("compare patient email", patientEmail, patientGot.getEmail());
        assertEquals("compare patient phone number", patientPhoneNumber, patientGot.getPhoneNumber());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testAssignPatientPatientSeesProvider() throws UserIDMustBeAtLeastEightCharactersException {

        // Init variables for later comparision of patient
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "1234567890";
        Patient patient = new Patient(patientId, patientEmail, patientPhoneNumber);

        // Init variables for later comparision of provider
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "1234567891";
        Provider provider = new Provider(providerId, providerEmail, providerPhoneNumber);

        // can fetch and add patient by user id using the controller, assume unique user id
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
    public void testUnassignPatientRemovesProviderFromPatient()
            throws UserIDMustBeAtLeastEightCharactersException {

        // Init patients and provider
        Patient patient = new Patient(patientId, "patient_email@email.com", "1234567890");
        Provider provider = new Provider(providerId, "provider_email@email.com", "1111111111");

        // add patient to provider
        provider.assignPatient(patient);

        // patient is there
        Patient patientGot = provider.getPatient(patientId);
        assertEquals("Patient not added correctly to provider",
                provider.getPatient(patient.getUserID()), patient);

        //provider is there
        Provider providerGot = patient.getProvider(providerId);
        assertEquals("Provider not added assigned to patient in database",
                patient.getProvider(provider.getUserID()), provider);

        //remove patient
        provider.unAssignPatient(patient);

        // patient is not there, check for exception
        try {
            patientGot = provider.getPatient(patientId);
            fail("No exception raised");
        } catch (NoSuchElementException e) {
            assertEquals("Patient not found", e.getMessage());
        }

        // Check if patient's list of providers do not have the provider (by userId, assume distinct userId)
        try {
            providerGot = patient.getProvider(providerId);
            fail("No exception raised");
        } catch (NoSuchElementException e) {
            assertEquals("Provider not found", e.getMessage());
        }
    }

}
