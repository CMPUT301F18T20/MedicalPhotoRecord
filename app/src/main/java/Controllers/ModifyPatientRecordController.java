package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyPatientRecordController {

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

    public static void modifyRecord(Context context, PatientRecord new_record, PatientRecord old_record){
        //Delete  old record entry
        new AddDeleteRecordController().deleteRecord(context,old_record);

        //Add new record to record list
        new AddDeleteRecordController().saveRecord(context,new_record);
    }
}