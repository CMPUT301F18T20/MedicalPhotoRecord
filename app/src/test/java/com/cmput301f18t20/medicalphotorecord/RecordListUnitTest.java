package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RecordListUnitTest {
    @Test
    public void testSetRecordsAndGetRecordCount() {

        /* create record Manager */
        RecordList recordList = new RecordList();

        /* try a few values for number of records */
        for (int recordCounter : Arrays.asList(0, 1, 3, 200) ) {

            /* Create our own Arraylist of records adding "recordCounter" records in */
            ArrayList<Record> records = new ArrayList<>();
            for (int i = 0; i < recordCounter; i++) {
                records.add(new Record(""));
            }

            /*set our array of records in the recordList to be our new array */
            recordList.setRecords(records);

            /* ensure correct record count */
            assertEquals(recordList.getRecordCount(), recordCounter);

            /* ensure array was correctly set */
            assertArrayEquals(recordList.Records.toArray(), records.toArray());

            /*  ensure array was correctly fetched */
            assertArrayEquals(recordList.getRecords().toArray(), records.toArray());
        }
    }
/*
    public ArrayList<Record> getRecords() {
        return Records;
    }

    public void addRecord(Record record) {
        Records.add(record);
    }

    public void deleteRecord(Record record) {
        // TODO: this will need to be a lot smarter, likely have to do with tasks like
        // TODO: "delete all records with a certain string in their title"
        Records.remove(record);
    }

    public void clearRecords() {
        Records.clear();
    }
    */
}

