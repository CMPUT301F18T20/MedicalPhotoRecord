package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class ProviderTests {

    public void testAssignPatient() {

        // Init variables for later comparision of patient
        String patientId = "0000";
        String patientEmail = "patient_email@email.com";
        String patientPhoneNumber = "1234567890";
        Patient patient = new Patient("0000","patient_email@email.com","1234567890");

        // Init variables for later comparision of provider
        String providerId = "0001";
        String providerEmail = "provider_email@email.com";
        String providerPhoneNumber = "1234567891";
        Provider provider = new Provider("0001","provider_email@email.com","1234567891");


        // Check if provider's list of patients has patient (by userId, assume distinct userId)
        provider.assignPatient(patientId);
        Patient patientGot = Provider.getPatient(patientId);

        assertEquals("compare patient userId", patientId, patientGot.getUserID());
        assertEquals("compare patient email", patientEmail, patientGot.getEmail());
        assertEquals("compare patient phone number", patientPhoneNumber, patientGot.getPhoneNumber());


        // Check if patient's list of provider has provider (by userId, assume distinct userId)
        Provider providerGot = patient.getProvider(providerId);

        assertEquals("compare provider userId", providerId, providerGot.getUserID());
        assertEquals("compare provider email", providerEmail, providerGot.getEmail());
        assertEquals("compare provider phone number", providerPhoneNumber, providerGot.getPhoneNumber());

    }

    public void testUnassignPatient(){

        // Init patients and provider
        Patient patient = new Patient("0000","patient_email@email.com","1234567890");
        Provider provider = new Provider("1000","provider_email@email.com","1111111111");

        // Check if provider's list of patients do not have the patient (by userId, assume distinct userId)
        provider.assignPatient(patient);
        provider.unAssignPatient(patient);

        try{
            Patient patientGot = provider.getPatient("0000");
        }catch(NoSuchElementException e){
            assertEquals("Patient not found", e.getMessage() );
        }

        // Check if patient's list of providers do not have the provider (by userId, assume distinct userId)
        try{
            Provider providerGot = patient.getProvider("1000");
        }catch(NoSuchElementException e){
            assertEquals("Provider not found", e.getMessage() );
        }

    }
}
