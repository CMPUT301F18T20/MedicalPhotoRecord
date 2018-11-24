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

import static GlobalSettings.GlobalSettings.NumberOfElasticsearchRetries;
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


    /**
     * String input is nothing to delete all Records in index, and a list of
     * UUIDs to delete specific Records
     */
    public static class DeleteRecordsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... RecordUUIDs) {
            setClient();
            String query;

            //if the RecordUUIDs are 1 entry or longer, do a query for the individual ids
            if (RecordUUIDs.length >= 1) {
                String CombinedRecordUUIDs = "";

                //add all strings to combined RecordUUIDs for query
                for (String RecordID : RecordUUIDs) {
                    CombinedRecordUUIDs = CombinedRecordUUIDs.concat(" " + RecordID);
                }

                //query for all supplied UUIDs
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"UUID\" : \"" + CombinedRecordUUIDs + "\" }" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("DeleteRecordQuer", query);

            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(deleteByQueryTask);

                    if (result.isSucceeded()) {
                        return TRUE;
                    }

                } catch (IOException e) {
                    Log.d("DeleteRecordQuer", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
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

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Record> RecordList;
                        RecordList = result.getSourceAsObjectList(Record.class);
                        Records.addAll(RecordList);
                        for (Record record : Records) {
                            Log.d("GetRecord", "Fetched Record: " + record.toString());
                        }
                        return Records;
                    }

                } catch (IOException e) {
                    Log.d("GetRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return Records;
        }
    }

    public static class GetRecordByRecordUUIDTask extends AsyncTask<String, Void, Record>{
        @Override
        protected Record doInBackground(String... RecordUUIDs) {
            setClient();
            Record returnRecord = null;

            String query;

            //if the RecordUUIDs are 1 entry or longer, do a query for the first id
            if (RecordUUIDs.length >= 1) {
                String recordUUIDForQuery = "\"" + RecordUUIDs[0] + "\"";

                //query for the supplied UUID
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"UUID\": " + recordUUIDForQuery +" }\n" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("RQueryByUUID", query + "\n" + RecordUUIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Record> RecordList;
                        RecordList = result.getSourceAsObjectList(Record.class);

                        //if we actually got a result, set it
                        if (RecordList.size() > 0) {
                            returnRecord = RecordList.get(0);
                            Log.d("RQueryByRUUID", "Fetched Record: " + returnRecord.toString());
                        }

                        //might be null if the search returned no results
                        return returnRecord;

                    }

                } catch (IOException e) {
                    Log.d("RQueryByRUUID", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return returnRecord;
        }
    }

    public static class GetRecordsWithProblemUUID extends AsyncTask<String, Void, ArrayList<Record>>{
        @Override
        protected ArrayList<Record> doInBackground(String... ProblemUUIDs) {
            ArrayList<Record> Records = new ArrayList<>();
            String query;
            setClient();

            /* don't give me more than one problem uuid or I'll give you null */
            if (ProblemUUIDs.length < 1) {
                return Records;
            }

            //query for Records associated with problem id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"associatedProblemUUID\" : \"" + ProblemUUIDs[0] + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("RecordQuryByPrblmUUID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Record")
                    .setParameter(SIZE, 10000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Record> RecordList = result.getSourceAsObjectList(Record.class);
                        Records.addAll(RecordList);
                        for (Record Record : Records) {
                            Log.d("RecordQuryByPrblmUUID", "Fetched Record: " + Record.toString());
                        }
                        return Records;
                    }
                } catch (IOException e) {
                    Log.d("RecordQuryByPrblmUUID", "IOEXCEPTION");
                }
                tryCounter--;
            }

            return Records;
        }
    }

    public static class AddRecordTask extends AsyncTask<Record, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Record... Records){
            setClient();

            if (Records.length < 1) {
                return FALSE;
            }

            Record record = Records[0];
            Index index=new Index.Builder(record)
                    .index(getIndex())
                    .type("Record")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        record.setElasticSearchID(result.getId());
                        Log.d("AddRecord", "Success, added " + record.toString());
                        return TRUE;
                    } else {
                        Log.d("AddRecord", "Try:" + tryCounter +
                                ", Failed to add " + record.toString());
                    }

                } catch (IOException e) {
                    Log.d("AddRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class SaveModifiedRecord extends AsyncTask<Record, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Record... records) {
            setClient();

            //can't be empty
            if (records.length < 1) {
                return FALSE;
            }

            Record record = records[0];

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
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
                        return TRUE;
                    } else {
                        Log.d("ModifyRecord", "Try:" + tryCounter +
                                ", Failed to modify " + record.toString());
                    }
                } catch (IOException e) {
                    Log.d("ModifyRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }
}
