package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyRecordController {

    public Record createNewRecord(String userID, String title, Date date, String description)
            throws UserIDMustBeAtLeastEightCharactersException,
            TitleTooLongException {

        //TODO add body_location,photos,geolocation setting
        //create new record  with updated info
        Record record =  new Record(userID,title);
        record.setDate(date);
        record.setDescription(description);
        return record;
    }

    public void saveRecord(Context context, Record new_record, Record old_record, int position){
        //Delete  old record entry
        new AddRecordController().saveRecord("delete",context,old_record,position);

        //Add new record to record list
        new AddRecordController().saveRecord("add",context,new_record,position);

    }
}