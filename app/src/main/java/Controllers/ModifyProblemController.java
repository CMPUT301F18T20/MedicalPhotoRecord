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

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyProblemController {


    public Problem createNewProblem(String userID, String title, Date date, String description) throws
            UserIDMustBeAtLeastEightCharactersException,
            TitleTooLongException {

            //create new problem with updated info
            Problem problem = new Problem(userID, title);
            problem.setDate(date);
            problem.setDescription(description);
        return problem;
    }

    public void saveProblem(Context context, Problem new_problem, Problem old_problem,String userID){
        //Delete old problem entry
        new AddDeleteProblemController().saveProblem("delete",context,old_problem);
        //Add new problem entry to user's problem list
        new AddDeleteProblemController().saveProblem("add",context,new_problem);
    }

}
