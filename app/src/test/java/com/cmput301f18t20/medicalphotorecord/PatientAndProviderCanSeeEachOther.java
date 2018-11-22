package com.cmput301f18t20.medicalphotorecord;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class PatientAndProviderCanSeeEachOther {
    protected static String patientId = "12345678";
    protected static String providerId = "abcdefgh";

    protected static String CorrectUserID1 = "abcdefgh";
    protected static String CorrectUserID2 = "stuvwxyz";
    protected static String CorrectUserID3 = "acebgfIII";

    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";



}
