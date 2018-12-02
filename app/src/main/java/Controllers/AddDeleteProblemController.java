/*
 * Class name: AddDeleteProblemController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 6:40 PM
 *
 * Last Modified: 12/11/18 6:40 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.ISCONNECTED;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;

/**
 * AddDeleteProblem Controller
 * Can create a new problem with inputs attribute
 * Can add given new problem online and offline
 * Can remove given problem online and offline
 * @version 2.0
 * @see Problem
 */
public class AddDeleteProblemController {

    /**
     * Create a problem object with createdByUserId, title, date and description
     * @param context: activity to be passed for offline save and load
     * @param userId
     * @param title
     * @param date
     * @param description
     * @return problem object
     * @throws UserIDMustBeAtLeastEightCharactersException thrown when < 8 characters
     * @throws TitleTooLongException thrown when > 30 characters
     * @throws ProblemDescriptionTooLongException thrown when > 300 characters
     */
    public Problem createProblem(Context context, String userId, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ProblemDescriptionTooLongException {

        // Creating a new problem to be added, add that problem to patient, save patient
        Problem problem = new Problem(userId, title);
        problem.setDate(date);
        problem.setDescription(description);
        return problem;
    }


    /**
     * Add problem to elastic search database and offline database
     * @param context: activity to be passed for offline save and load
     * @param problem: problem to be added to database
     */
    public void saveAddProblem(Context context, Problem problem) {

        // Check connection
        Boolean isConnected = ISCONNECTED;

        if (isConnected == true){
            // Online
            try {
                new ElasticsearchProblemController.AddProblemTask().execute(problem).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Offline (always save)
        new OfflineProblemController().addProblem(context, problem);
    }

    /**
     * Remove problem, all records related to that problem from elastic search database and offline database
     * @param context: activity to be passed for offline save and load
     * @param problem: problem to be removed to database
     */
    public void saveDeleteProblem(Context context, Problem problem) {

        // Check connection
        Boolean isConnected = ISCONNECTED;

        if (isConnected == true){
            // Online
            try {
                new ElasticsearchProblemController.DeleteProblemsTask().execute(problem.getUUID()).get();
                // Delete all records associated to problem

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Offline (always save)
        new OfflineProblemController().deleteProblem(context, problem);
        // Delete all records associated to problem

    }
}
