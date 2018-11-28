package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.ArrayList;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.assertTrue;

public class RecordTest {
    @Test
    public void testRecordsAndPatientRecordsCanBeAddedToSameArray()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        ArrayList<Record> records = new ArrayList<>();

        Record record = new Record("aabbccdd", "");
        PatientRecord patientRecord = new PatientRecord("aabbccdd", "");

        records.add(record);
        records.add(patientRecord);

        assertTrue(records.get(0) instanceof Record);
        assertTrue(records.get(1) instanceof PatientRecord);

    }
}
