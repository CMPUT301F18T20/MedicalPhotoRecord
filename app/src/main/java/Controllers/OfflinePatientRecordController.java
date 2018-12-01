package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;

import Controllers.OfflineLoadController;
import Controllers.OfflineProblemController;
import Controllers.OfflineSaveController;

/**
 * OfflinePatientController
 * Can get patient record object from recordUUID Offline
 * Can add patient record object to database Offline
 * Can delete patient record object to database Offline
 * Can get all patients record from database Offline
 * @version 2.0
 * @see PatientRecord
 */
public class OfflinePatientRecordController {

    /**
     * Get patient record object from offline database using recordUUID
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     * @return patient record object if found, null if not found
     */
    public PatientRecord getPatientRecord(Context context, String recordUUID){

        ArrayList<PatientRecord> records = new OfflineLoadController().loadPatientRecordList(context);

        for (PatientRecord record : records){
            if (recordUUID.equals(record.getUUID())){
                return record;
            }
        }
        return null;
    }

    /**
     * Add patient record object to offine database
     * @param context: activity to be passed for offline save and load
     * @param record
     */
    public void addPatientRecord(Context context, PatientRecord record){

        ArrayList<PatientRecord> records=  new OfflineLoadController().loadPatientRecordList(context);
        records.add(record);
        new OfflineSaveController().savePatientRecordLIst(records,context);
    }

    /**
     * Delete patient record object from offline database
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     */
    public void deletePatientRecord(Context context, String recordUUID){

        ArrayList<PatientRecord> records = new OfflineLoadController().loadPatientRecordList(context);
        for (PatientRecord record: records){
            if(recordUUID.equals(record.getUUID())){
                records.remove(record);
            }
        }
        new OfflineSaveController().savePatientRecordLIst(records,context);
    }


    /**
     * Get all patient records for specific patient's problem Offline
     * @param context: activity to be passed for offline save and load
     * @param userId
     * @param problemUUID
     * @return
     */
    public ArrayList<PatientRecord> getAllPatientRecords(Context context, String userId,String problemUUID){

        ArrayList<PatientRecord> allRecords = new OfflineLoadController().loadPatientRecordList(context);
        ArrayList<PatientRecord> desiredRecords = new ArrayList<>();

        for (PatientRecord record:allRecords){
            if (userId.equals(record.getCreatedByUserID()) && problemUUID.equals(record.getAssociatedProblemUUID())){
                desiredRecords.add(record);
            }
        }
        return desiredRecords;
    }


}
