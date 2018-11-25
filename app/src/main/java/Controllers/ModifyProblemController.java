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

import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyProblemController {

    public Problem getProblem(Context context, String problemUUID){

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
        return actualProblem;
    }

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
