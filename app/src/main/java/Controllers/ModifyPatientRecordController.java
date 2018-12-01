package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyPatientRecordController {

    public PatientRecord getPatientRecord(Context context,String recordUUID){
        PatientRecord chosen_record = null;
        try {
            chosen_record = new ElasticsearchPatientRecordController
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
        return chosen_record;
    }


    public static PatientRecord createNewRecord(String userID, String title, Date date, String description)
            throws UserIDMustBeAtLeastEightCharactersException,
            TitleTooLongException {

        //TODO add body_location,photos,geolocation setting
        //create new record  with updated info
        PatientRecord record =  new PatientRecord(userID,title);
        record.setDate(date);
        record.setDescription(description);
        return record;
    }

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