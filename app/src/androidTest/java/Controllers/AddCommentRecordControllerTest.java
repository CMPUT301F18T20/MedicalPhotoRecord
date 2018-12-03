package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import Activities.ProviderAddCommentRecord;
import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import androidx.test.rule.ActivityTestRule;
import static junit.framework.TestCase.assertEquals;


import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;

/**
 * AddCommentRecordControllerTest
 * Testing methods for AddCommentRecordController
 *
 * @version 1.0
 * @since   2018-12-01
 *
 */

public class AddCommentRecordControllerTest {


    /**
     * Clears Online Database
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchRecordController.DeleteRecordsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     *  Clears offline database
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = ProviderAddCommentRecord.getActivity().getBaseContext();
        ArrayList<Record> emptyRecord = new ArrayList<>();
        new OfflineSaveController().saveRecordList(emptyRecord, context);
    }

    @Rule
    public ActivityTestRule<ProviderAddCommentRecord> ProviderAddCommentRecord =
            new ActivityTestRule<>(ProviderAddCommentRecord.class);

    /**
     * Tests addRecord method in AddCommentRecordController
     * Tests to see if the created comment record
     * matches what is stored in the online database
     *
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >= 8
     * @throws ExecutionException -
     * @throws InterruptedException -
     * @throws TitleTooLongException - Title too long
     * @throws CommentTooLongException - comment too long
     */
    @Test
    public void testAddRecord() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException, CommentTooLongException {
        // Create new Provider
        Provider expectedProvider = new Provider("testAddRecord");
        new ElasticsearchProviderController.AddProviderTask().execute(expectedProvider).get();

        Patient expectedPatient = new Patient("testPatient");
        new ElasticsearchPatientController.AddPatientTask().execute(expectedPatient).get();

        Problem expectedProblem = new Problem(expectedPatient.getUserID(),"");
        new ElasticsearchProblemController.AddProblemTask().execute(expectedProblem).get();

        Thread.sleep(ControllerTestTimeout);

        // Create new Record
        String createdBy = expectedProvider.getUserID();
        String title = "expectedTitle";
        String comment = "expected Comment";
        Date date = new Date();
        String problemUUID = expectedProblem.getUUID();

        // Test addRecord
        Record expectedRecord = new AddCommentRecordController().createRecord(createdBy,title,date,comment,problemUUID);
        new AddCommentRecordController().addRecord(expectedRecord);

        Thread.sleep(ControllerTestTimeout);

        Record gotRecordOnline = new ElasticsearchRecordController.GetRecordByRecordUUIDTask().execute(expectedRecord.getUUID()).get();

        String expectedRecordString = new Gson().toJson(expectedRecord);
        String gotRecordOnlineString = new Gson().toJson(gotRecordOnline);

        assertEquals("added record for online are not the same", expectedRecordString, gotRecordOnlineString);
    }
}
