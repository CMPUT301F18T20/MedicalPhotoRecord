/*
 * Class name: BrowseUserProblemsController
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
import java.util.stream.Collectors;

public class BrowseUserProblemsController {

    public ArrayList<Problem> getProblemList(Context context, String userID){

        // Get patient
        Patient patient = new ModifyUserController().getPatient(context, userID);

        // Get patient's problems list
        return patient.getProblems();
    }
}
