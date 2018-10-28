package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EntryUnitTests {

    /**
     * Testing that getting and setting Title for an entry behaves as expected.
     */
    @Test
    public void CanGetAndSetTitle() {
        Entry entry = new Entry("");
        String string1 = "hello";
        String string2 = "world";
        List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

        for (String currTitle: SetAndGetTestStrings) {
            try {
                entry.setTitle(currTitle);
                assertEquals("Title not set correctly", currTitle, entry.title);
                assertEquals("Title not fetched correctly", currTitle, entry.getTitle());

            } catch (TitleTooLongException e){
                fail("Title too long exception was encountered when it shouldn't have been.");
            }
        }
    }

    /** if title is longer than 30 chars, should raise TitleTooLongException.
     * if title is less than or equal to 30 chars, it should not raise TitleTooLongException.
     * the other two cases are fail states.
     */
    @Test
    public void TitleBoundaries() {
        Entry entry = new Entry("");
        List<String> BoundaryTestStrings = Arrays.asList(
                "", //0 char
                "a", //1 char
                "ababa", //5 chars
                "aabbccddeeaabbccddeeaabbccdde", //29 chars
                "aabbccddeeaabbccddeeaabbccddee", //30 chars
                "aabbccddeeaabbccddeeaabbccddeea", //31 chars
                "aabbccddeeaabbccddeeaabbccddeeaabbccddee" //40 chars
        );
        Boolean isLongerThanAcceptable;
        int Acceptable = 30;

        for (String currTitle: BoundaryTestStrings) {

            /* test if exception should be raised for current title */
            isLongerThanAcceptable = currTitle.length() > Acceptable;

            try {
                /* if isLongerThanAcceptable is true, should raise TitleTooLongException.
                if false, it should not raise TitleTooLongException.
                the other two cases are fail states. */
                entry.setTitle(currTitle);

                /* if it is longer than acceptable length */
                if (isLongerThanAcceptable) {
                    fail("Title too long exception was encountered when it shouldn't have been.\n"
                            + "Current title length:" + currTitle.length() + ",\n"
                            + "Current acceptable title length:" + Acceptable);
                }


            } catch (TitleTooLongException e){

                /* if it is shorter than or equal to acceptable length */
                if (!isLongerThanAcceptable) {
                    fail("Title too long exception was not encountered when it should have been.\n"
                            + "Current title length:" + currTitle.length() + ",\n"
                            + "Current acceptable title length:" + Acceptable);
                }
            }
        }
    }

    /**
     * Testing that getting and setting Date for a entry behaves as expected.
     */
    @Test
    public void CanGetAndSetDate() {
        Entry entry = new Entry("");
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            entry.setDate(date);

            assertEquals("Date was not set correctly", date, entry.date);
            assertEquals("Date was not fetched correctly", date, entry.getDate());
        }
    }

    @Test
    public void CanGetCreatedUserID() {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "29811001", "99999999999999")) {

            Entry entry = new Entry(TestUserID);

            assertEquals("UserIDs did not match.",
                    TestUserID, entry.getCreatedByUserID());
        }
    }

    //TODO network and local storage tests
}
