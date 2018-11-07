package com.cmput301f18t20.medicalphotorecord;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class PatientAndProviderCanSeeEachOther {
    protected static String patientId = "12345678";
    protected static String providerId = "abcdefgh";

    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";

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

    /* tests that fetchUpdatedRecordListTest will fetch updated database results */
    @Test(expected = Test.None.class /* no exception expected */)
    public void fetchUpdatedRecordListTest()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /*TODO this should be covered by the database tests */
        Record record = new Record(Correct_User_ID, Correct_Title);
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        problem.addRecord(record); //TODO consideration, wouldn't problem.addRecord add the record to database?

        /* call it so it registers as covered */
        problem.refresh();

        Assert.fail("Not fully implemented");
        //TODO: XXX URGENT: Need a way to add a record to the database

        /*
        add record, record2 to database.
        Check actual this.records instead of using getRecords to verify that only record is in there and recordCount is 1
        call problem.fetchUpdatedRecordList()
        See that it called fetchUpdatedRecordListTest() and now the record list has those exact two records
        add record3 to database
        check this.records only has two members with problem.getRecordCount()
        call record.getList()
        See that it called fetchUpdatedRecordListTest() and now the record list has all three records
         */
    }

}
