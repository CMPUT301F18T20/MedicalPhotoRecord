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

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;

public class AddDeleteProblemController {

    public Problem createProblem(Context context, String userId, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Creating a new problem to be added, add that problem to patient, save patient
        Problem problem = new Problem(userId, title);
        problem.setDate(date);
        problem.setDescription(description);
        return problem;
    }


    public void saveAddProblem(Context context, Problem problem) {

        // Online
        try {
            new ElasticsearchProblemController.AddProblemTask().execute(problem).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        new OfflineProblemController().addProblem(context, problem);
    }

    public void saveDeleteProblem(Context context, Problem problem) {

        // Online
        try {
            new ElasticsearchProblemController.DeleteProblemsTask().execute(problem.getUUID()).get();
            // Delete all records associated to problem

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        new OfflineProblemController().deleteProblem(context, problem);
        // Delete all records associated to problem

    }
}
