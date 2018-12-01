package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * ProviderRecordsController
 * Simply collects all of the patient's records and returns it
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

public class ProviderRecordsController {
    protected  ArrayList<Record> records = new ArrayList<>();
    private ArrayList<Record> providerRecords;
    private ArrayList<PatientRecord> patientRecords;
    private BrowseProblemRecordsController browseProblemRecordsController = new BrowseProblemRecordsController();

    /**
     * getRecords
     * Gets the list of records created by the patient and gets the list of records created
     * by the provider, the two list of records are combined and returned.
     *
     * @param context - activity's context
     * @param problemUUID - problem's UUID
     * @param patientId - patient's ID
     * @return - returns ArrayList<Record> of patient's records
     */

    public ArrayList<Record> getRecords(Context context, String problemUUID, String patientId){

        this.patientRecords = browseProblemRecordsController.getPatientRecords(context,problemUUID,patientId);


        try {
            this.providerRecords = new ElasticsearchRecordController.GetRecordsWithProblemUUID().execute(problemUUID).get();
        } catch (ExecutionException e){
            throw new RuntimeException(e);
        }catch (InterruptedException i){
            throw new RuntimeException(i);
        }

        records.addAll(providerRecords);
        records.addAll(patientRecords);

        return records;
    }
}
