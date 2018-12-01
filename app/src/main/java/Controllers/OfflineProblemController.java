package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;
import java.util.UUID;

/**
 * OfflineProblemController
 * Can get problem object from problemUUID Offline
 * Can add problem object to database Offline
 * Can delete problem object to database Offline
 * Can modify problem object to database Offline
 * Can get all problems for specific patient Offline
 * Can delete all problems for specific patient Offline
 * @version 2.0
 * @see Problem
 */
public class OfflineProblemController {

    /**
     * Get problem object from offline database using uuid
     * @param context: activity to be passed for offline save and load
     * @param uuid
     * @return patient record object if found, null if not found
     */
    public Problem getProblem(Context context, String uuid){

        // Compare uuid to every problem's uuid to get problem
        ArrayList<Problem> problems = new OfflineLoadController().loadProblemList(context);
        for (Problem p : problems) {
            if (uuid.equals(p.getUUID())) {
                return p;
            }
        }
        // If not found
        return null;
    }

    /**
     * Add problem object to offine database
     * @param context: activity to be passed for offline save and load
     * @param problem
     */
    public void addProblem(Context context, Problem problem){

        // Get list of problems, add, save list of problems
        ArrayList<Problem> problems = new OfflineLoadController().loadProblemList(context);
        problems.add(problem);
        new OfflineSaveController().saveProblemList(problems, context);
    }

    /**
     * Delete problem object from offline database
     * @param context: activity to be passed for offline save and load
     * @param problem
     */
    public void deleteProblem(Context context, Problem problem){

        // Get list of problems, delete, save list of problems
        ArrayList<Problem> problems = new OfflineLoadController().loadProblemList(context);
        for (Problem p : new ArrayList<>(problems)){
            if (p.getUUID().equals(problem.getUUID())){
                problems.remove(p);
            }
        }
        new OfflineSaveController().saveProblemList(problems, context);
    }

    /**
     * Modify problem object from offline database (delete old problem object using userId, then add problem object back)
     * @param context: activity to be passed for offline save and load
     * @param problem
     */
    public void modifyProblem(Context context, Problem problem){

        // Delete old problem with same uuid, add problem object back
        deleteProblem(context, problem);
        addProblem(context, problem);
    }

    /**
     * Get all problems associated to specific patient Offline
     * @param context: activity to be passed for offline save and load
     * @param userId
     * @return patientProblems
     */
    public ArrayList<Problem> getPatientProblems(Context context, String userId){

        // Loop through list of all problems and get those with same createdByUserId
        ArrayList<Problem> allProblems = new OfflineLoadController().loadProblemList(context);
        ArrayList<Problem> patientProblems = new ArrayList<>();

        for (Problem p:allProblems){
            if (userId.equals(p.getCreatedByUserID())){
                patientProblems.add(p);
            }
        }

        return patientProblems;
    }

    /**
     * Delete all problems associated to specific patient Offline
     * @param context: activity to be passed for offline save and load
     * @param userId
     */
    public void deletePatientProblems(Context context, String userId){

        // Loop through list of all problems and delete those with same createdByUserId, save to offline database
        ArrayList<Problem> problems = new OfflineLoadController().loadProblemList(context);

        for (Problem p:new ArrayList<>(problems)){
            if (userId.equals(p.getCreatedByUserID())){
                problems.remove(p);
            }
        }

        new OfflineSaveController().saveProblemList(problems, context);
    }
}
