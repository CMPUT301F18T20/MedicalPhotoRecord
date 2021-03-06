package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchRecordException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

/**
 * ModifyPatientRecordController
 * Can get patient record object from recordUUID
 * Can save modified patient record object to online and offline database
 * @version 2.0
 * @see PatientRecord
 */
public class ModifyPatientRecordController {


    /**
     * Get patient record object from appropriate database (online when there's wifi, offline when there's no wifi)
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     * @return chosen_record corresponding to recordUUID
     * @throws NoSuchRecordException: if record is not found
     */
    public PatientRecord getPatientRecord(Context context,String recordUUID) throws NoSuchRecordException {

        PatientRecord chosen_record = null;

        PatientRecord online_chosenRecord = null;
        try {
            online_chosenRecord = new ElasticsearchPatientRecordController
                    .GetPatientRecordByPatientRecordUUIDTask()
                    .execute(recordUUID).get();
        } catch(InterruptedException e1){
            e1.printStackTrace();
        }catch (ExecutionException e2){
            e2.printStackTrace();
        }

        //Offline
        PatientRecord offline_chosenRecord = new OfflinePatientRecordController().getPatientRecord(context,recordUUID);


        //TODO syncing online and offline
        chosen_record = online_chosenRecord;

        if (chosen_record == null){
            throw new NoSuchRecordException();
        }
        return chosen_record;
    }


    /**
     * Takes in old patient record, new modified email, new modified phone number
     * Sets new information to patient record object
     * Save patient record object to both online and offline database
     * @param context: activity to be passed for offline save and load
     * @param chosen_record
     * @param title
     * @param description
     */
    public static void modifyRecord(Context context, PatientRecord chosen_record, String title, String description){
        try {
            chosen_record.setTitle(title);
        } catch(TitleTooLongException e1){
            Toast.makeText(context, "Your Title is too long!",Toast.LENGTH_SHORT).show();
        }
        chosen_record.setDescription(description);

        new ElasticsearchPatientRecordController.SaveModifiedPatientRecord().execute(chosen_record);

        //Offline
        //Delete old record
        new OfflinePatientRecordController().deletePatientRecord(context,chosen_record.getUUID());

        //Add new modified
        new OfflinePatientRecordController().addPatientRecord(context,chosen_record);

    }
}