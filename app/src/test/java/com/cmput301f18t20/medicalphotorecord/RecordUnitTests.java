package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.location.LocationManager;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.apache.commons.lang3.StringUtils;

public class RecordUnitTests {
    String string1 = "hello";
    String string2 = "world";
    List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

    /**
     * Testing that getting and setting Title for a record behaves as expected.
     */
    @Test
    public void CanGetAndSetTitle() {
        Record record = new Record("");

        for (String currTitle: SetAndGetTestStrings) {
            try {
                record.setTitle(currTitle);
                assertEquals(currTitle, record.title);
                assertEquals(currTitle, record.getTitle());


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
        Record record = new Record("");
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
     * Testing that getting and setting Comment for a record behaves as expected.
     */
    @Test
    public void CanGetAndSetComment() {
        Record record = new Record("");
        try {
            for (String currComment : SetAndGetTestStrings) {
                record.setComment(currComment);
                assertEquals(currComment, record.comment);
                assertEquals(currComment, record.getComment());
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
    public void CommentBoundaries() {
        Record record = new Record("");
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

    /**
     * Testing that getting and setting Date for a record behaves as expected.
     */
    @Test
    public void CanGetAndSetDate() {
        Record record = new Record("");
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            record.setDate(date);

            assertEquals(date, record.date);
            assertEquals(date, record.getDate());
        }
    }

    @Test
    public void CanGetAndSetLocation() {
        /* limit for longitude is +- 180, latitude is +-90. TODO: setter should throw error on violating those */
        int offset = 15;
        PatientRecord record = new PatientRecord("");
        Location newLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation.setLatitude(0);
        newLocation.setLongitude(0);

        for (int i = -195; i < 195; i+=5) {
            newLocation.setLatitude(i);
            newLocation.setLongitude(i + offset);
            record.setGeolocation(newLocation);

            assertEquals("geolocation was not set properly.",
                    newLocation, record.geolocation);
            assertEquals("geolocation was not fetched properly.",
                    newLocation, record.getGeolocation());
        }
    }

    @Test
    public void CanGetCreatedUserID() {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "29811001", "99999999999999")) {

            Record record = new Record(TestUserID);

            assertEquals("UserIDs did not match.",
                    TestUserID, record.getCreatedByUserID());
        }
    }
}
