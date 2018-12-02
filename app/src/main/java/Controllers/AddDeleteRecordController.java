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

import static GlobalSettings.GlobalSettings.ISCONNECTED;

/**
 * AddDeleteRecordController
 * Can create a new record with input attributes
 * Can add given  new record online and offline
 * Can remove given problem online and offline
 * @version 2.0
 * @see Record
 */

public class AddDeleteRecordController {

    /**
     * Create a record object with createdByUserId, title, date and description
     * @param problemUUID
     * @param userID
     * @param title
     * @param date
     * @param description
     * @return
     * @throws UserIDMustBeAtLeastEightCharactersException thrown when < 8 characters
     * @throws TitleTooLongException thrown when > 30 characters
     */
    public PatientRecord createRecord (String problemUUID, String userID, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        PatientRecord record = new PatientRecord(userID, title);
        record.setAssociatedProblemUUID(problemUUID);
        record.setDate(date);
        record.setDescription(description);

        return record;
    }

    /**
     * Add patient record to elastic search database and offline database
     * @param context: activity to be passed for offline save and load
     * @param record: patient record to be added to database
     */
    public void saveRecord(Context context, PatientRecord record) {

        // Check connection
        Boolean isConnected = ISCONNECTED;

        if (isConnected == true){
            //Online
            try {
                new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(record).get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }

        //Offline (always save)
        new OfflinePatientRecordController().addPatientRecord(context, record);
    }

    /**
     * Remove patient record from elastic search database and offline database
     * @param context: activity to be passed for offline save and load
     * @param record: patient record to be removed to database
     */
    public void deleteRecord(Context context, PatientRecord record){

        // Check connection
        Boolean isConnected = ISCONNECTED;

        if (isConnected == true){
            //Online
            try{
                new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute(record.getUUID()).get();
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }catch (ExecutionException e2){
                e2.printStackTrace();
            }
        }

        //Offline (always save)
        new OfflinePatientRecordController().deletePatientRecord(context,record.getUUID());
    }

}
