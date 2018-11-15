/*
 * Class name: AddProblemControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 1:00 PM
 *
 * Last Modified: 11/15/18 1:00 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.assertEquals;

public class AddProblemControllerTest {

    @Test
    public void testsaveProblem() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        // Create new patient and problem
        Patient patient = new Patient("patientname","","");
        Problem expectedProblem = new Problem(patient.getUserID(), "problem_title");
        expectedProblem.setDate(new Date());
        expectedProblem.setDescription("problem_descriptions");

        // Add and compare
        new AddProblemController().saveProblem("add", null, patient.getUserID(),
                expectedProblem.getTitle(), expectedProblem.getDate(), expectedProblem.getDescription());

        // not sure which implementation to use
        // OOP
        Patient gotPatient = (new ElasticsearchPatientController.GetPatientTask().execute(patient.getUserID()).get()).get(0);
        Problem gotProblem = gotPatient.getProblem(0);

        // Problem database??
        assertEquals("added problems are not the same", expectedProblem, gotProblem);

    }
}
