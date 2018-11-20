package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.Date;

import Activities.BrowseProblemRecords;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class AddRecordController {

    public Record createRecord (Context context, String userID, String title, Date date, String description) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        Record record = new Record(userID, title);
        record.setDate(date);
        record.setDescription(description);

        return record;
    }

    public void saveRecord(String mode, Context context, Record record, int position){
        // Get patient
        Patient patient = new ModifyUserController().getPatient(context, record.getCreatedByUserID());
        ArrayList<Problem> problems;
        problems = patient.getProblems();


        if (mode.equals("add")){
            problems.get(position).addRecord(record);
        }
        if (mode.equals("delete")){

            // Has to search for problem then delete b/c of date issue again
            for (Record rec : new ArrayList<>(problems.get(position).getRecords())){
                if (rec.getTitle().equals(record.getTitle())){
                    problems.get(position).removeRecord(rec);


                }
            }
        }

        new ModifyUserController().savePatient(context, patient);

    }
}
