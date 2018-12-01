/*
 * Class name: BrowseProblemsController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 2:30 PM
 *
 * Last Modified: 12/11/18 2:30 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * BrowseProblemsController
 * Get all problems associated to a patient
 * @version 2.0
 * @see Problem
 * @see Patient
 */
public class BrowseProblemsController {

    /**
     * Get all problems for that specific patient
     * Get from both online and offline database
     * Use syncing controller to decide which problem list to return
     * @param context: activity to be passed for offline save and load
     * @param userID: id to identiy user (patient)
     * @return actualProblemList
     */
    public ArrayList<Problem> getProblemList(Context context, String userID) {

        // Online
        ArrayList<Problem> onlineProblemList = null;
        try {
            onlineProblemList = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                    .execute(userID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        ArrayList<Problem> offlineProblemList = new OfflineProblemController().getPatientProblems(context, userID);

        // Syncing issue (for later)
        ArrayList<Problem> actualProblemList = onlineProblemList;
        return actualProblemList;
    }
}
