package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class AddDeleteRecordController {

    public PatientRecord createRecord (String problemUUID, String userID, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        PatientRecord record = new PatientRecord(userID, title);
        record.setAssociatedProblemUUID(problemUUID);
        record.setDate(date);
        record.setDescription(description);

        return record;
    }

    public void saveRecord(Context context, PatientRecord record) {

        //Online
        try {
            new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(record).get();
        } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
        } catch (ExecutionException e2) {
            throw new RuntimeException(e2);
        }

        //Offline
        new OfflinePatientRecordController().addPatientRecord(context, record);
    }

    public void deleteRecord(Context context, PatientRecord record){

        //Online
        try{
            new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute(record.getUUID()).get();
        } catch (InterruptedException e1){
            throw new RuntimeException(e1);
        }catch (ExecutionException e2){
            throw new RuntimeException(e2);
        }
        //Offline
        new OfflinePatientRecordController().deletePatientRecord(context,record.getUUID());
    }

}
