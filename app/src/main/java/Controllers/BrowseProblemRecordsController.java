package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BrowseProblemRecordsController {

    public ArrayList<PatientRecord> getPatientRecords(Context context,String problemUUID, String userID){
        ArrayList<PatientRecord> records;
        try {
            records = new ElasticsearchPatientRecordController
                    .GetPatientRecordsWithProblemUUID().execute(problemUUID).get();
        } catch (ExecutionException e){
            throw new RuntimeException(e);
        }catch (InterruptedException i){
            throw new RuntimeException(i);
        }

        //Offline retrieval
        ArrayList<PatientRecord> offlineRecords = new OfflinePatientRecordController().getAllPatientRecords(context,userID,problemUUID);

        //TODO syncing

        return records;
    }

}
