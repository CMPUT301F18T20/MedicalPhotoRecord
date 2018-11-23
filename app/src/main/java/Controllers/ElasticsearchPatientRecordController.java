package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import static GlobalSettings.GlobalSettings.getIndex;
import static io.searchbox.params.Parameters.SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchPatientRecordController {

    static JestDroidClient client = null;

    public final static String matchAllquery =
            "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_all\" : {}" +
                    "    }\n" +
                    "}";

    public static void setClient(){
        if(client == null){

            DroidClientConfig config = new DroidClientConfig
                    .Builder("http://cmput301.softwareprocess.es:8080/")
                    .build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client=(JestDroidClient) factory.getObject();
        }
    }

    public static class DeletePatientRecordsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... PatientRecordIDs) {
            setClient();
            String query;

            //if the PatientRecordIDs are 1 entry or longer, do a query for the individual ids
            if (PatientRecordIDs.length >= 1) {
                String CombinedPatientRecordIDs = "";

                //add all strings to combined PatientRecordIDs for query
                for (String PatientRecordID : PatientRecordIDs) {
                    CombinedPatientRecordIDs = CombinedPatientRecordIDs.concat(" " + PatientRecordID);
                }

                //query for all supplied IDs
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"_id\" : \"" + CombinedPatientRecordIDs + "\" }" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("DeletePatientRecordQuer", query);

            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .build();

            try {
                JestResult result=client.execute(deleteByQueryTask);

                if(result.isSucceeded()){
                    return TRUE;
                }

                return FALSE;

            } catch(IOException e){
                Log.d("DeletePatientRecordQuer", "IOEXCEPTION");
            }

            return FALSE;
        }
    }

    public static class GetAllPatientRecords extends AsyncTask<String, Void, ArrayList<PatientRecord>>{
        @Override
        protected ArrayList<PatientRecord> doInBackground(String... PatientRecordIDs) {
            setClient();
            ArrayList<PatientRecord> PatientRecords = new ArrayList<>();

            Search search = new Search.Builder(matchAllquery)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .setParameter(SIZE, 10000)
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<PatientRecord> PatientRecordList;
                    PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);
                    PatientRecords.addAll(PatientRecordList);
                }

                for (PatientRecord PatientRecord : PatientRecords) {
                    Log.d("GetPatientRecord", "Fetched PatientRecord: " + PatientRecord.toString());
                }

            } catch(IOException e){
                Log.d("GetPatientRecord", "IOEXCEPTION");

            }

            return PatientRecords;
        }
    }
    public static class GetPatientRecordByPatientRecordIDTask extends AsyncTask<String, Void, PatientRecord>{
        @Override
        protected PatientRecord doInBackground(String... PatientRecordIDs) {
            setClient();
            PatientRecord returnPatientRecord = null;

            String query;

            //if the PatientRecordIDs are 1 entry or longer, do a query for the first id
            if (PatientRecordIDs.length >= 1) {
                String patientRecordIDForQuery = "\"" + PatientRecordIDs[0] + "\" ";

                //query for the supplied IDs
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"terms\": {\n" +
                                "            \"_id\": [" + patientRecordIDForQuery + "]\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("PRQueryByPRID", query + "\n" + PatientRecordIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<PatientRecord> PatientRecordList;
                    PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);
                    returnPatientRecord = PatientRecordList.get(0);
                    Log.d("PRQueryByPRID", "Fetched PatientRecord: " + returnPatientRecord.toString());
                }

            } catch(IOException e){
                Log.d("PRQueryByPRID", "IOEXCEPTION");

            }

            return returnPatientRecord;
        }
    }

    public static class GetPatientRecordsWithProblemID extends AsyncTask<String, Void, ArrayList<PatientRecord>>{
        @Override
        protected ArrayList<PatientRecord> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<PatientRecord> PatientRecords = new ArrayList<>();
            String query;

            if (UserIDs.length < 1) {
                return null;
            }

            //query for PatientRecords associated with problem id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"associatedProblemID\" : \"" + UserIDs[0] + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("PtntRcrdQueryByPrblemID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .setParameter(SIZE, 10000)
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<PatientRecord> PatientRecordList;
                    PatientRecordList=result.getSourceAsObjectList(PatientRecord.class);
                    PatientRecords.addAll(PatientRecordList);
                }

                for (PatientRecord PatientRecord : PatientRecords) {
                    Log.d("PtntRcrdQueryByPrblemID", "Fetched PatientRecord: " + PatientRecord.toString());
                }

            } catch(IOException e){
                Log.d("PtntRcrdQueryByPrblemID", "IOEXCEPTION");

            }

            return PatientRecords;
        }
    }

    public static class AddPatientRecordTask extends AsyncTask<PatientRecord, Void, Void>{
        @Override
        protected Void doInBackground(PatientRecord... UserIDs){
            setClient();

            PatientRecord PatientRecord = UserIDs[0];
            Index index=new Index.Builder(PatientRecord)
                    .index(getIndex())
                    .type("PatientRecord")
                    .build();

            int tryCounter = 100;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        PatientRecord.setElasticSearchID(result.getId());
                        Log.d("AddPatientRecord", "Success, added " + PatientRecord.toString());

                        //success
                        break;
                    } else {
                        Log.d("AddPatientRecord",
                                "Try:" + tryCounter + ", Failed to add " + PatientRecord.toString());
                    }

                } catch (IOException e) {
                    Log.d("AddPatientRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }

                tryCounter--;
            }
            return null;

        }
    }

    public static class SaveModifiedPatientRecord extends AsyncTask<PatientRecord, Void, Void> {
        @Override
        protected Void doInBackground(PatientRecord... UserID) {
            setClient();
            PatientRecord PatientRecord = UserID[0];
            try {
                JestResult result = client.execute(
                        new Index.Builder(PatientRecord)
                                .index(getIndex())
                                .type("PatientRecord")
                                .id(PatientRecord.getElasticSearchID())
                                .build()
                );

                if (result.isSucceeded()) {
                    Log.d("ModifyPatientRecord", "Success, modified " + PatientRecord.toString());
                } else {
                    Log.d("ModifyPatientRecord", "Failed to modify " + PatientRecord.toString());
                }
            } catch (IOException e) {
                Log.d("ModifyPatientRecord", "IOEXCEPTION");
            }
            return null;
        }
    }
}
