/*
 * Class name: ModifyProblemController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 19/11/18 2:17 AM
 *
 * Last Modified: 19/11/18 2:17 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchProblemException;
import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

/**
 * ModifyProblemController
 * Can get problem object from problemUUID
 * Can save modified problem object to online and offline database
 * @version 2.0
 * @see Problem
 */
public class ModifyProblemController {

    /**
     * Get problem object from appropriate database (online when there's wifi, offline when there's no wifi)
     * @param context: activity to be passed for offline save and load
     * @param problemUUID
     * @return actualProblem object correspond to problemUUID
     * @throws NoSuchProblemException: if problem is not found in databases
     */
    public Problem getProblem(Context context, String problemUUID) throws NoSuchProblemException {

        // Online
        Problem onlineProblem = null;
        try {
            onlineProblem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(problemUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        Problem offlineProblem = new OfflineProblemController().getProblem(context, problemUUID);

        // Sync
        Problem actualProblem = onlineProblem;
        if (actualProblem == null){
            throw new NoSuchProblemException();
        }
        return actualProblem;
    }

    /**
     * Takes in old problem, new modified title, new modified description
     * Sets new information to problem object
     * Save problem object to both online and offline database
     * @param context: activity to be passed for offline save and load
     * @param problem
     * @param title
     * @param description
     * @exception TitleTooLongException: thrown when > 30 characters
     * @exception ProblemDescriptionTooLongException: thrown when > 300 characters
     */
    public void saveModifyProblem(Context context, Problem problem, String title, String description) throws TitleTooLongException, ProblemDescriptionTooLongException {

        // Modified
        problem.setTitle(title);
        problem.setDescription(description);

        // Online
        try {
            new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        new OfflineProblemController().modifyProblem(context, problem);
    }
}
