/*
 * Class name: BrowseUserProblemControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 12:51 PM
 *
 * Last Modified: 11/15/18 12:51 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;

import org.junit.Test;

import java.util.ArrayList;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.assertEquals;

public class BrowseUserProblemControllerTest {

    @Test
    public void testGetProblemList() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        // Create new providers with patients
        String[] problemTitles = {
                "getProbp1",
                "getProbp2",
                "getProbp3",
                "getProbp4",
                "getProbp5",

        };

        Patient p = new Patient("getProblemListPatient", "", "");
        ArrayList<Problem> expectedP1Problems = new ArrayList<>();

        // Add problems for patient
        for (int i = 0; i < problemTitles.length; i++){
            Problem pl = new Problem(p.getUserID(), problemTitles[i]);
            p.addProblem(pl);
            expectedP1Problems.add(pl);
        }

        // Save them to database (online for now)
        ArrayList<Problem> gotP1Problems= new BrowseUserProblemsController().getProblemList(null, p.getUserID());

        // Check
        assertEquals("patient list of problems size 5", expectedP1Problems, gotP1Problems);

    }
}
