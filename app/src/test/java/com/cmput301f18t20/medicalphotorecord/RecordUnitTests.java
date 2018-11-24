package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.apache.commons.lang3.StringUtils;

import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class RecordUnitTests {
    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";

    /**
     * Testing that getting and setting Comment for a record behaves as expected.
     */
    @Test
    public void CanGetAndSetComment()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        String string1 = "hello";
        String string2 = "world";
        List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

        try {
            for (String currComment : SetAndGetTestStrings) {
                record.setComment(currComment);
                assertEquals("Comment was not set correctly", currComment, record.comment);
                assertEquals("Comment was not fetched correctly",
                        currComment, record.getComment());
            }
        }  catch (CommentTooLongException e){
            fail("Comment too long exception was encountered when it shouldn't have been.");
        }
    }

    /** if comment is longer than 300 chars, should raise CommentTooLongException.
     * if comment is less than or equal to 300 chars, it should not raise CommentTooLongException.
     * the other two cases are fail states.
     */
    @Test
    public void CommentBoundaries()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        List<String> BoundaryTestStrings = Arrays.asList(
                "", //0 char
                "a", //1 char
                "ababa", //5 chars
                StringUtils.repeat("aabbccddee", 29) + "aabbccdde", //299 chars
                StringUtils.repeat("aabbccddee", 30), //300 chars
                StringUtils.repeat("aabbccddee", 30) + "a", //301 chars
                StringUtils.repeat("aabbccddee", 40) //400 chars
        );
        Boolean isLongerThanAcceptable;
        int Acceptable = 300;

        for (String currComment: BoundaryTestStrings) {

            /* test if exception should be raised for current title */
            isLongerThanAcceptable = currComment.length() > Acceptable;

            try {
                /* if isLongerThanAcceptable is true, should raise TitleTooLongException.
                if false, it should not raise TitleTooLongException.
                the other two cases are fail states. */
                record.setComment(currComment);

                /* if it is longer than acceptable length */
                if (isLongerThanAcceptable) {
                    fail("Comment too long exception was encountered when it shouldn't have been.\n"
                            + "Current comment length:" + currComment.length() + ",\n"
                            + "Current acceptable comment length:" + Acceptable);
                }

            } catch (CommentTooLongException e){

                /* if it is shorter than or equal to acceptable length */
                if (!isLongerThanAcceptable) {
                    fail("Comment too long exception was not encountered when it should have been.\n"
                            + "Current comment length:" + currComment.length() + ",\n"
                            + "Current acceptable comment length:" + Acceptable);
                }
            }
        }
    }

    /* does not generate UserIDMustBeAtLeastEightCharactersException on valid input
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetCreatedUserIDAndConstructorSanity()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "UserName", "'$%%**?+++")) {

            Record record = new Record(TestUserID, Correct_Title);

            assertEquals("UserIDs did not match.",
                    TestUserID, record.getCreatedByUserID());
            assertEquals("Titles did not match.",
                    Correct_Title, record.getTitle());
        }
    }

    /* generates UserIDMustBeAtLeastEightCharactersException on invalid input
     */
    @Test
    public void UserIDMustBeAtLeastEightCharactersExceptionGeneration ()
            throws TitleTooLongException {
        for (String TestUserID : Arrays.asList("Small", "Limits7", "")) {

            try {
                Record record = new Record(TestUserID, Correct_Title);
                fail("UserIDMustBeAtLeastEightCharactersException should have been " +
                        "generated for input " + TestUserID);

            } catch (UserIDMustBeAtLeastEightCharactersException e) {
                //Do nothing as correct functionality generates this exception
            }
        }
    }

    /**
     * Testing that getting and setting Title for an record behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetTitle()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        String string1 = "hello";
        String string2 = "world";
        List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

        for (String currTitle: SetAndGetTestStrings) {
            record.setTitle(currTitle);
            assertEquals("Title not set correctly", currTitle, record.title);
            assertEquals("Title not fetched correctly", currTitle, record.getTitle());
        }
    }

    /** if title is longer than 30 chars, should raise TitleTooLongException.
     * if title is less than or equal to 30 chars, it should not raise TitleTooLongException.
     * the other two cases are fail states.
     */
    @Test
    public void TitleBoundaries()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
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
                record.setTitle(currTitle);

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
     * Testing that getting and setting Date for a record behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetDate()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            record.setDate(date);

            assertEquals("Date was not set correctly", date, record.dateCreated);
            assertEquals("Date was not fetched correctly", date, record.getDate());
        }
    }

    /**
     * Testing that getting and setting DateLastModified for a record behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetLastModifiedDate()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            record.setDateLastModified(date);

            assertEquals("Date was not set correctly", date, record.dateLastModified);
            assertEquals("Date was not fetched correctly", date, record.getDateLastModified());
        }
    }
}
