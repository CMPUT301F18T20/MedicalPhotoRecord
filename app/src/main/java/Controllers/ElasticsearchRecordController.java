package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Record;
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
public class ElasticsearchRecordController {

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

    public static class DeleteRecordsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... RecordIDs) {
            setClient();
            String query;

            //if the RecordIDs are 1 entry or longer, do a query for the individual ids
            if (RecordIDs.length >= 1) {
                String CombinedRecordIDs = "";

                //add all strings to combined RecordIDs for query
                for (String RecordID : RecordIDs) {
                    CombinedRecordIDs = CombinedRecordIDs.concat(" " + RecordID);
                }

                //query for all supplied IDs
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"_id\" : \"" + CombinedRecordIDs + "\" }" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("DeleteRecordQuery", query);

            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .build();

            try {
                JestResult result=client.execute(deleteByQueryTask);

                if(result.isSucceeded()){
                    return TRUE;
                }

                return FALSE;

            } catch(IOException e){
                Log.d("DeleteRecordQuery", "IOEXCEPTION");
            }

            return FALSE;
        }
    }

    public static class GetAllRecords extends AsyncTask<String, Void, ArrayList<Record>>{
        @Override
        protected ArrayList<Record> doInBackground(String... RecordIDs) {
            setClient();
            ArrayList<Record> Records = new ArrayList<>();

            Search search = new Search.Builder(matchAllquery)
                    .addIndex(getIndex())
                    .addType("Record")
                    .setParameter(SIZE, 10000)
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Record> RecordList;
                    RecordList = result.getSourceAsObjectList(Record.class);
                    Records.addAll(RecordList);
                }

                for (Record record : Records) {
                    Log.d("GetRecord", "Fetched Record: " + record.toString());
                }

            } catch(IOException e){
                Log.d("GetRecord", "IOEXCEPTION");

            }

            return Records;
        }
    }
    public static class GetRecordByRecordIDTask extends AsyncTask<String, Void, ArrayList<Record>>{
        @Override
        protected ArrayList<Record> doInBackground(String... RecordIDs) {
            setClient();
            ArrayList<Record> Records = new ArrayList<>();

            String query;

            //if the RecordIDs are 1 entry or longer, do a query for the first id
            if (RecordIDs.length >= 1) {
                String recordIDForQuery = "\"" + RecordIDs[0] + "\" ";

                //query for the supplied IDs
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"terms\": {\n" +
                                "            \"_id\": [" + recordIDForQuery + "]\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("RecordQueryByRecordID", query + "\n" + RecordIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Record> RecordList;
                    RecordList = result.getSourceAsObjectList(Record.class);
                    Records.addAll(RecordList);
                }

                for (Record record : Records) {
                    Log.d("GetRecordByRecordID", "Fetched Record: " + record.toString());
                }

            } catch(IOException e){
                Log.d("GetRecordByRecordID", "IOEXCEPTION");

            }

            return Records;
        }
    }

    public static class GetRecordsWithProblemID extends AsyncTask<String, Void, ArrayList<Record>>{
        @Override
        protected ArrayList<Record> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<Record> Records = new ArrayList<>();
            String query;

            if (UserIDs.length < 1) {
                return null;
            }

            //query for records associated with problem id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"associatedProblemID\" : \"" + UserIDs[0] + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("RecordQueryByProblemID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .setParameter(SIZE, 10000)
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Record> RecordList;
                    RecordList=result.getSourceAsObjectList(Record.class);
                    Records.addAll(RecordList);
                }

                for (Record record : Records) {
                    Log.d("GetRecordByProblemID", "Fetched Record: " + record.toString());
                }

            } catch(IOException e){
                Log.d("GetRecordByProblemID", "IOEXCEPTION");

            }

            return Records;
        }
    }

    public static class AddRecordTask extends AsyncTask<Record, Void, Void>{
        @Override
        protected Void doInBackground(Record... UserIDs){
            setClient();

            Record record = UserIDs[0];
            Index index=new Index.Builder(record)
                    .index(getIndex())
                    .type("Record")
                    .build();

            int tryCounter = 100;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        record.setElasticSearchID(result.getId());
                        Log.d("AddRecord", "Success, added " + record.toString());

                        //success
                        break;
                    } else {
                        Log.d("AddRecord",
                                "Try:" + tryCounter + ", Failed to add " + record.toString());
                    }

                } catch (IOException e) {
                    Log.d("AddRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }

                tryCounter--;
            }
            return null;

        }
    }

    public static class SaveModifiedRecord extends AsyncTask<Record, Void, Void> {
        @Override
        protected Void doInBackground(Record... UserID) {
            setClient();
            Record record = UserID[0];
            try {
                JestResult result = client.execute(
                        new Index.Builder(record)
                                .index(getIndex())
                                .type("Record")
                                .id(record.getElasticSearchID())
                                .build()
                );

                if (result.isSucceeded()) {
                    Log.d("ModifyRecord", "Success, modified " + record.toString());
                } else {
                    Log.d("ModifyRecord", "Failed to modify " + record.toString());
                }
            } catch (IOException e) {
                Log.d("ModifyRecord", "IOEXCEPTION");
            }
            return null;
        }
    }
}
