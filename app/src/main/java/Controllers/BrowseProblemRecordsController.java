package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * BrowseProblemRecordsController
 * Get all records associated to a patient's specific problem
 * @version 2.0
 * @see Record
 * @see Problem
 * @see Patient
 */
public class BrowseProblemRecordsController {

    /**
     * Get all patient records for that specific patient's problem
     * Get from both online and offline database
     * Use syncing controller to decide which record list to return
     * @param context: activity to be passed for offline save and load
     * @param problemUUID: id to identiy problem
     * @param userID: id to identiy user (patient)
     * @return records
     */
    public ArrayList<PatientRecord> getPatientRecords(Context context,String problemUUID, String userID){
        ArrayList<PatientRecord> records = new ArrayList<>();
        try {
            records = new ElasticsearchPatientRecordController
                    .GetPatientRecordsWithProblemUUID().execute(problemUUID).get();
            return records;
        } catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException i){
            i.printStackTrace();
        }

        //Offline retrieval
        ArrayList<PatientRecord> offlineRecords = new OfflinePatientRecordController().getAllPatientRecords(context,userID,problemUUID);

        //TODO syncing

        return records;
    }

}
