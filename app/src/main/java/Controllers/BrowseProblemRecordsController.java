package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;

public class BrowseProblemRecordsController {

    public ArrayList<Record> getRecordList(Context context, String userID, int position) {


        ArrayList<Problem> problems = new BrowseUserProblemsController().getProblemList(context, userID);

        ArrayList<Record> records = problems.get(position).getRecords();
        Log.d("Wasteman", "" + position);
        Log.d("WASTEMAN", records.toString());


        return records;

    }
}
