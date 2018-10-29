package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.location.LocationManager;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.*;
import org.apache.commons.lang3.StringUtils;

public class RecordUnitTests {

    /**
     * Testing that getting and setting Comment for a record behaves as expected.
     */
    @Test
    public void CanGetAndSetComment() {
        Record record = new Record();
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
    public void CommentBoundaries() {
        Record record = new Record();
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

    //TODO network and local storage tests
}
