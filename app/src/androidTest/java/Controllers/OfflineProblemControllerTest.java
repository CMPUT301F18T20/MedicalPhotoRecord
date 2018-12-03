package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import Activities.AddProblemActivity;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

/**
 * OfflineProblemControllerTest
 * Testing for OfflineProblemController
 * @version 1.0
 * @see OfflineProblemController
 */
public class OfflineProblemControllerTest {

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    public String userId = "OfflineProblemControllerTest";

    /**
     * Clear offline problem database
     */
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context);
    }

    /**
     * Add 2 problems to database to be used for later tests
     * @param context
     * @return
     * @throws TitleTooLongException
     * @throws UserIDMustBeAtLeastEightCharactersException
     */
    public ArrayList<Problem> addProblemsToDatabase(Context context) throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException {

        // Create problems and add them to database, return arraylist of problems
        Problem problem1 = new Problem(userId,"");
        Problem problem2 = new Problem(userId,"");
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem1);
        problems.add(problem2);
        new OfflineSaveController().saveProblemList(problems, context);

        return problems;
    }

    /**
     * Check if get all patient's problems offline work
     * @throws TitleTooLongException
     * @throws UserIDMustBeAtLeastEightCharactersException
     */
    @Test
    public void testGetPatientProblems() throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Add problems to database
        ArrayList<Problem> expectedProblems = addProblemsToDatabase(context);

        // Test getPatientProblems()
        ArrayList<Problem> gotProblems = new OfflineProblemController().getPatientProblems(context, userId);

        // Converting objects to json string because of date issue
        for (int i = 0; i < expectedProblems.size(); i ++){
            String p1 = new Gson().toJson(expectedProblems.get(i));
            String p2 = new Gson().toJson(gotProblems.get(i));
            assertEquals("compare each problem for getPatientProblems()", p2,p1);
        }

        // Wipe database
        wipeOfflineDatabase();
    }

    /**
     * Check if delete all patient's problems offline work
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws TitleTooLongException
     */
    @Test
    public void testDeletePatientProblems() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Add problems to database
        ArrayList<Problem> expectedProblems = addProblemsToDatabase(context);

        // test deletePatientProblems()
        new OfflineProblemController().deletePatientProblems(context, userId);

        // Compare
        ArrayList<Problem> gotProblems = new OfflineProblemController().getPatientProblems(context, userId);
        assertEquals("test deletePatientProblems", 0, gotProblems.size());

        // Wipe database
        wipeOfflineDatabase();
    }
}
