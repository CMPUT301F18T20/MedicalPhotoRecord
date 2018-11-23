package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;
import java.util.UUID;

public class OfflineProblemController {

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

    public void addProblem(Context context, Problem problem){

        // Get list of problems, add, save list of problems
        ArrayList<Problem> problems = new OfflineLoadController().loadProblemList(context);
        problems.add(problem);
        new OfflineSaveController().saveProblemList(problems, context);
    }

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

    public void modifyProblem(Context context, Problem problem){

        // Delete old problem with same uuid, add problem object back
        deleteProblem(context, problem);
        addProblem(context, problem);
    }

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
