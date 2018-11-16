/*
 * Class name: AddProblemController
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
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;
import java.util.Date;

import Activities.BrowseUserProblems;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class AddProblemController {

    public Problem createProblem(Context context, String userId, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException{

        // Creating a new problem to be added, add that problem to patient, save patient
        Problem problem = new Problem(userId, title);
        problem.setDate(date);
        problem.setDescription(description);
        return problem;
    }


    public void saveProblem(String mode, Context context, Problem problem){

        // Get patient
        Patient patient = new ModifyUserController().getPatient(context, problem.getCreatedByUserID());

        if (mode.equals("add")){
            patient.addProblem(problem);
        }
        if (mode.equals("delete")){

            // Has to search for problem then delete b/c of date issue again
            for (Problem p : new ArrayList<>(patient.getProblems())){
                if (p.getTitle().equals(problem.getTitle())){
                    patient.removeProblem(p);
                }
            }
        }

        new ModifyUserController().savePatient(context, patient);
    }
}
