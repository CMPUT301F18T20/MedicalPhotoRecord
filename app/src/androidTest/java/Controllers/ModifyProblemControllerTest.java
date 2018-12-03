package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddProblemActivity;
import Enums.INDEX_TYPE;
import Exceptions.NoSuchProblemException;
import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;

/**
 * ModifyProblemControllerTest
 * Testing for method in ModifyProblemController
 * @version 1.0
 * @see ModifyProblemController
 */
public class ModifyProblemControllerTest {

    /**
     * Clear online problem database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * Clear offline problem database
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context);
    }

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(Activities.AddProblemActivity.class);


    /**
     * Test for getting a problem using problem UUID for online and offline
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TitleTooLongException
     * @throws NoSuchProblemException
     */
    @Test
    public void testGetProblem() throws UserIDMustBeAtLeastEightCharactersException,
            ExecutionException, InterruptedException, TitleTooLongException, NoSuchProblemException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new problem
        Problem problem = new Problem("testGetProblem","");

        // Save them to online and offline database
        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflineProblemController().addProblem(context, problem);

        // Test getProblem (online and offline, will have to fix after syncing issue is done)
        Problem gotOnlineProblem = new ModifyProblemController().getProblem(context, problem.getUUID());
        Problem gotOfflineProblem = new ModifyProblemController().getProblem(context, problem.getUUID());

        // Compare 3 objects, convert to gson string since date is giving some problem
        String problemString = new Gson().toJson(problem);
        String gotOnlineProblemString = new Gson().toJson(gotOnlineProblem);
        String gotOfflineProblemString = new Gson().toJson(gotOfflineProblem);
        assertEquals("problem got from online database is not the same", problemString, gotOnlineProblemString);
        assertEquals("problem got from offline database is not the same", problemString, gotOfflineProblemString);

    }

    /**
     * Test if modified problem is saved correctly in online and offline
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TitleTooLongException
     * @throws ProblemDescriptionTooLongException
     */
    @Test
    public void testSaveModifyProblem() throws UserIDMustBeAtLeastEightCharactersException,
            ExecutionException, InterruptedException, TitleTooLongException,
            ProblemDescriptionTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new problem and modified problem
        Problem problem = new Problem("testSaveModifyProblem","");
        String modTitle = "testSaveModifyProblemTitle";
        String modDescription = "testSaveModifyProblemDescription";

        // Save them to online and offline database
        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflineProblemController().addProblem(context, problem);

        // Test saveModifyProblem
        new ModifyProblemController().saveModifyProblem(context,problem, modTitle, modDescription);
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        problem.setTitle(modTitle);
        problem.setDescription(modDescription);

        Problem gotOnlineProblem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(problem.getUUID()).get();
        Problem gotOfflineProblem = new OfflineProblemController().getProblem(context, problem.getUUID());

        String modProblemString = new Gson().toJson(problem);
        String gotOnlineProblemString = new Gson().toJson(gotOnlineProblem);
        String gotOfflineProblemString = new Gson().toJson(gotOfflineProblem);
        assertEquals("modified online problem are not the same", modProblemString, gotOnlineProblemString);
        assertEquals("modified offline problem are not the same", modProblemString, gotOfflineProblemString);
    }

}
