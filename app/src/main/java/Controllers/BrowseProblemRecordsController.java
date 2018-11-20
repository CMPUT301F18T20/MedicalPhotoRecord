package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;

public class BrowseProblemRecordsController {

    public ArrayList<Record> getRecordList(Context context, String userID, int position) {

        Patient patient = new ModifyUserController().getPatient(context, userID);

        ArrayList<Problem> problems = patient.getProblems();

        ArrayList<Record> records = problems.get(position).getRecords();



        return records;

    }
}
