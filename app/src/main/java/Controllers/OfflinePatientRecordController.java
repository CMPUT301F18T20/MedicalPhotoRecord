package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;

import java.util.ArrayList;

import Controllers.OfflineLoadController;
import Controllers.OfflineProblemController;
import Controllers.OfflineSaveController;

public class OfflinePatientRecordController {

    public PatientRecord getPatientRecord(Context context, String recordUUID){

        ArrayList<PatientRecord> records = new OfflineLoadController().loadPatientRecordList(context);

        for (PatientRecord record : records){
            if (recordUUID.equals(record.getUUID())){
                return record;
            }
        }
        return null;
    }

    public void addPatientRecord(Context context, PatientRecord record){

        ArrayList<PatientRecord> records=  new OfflineLoadController().loadPatientRecordList(context);
        records.add(record);
        new OfflineSaveController().savePatientRecordLIst(records,context);
    }

    public void deletePatientRecord(Context context, String recordUUID){

        ArrayList<PatientRecord> records = new OfflineLoadController().loadPatientRecordList(context);
        for (PatientRecord record: records){
            if(recordUUID.equals(record.getUUID())){
                records.remove(record);
            }
        }
        new OfflineSaveController().savePatientRecordLIst(records,context);
    }
    public ArrayList<PatientRecord> getAllPatientRecords(Context context, String userId){

        ArrayList<PatientRecord> allRecords = new OfflineLoadController().loadPatientRecordList(context);
        ArrayList<PatientRecord> desiredRecords = new ArrayList<>();

        for (PatientRecord record:allRecords){
            if (userId.equals(record.getCreatedByUserID())){
                desiredRecords.add(record);
            }
        }
        return desiredRecords;
    }


}
