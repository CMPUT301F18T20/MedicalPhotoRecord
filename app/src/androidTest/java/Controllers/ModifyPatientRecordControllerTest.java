package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddRecordActivity;
import Enums.INDEX_TYPE;
import Exceptions.NoSuchRecordException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;

/**
 * ModifyPatientRecordControllerTest
 * Testing for methods in ModifyPatientRecordController
 * @version 1.0
 * @see ModifyPatientRecordController
 */

public class ModifyPatientRecordControllerTest {

    /**
     * Clear Online database for patient records
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();
        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * Clear offline database for patient records
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<PatientRecord> emptyPatientRecords = new ArrayList<>();
        new OfflineSaveController().savePatientRecordLIst(emptyPatientRecords, context);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity =
            new ActivityTestRule<>(AddRecordActivity.class);

    /**
     * testGetPatientRecord
     * Tests getPatientRecord in ModifyPatientRecordController
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >= 8
     * @throws ExecutionException -
     * @throws InterruptedException -
     * @throws TitleTooLongException - Title too long
     * @throws NoSuchRecordException - Record not found
     */
    @Test
    public void testGetPatientRecord() throws
            UserIDMustBeAtLeastEightCharactersException, ExecutionException,
            InterruptedException, TitleTooLongException, NoSuchRecordException {

        Context context = AddRecordActivity.getActivity().getBaseContext();

        // Create new record
        PatientRecord testRecord = new PatientRecord("testPatient", " ");

        // Save them to online and offline database
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(testRecord).get();
        Thread.sleep(ControllerTestTimeout);

        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        patientRecords.add(testRecord);
        new OfflineSaveController().savePatientRecordLIst(patientRecords, context);

        // Test getPatientRecord
        PatientRecord onlineRecord = new ModifyPatientRecordController().getPatientRecord(context, testRecord.getUUID());
        PatientRecord offlineRecord = new ModifyPatientRecordController().getPatientRecord(context, testRecord.getUUID());

        // Compare 3 objects
        String recordString = new Gson().toJson(testRecord);
        String gotOnlinePatientRecordString = new Gson().toJson(onlineRecord);
        String gotOfflinePatientRecordString = new Gson().toJson(offlineRecord);
        assertEquals("patient record got from online database is not the same", recordString, gotOnlinePatientRecordString);
        assertEquals("patient record got from offline database is not the same", recordString, gotOfflinePatientRecordString);

    }

    /**
     * testModifyRecord
     * Tests modifyRecord method in ModifyPatientRecordController
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >=8
     * @throws TitleTooLongException - title too long
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    @Test
    public void testModifyRecord() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Context context = AddRecordActivity.getActivity().getBaseContext();

        PatientRecord testRecord = new PatientRecord("modifyRecord", "");
        String title = "New Title";
        String description = "New description.";

        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        patientRecords.add(testRecord);
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(testRecord).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflineSaveController().savePatientRecordLIst(patientRecords, context);

        // Test modifyRecord
        new ModifyPatientRecordController().modifyRecord(context,testRecord,title,description);
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        testRecord.setTitle(title);
        testRecord.setDescription(description);

        PatientRecord onlinePatientRecord = new ElasticsearchPatientRecordController.GetPatientRecordByPatientRecordUUIDTask().execute(testRecord.getUUID()).get();
        PatientRecord offlinePatientRecord = new OfflinePatientRecordController().getPatientRecord(context,testRecord.getUUID());

        String modPatientRecordString = new Gson().toJson(testRecord);
        String onlinePatientRecordString = new Gson().toJson(onlinePatientRecord);
        String offlinePatientRecordString = new Gson().toJson(offlinePatientRecord);
        assertEquals("modified online patient record are not the same", modPatientRecordString, onlinePatientRecordString);
        assertEquals("modified offline patient record not the same", modPatientRecordString, offlinePatientRecordString);
    }

}
