package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProviderTests {
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

        //add patient
        provider.assignPatient(patient);

        //fetch newly added patient
        Patient patientGot = provider.getPatient(patient.getUserID());

        //
        assertEquals("compare patient userId", patient.getUserID(), patientGot.getUserID());
        assertEquals("compare patient email", patientEmail, patientGot.getEmail());
        assertEquals("compare patient phone number", patientPhoneNumber, patientGot.getPhoneNumber());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testUnassignPatient() throws UserIDMustBeAtLeastEightCharactersException {

        // Init patients and provider
        Patient patient = new Patient(patientId, "patient_email@email.com", "1234567890");
        Provider provider = new Provider(providerId, "provider_email@email.com", "1111111111");

        // Check if provider's list of patients do not have the patient (by userId, assume distinct userId)
        provider.assignPatient(patient);
        // patient is there
        Patient patientGot = provider.getPatient(patientId);

        provider.unAssignPatient(patient);

        // patient is not there, check for exception
        try {
            patientGot = provider.getPatient(patientId);
            fail("No exception raised");
        } catch (NoSuchElementException e) {
        }
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetPatient() throws UserIDMustBeAtLeastEightCharactersException {
        // Init patients and provider
        Patient patient = new Patient(patientId, "patient_email@email.com", "1234567890");
        Provider provider = new Provider(providerId, "provider_email@email.com", "1111111111");

        // patient is not there, check for exception
        try {
            Patient patientGot = provider.getPatient(patientId);
            fail("No exception raised");
        } catch (NoSuchElementException e) {
        }

        // If patient is there, check if they're the same patients
        provider.assignPatient(patient);
        Patient patientGot1 = provider.getPatient(patient.getUserID());
        Patient patientGot2 = provider.getPatient(0);
        assertEquals("compare patient userId", patient.getUserID(), patientGot1.getUserID());
        assertEquals("compare patient email", patient.getEmail(), patientGot1.getEmail());
        assertEquals("compare patient phone number", patient.getPhoneNumber(), patientGot1.getPhoneNumber());
        assertEquals("Got same object when fetched by index", patientGot1, patientGot2);
        
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetPatients()
            throws UserIDMustBeAtLeastEightCharactersException {
        String patientId2 = "13572468";

        // Init patients and provider
        Patient patient = new Patient(patientId, "patient_email@email.com", "1234567890");
        Patient patient1 = new Patient(patientId2, "patient1_email@email.com", "1234567890");
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Provider provider = new Provider(providerId, "provider_email@email.com", "1111111111");

        // Check for empty list of patient
        assertEquals("Patient list of size 0", patients, provider.getPatients());

        // Check for non empty list of patient
        provider.assignPatient(patient);
        patients.add(patient);
        assertEquals("Patient list of size 1", patients, provider.getPatients());
        provider.assignPatient(patient1);
        patients.add(patient1);
        assertEquals("Patient list of size 2", patients, provider.getPatients());
    }

}
