package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EntryUnitTests {
    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";

    /**
     * Testing that getting and setting Title for an entry behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetTitle()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Entry entry = new Entry(Correct_User_ID, Correct_Title);
        String string1 = "hello";
        String string2 = "world";
        List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

        for (String currTitle: SetAndGetTestStrings) {
            entry.setTitle(currTitle);
            assertEquals("Title not set correctly", currTitle, entry.title);
            assertEquals("Title not fetched correctly", currTitle, entry.getTitle());
        }
    }

    /** if title is longer than 30 chars, should raise TitleTooLongException.
     * if title is less than or equal to 30 chars, it should not raise TitleTooLongException.
     * the other two cases are fail states.
     */
    @Test
    public void TitleBoundaries()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Entry entry = new Entry(Correct_User_ID, Correct_Title);
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
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetDate()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Entry entry = new Entry(Correct_User_ID, Correct_Title);
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            entry.setDate(date);

            assertEquals("Date was not set correctly", date, entry.date);
            assertEquals("Date was not fetched correctly", date, entry.getDate());
        }
    }

    /* does not generate UserIDMustBeAtLeastEightCharactersException on valid input
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetCreatedUserIDAndConstructorSanity()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "UserName", "'$%%**?+++")) {

            Entry entry = new Entry(TestUserID, Correct_Title);

            assertEquals("UserIDs did not match.",
                    TestUserID, entry.getCreatedByUserID());
            assertEquals("Titles did not match.",
                    Correct_Title, entry.getTitle());
        }
    }

    /* generates UserIDMustBeAtLeastEightCharactersException on invalid input
     */
    @Test
    public void UserIDMustBeAtLeastEightCharactersExceptionGeneration ()
            throws TitleTooLongException {
        for (String TestUserID : Arrays.asList("Small", "Limits7", "")) {

            try {
                Entry entry = new Entry(TestUserID, Correct_Title);
                fail("UserIDMustBeAtLeastEightCharactersException should have been " +
                        "generated for input " + TestUserID);

            } catch (UserIDMustBeAtLeastEightCharactersException e) {
                //Do nothing as correct functionality generates this exception
            }
        }
    }

    //TODO network and local storage tests
}
