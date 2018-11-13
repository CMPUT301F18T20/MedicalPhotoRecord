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

import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;
import java.util.Date;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class AddProblemController {

    private ArrayList<Problem> problems;

    public void saveProblem(String mode, Context context, int position, String userId, String title, Date date, String description){

        // Get most updated version of problem list
        this.problems = new OfflineLoadController().loadProblemList(context);

        Log.d("title elngth",String.valueOf(title));


        // Modify
        if (mode == "modify"){
            this.problems.remove(position);
        }

        // Add & Modify
        // Creating a new problem to be added, add that problem to the list
        try{
            Problem problem = new Problem(userId, title);
            problem.setDate(date);
            problem.setDescription(description);
            this.problems.add(problem);

            // Offline save
            new OfflineSaveController().saveProblemList(problems, context);

            // Online save TODO

            // Toast confirmation
            Toast.makeText(context, "Your infos have been saved locally",Toast.LENGTH_LONG).show();

        }catch (UserIDMustBeAtLeastEightCharactersException e){
            Toast.makeText(context, "Your userId has to contains more than 8 characters",Toast.LENGTH_LONG).show();
        }catch (TitleTooLongException e){
            Toast.makeText(context, "Your title is too long",Toast.LENGTH_LONG).show();
        }
    }
}
