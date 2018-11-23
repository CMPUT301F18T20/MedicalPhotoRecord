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

    /**
     * String input is nothing to delete all Patients in index, and a list of
     * UUIDs to delete specific PatientRecords
     */
    public static class DeletePatientRecordsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... PatientRecordUUIDs) {
            setClient();
            String query;

            //if the PatientRecordUUIDs are 1 entry or longer, do a query for the individual ids
            if (PatientRecordUUIDs.length >= 1) {
                String CombinedPatientRecordUUIDs = "";

                //add all strings to combined PatientRecordUUIDs for query
                for (String PatientRecordID : PatientRecordUUIDs) {
                    CombinedPatientRecordUUIDs = CombinedPatientRecordUUIDs.concat(" " + PatientRecordID);
                }

                //query for all supplied UUIDs
                query =
                        "{\n" +
                        "    \"query\": {\n" +
                        "        \"match\" : { \"UUID\" : \"" + CombinedPatientRecordUUIDs + "\" }" +
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
        protected ArrayList<PatientRecord> doInBackground(String... PatientRecordUUIDs) {
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
    public static class GetPatientRecordByPatientRecordUUIDTask extends AsyncTask<String, Void, PatientRecord>{
        @Override
        protected PatientRecord doInBackground(String... PatientRecordUUIDs) {
            setClient();
            PatientRecord returnPatientRecord = null;

            String query;

            //if the PatientRecordUUIDs are 1 entry or longer, do a query for the first id
            if (PatientRecordUUIDs.length >= 1) {
                String patientRecordUUIDForQuery = "\"" + PatientRecordUUIDs[0] + "\"";

                //query for the supplied UUID
                query =
                        "{\n" +
                        "    \"query\": {\n" +
                        "        \"match\" : { \"UUID\": " + patientRecordUUIDForQuery +" }\n" +
                        "    }\n" +
                        "}";

            } else {
                query = matchAllquery;
            }

            Log.d("PRQueryByUUID", query + "\n" + PatientRecordUUIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<PatientRecord> PatientRecordList;
                    PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);
                    returnPatientRecord = PatientRecordList.get(0); //TODO GET THIS SAFER
                    Log.d("PRQueryByPRUUID", "Fetched PatientRecord: " + returnPatientRecord.toString());
                }

            } catch(IOException e){
                Log.d("PRQueryByPRUUID", "IOEXCEPTION");
            }

            return returnPatientRecord;
        }
    }

    public static class GetPatientRecordsWithProblemUUID extends AsyncTask<String, Void, ArrayList<PatientRecord>>{
        @Override
        protected ArrayList<PatientRecord> doInBackground(String... ProblemUUIDs) {
            ArrayList<PatientRecord> PatientRecords = new ArrayList<>();
            String query;

            setClient();

            /* don't give me more than one problem uuid or I'll give you null */
            if (ProblemUUIDs.length < 1) {
                return null;
            }

            //query for PatientRecords associated with problem id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"associatedProblemUUID\" : \"" + ProblemUUIDs[0] + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("PtntRcrdQuryByPrblmUUID", query);

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
                    Log.d("PtntRcrdQuryByPrblmUUID", "Fetched PatientRecord: " + PatientRecord.toString());
                }

            } catch(IOException e){
                Log.d("PtntRcrdQuryByPrblmUUID", "IOEXCEPTION");

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