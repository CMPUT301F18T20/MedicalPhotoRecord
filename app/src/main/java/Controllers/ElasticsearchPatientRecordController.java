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

import static GlobalSettings.GlobalSettings.NumberOfElasticsearchRetries;
import static GlobalSettings.GlobalSettings.getIndex;
import static io.searchbox.params.Parameters.SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchPatientRecordController extends ElasticsearchController {

    private static Boolean DeleteCode(String... PatientRecordUUIDs) {
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

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }


            } catch (IOException e) {
                Log.d("DeletePatientRecordQuer", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    /**
     * String input is nothing to delete all PatientRecords in index, and a list of
     * UUIDs to delete specific PatientRecords
     */
    public static class DeletePatientRecordsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... PatientRecordUUIDs) {
            setClient();
            return DeleteCode(PatientRecordUUIDs);
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

            Log.d("PatientRcrdQueryByUUID", query + "\n" + PatientRecordUUIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<PatientRecord> PatientRecordList;
                        PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);

                        //if we actually got a result, set it
                        if (PatientRecordList.size() > 0) {
                            returnPatientRecord = PatientRecordList.get(0);
                            returnPatientRecord.clearArrays();
                            Log.d("PatientRcrdQueryByUUID", "Fetched PatientRecord: " + returnPatientRecord.toString());
                        }

                        //might be null if the search returned no results
                        return returnPatientRecord;

                    }

                } catch (IOException e) {
                    Log.d("PatientRcrdQueryByUUID", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
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
                return PatientRecords;
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

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<PatientRecord> PatientRecordList =
                                result.getSourceAsObjectList(PatientRecord.class);
                        PatientRecords.addAll(PatientRecordList);
                        for (PatientRecord PatientRecord : PatientRecords) {
                            Log.d("PtntRcrdQuryByPrblmUUID",
                                    "Fetched PatientRecord: " + PatientRecord.toString());
                            PatientRecord.clearArrays();
                        }
                        return PatientRecords;
                    }
                } catch (IOException e) {
                    Log.d("PtntRcrdQuryByPrblmUUID", "IOEXCEPTION");
                }
                tryCounter--;
            }

            return PatientRecords;
        }
    }

    public static class AddPatientRecordTask extends AsyncTask<PatientRecord, Void, Boolean>{
        @Override
        protected Boolean doInBackground(PatientRecord... PatientRecords){
            setClient();

            if (PatientRecords.length < 1) {
                return FALSE;
            }

            PatientRecord patientRecord = PatientRecords[0];
            patientRecord.clearArrays();
            Index index=new Index.Builder(patientRecord)
                    .index(getIndex())
                    .type("PatientRecord")
                    .id(patientRecord.getUUID())
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("AddPatientRecord", "Success, added " + patientRecord.toString());
                        return TRUE;
                    } else {
                        Log.d("AddPatientRecord", "Try:" + tryCounter +
                                ", Failed to add " + patientRecord.toString());
                    }

                } catch (IOException e) {
                    DeleteCode(patientRecord.getUUID());
                    Log.d("AddPatientRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class SaveModifiedPatientRecord extends AsyncTask<PatientRecord, Void, Boolean> {
        @Override
        protected Boolean doInBackground(PatientRecord... patientRecords) {
            setClient();

            //can't be empty
            if (patientRecords.length < 1) {
                return FALSE;
            }

            PatientRecord patientRecord = patientRecords[0];
            patientRecord.clearArrays();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(
                            new Index.Builder(patientRecord)
                                    .index(getIndex())
                                    .type("PatientRecord")
                                    .id(patientRecord.getUUID())
                                    .build()
                    );

                    if (result.isSucceeded()) {
                        Log.d("ModifyPatientRecord", "Success, modified " + patientRecord.toString());
                        return TRUE;
                    } else {
                        Log.d("ModifyPatientRecord", "Try:" + tryCounter +
                                ", Failed to modify " + patientRecord.toString());
                    }
                } catch (IOException e) {
                    Log.d("ModifyPatientRecord", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    //due to network restrictions on the main thread in android, this must be called like so:
    //problems = new ElasticsearchPatientRecordController.QueryByUserIDWithKeywords(UserID).execute(keywords).get();
    public static class QueryByUserIDWithKeywords extends AsyncTask<String, Void, ArrayList<PatientRecord>> {
        String UserID = "";

        public QueryByUserIDWithKeywords(String UserID) {
            this.UserID = UserID;
        }

        @Override
        protected ArrayList<PatientRecord> doInBackground(String... keywords) {
            setClient();

            //if userID is blank, no need to even query, return null
            if (UserID.equals("")) {
                return null;
            }

            ArrayList<PatientRecord> PatientRecords = new ArrayList<>();

            //add all keywords together
            String combinedKeywords = "";
            for (String keyword : keywords) {
                combinedKeywords = combinedKeywords.concat(" " + keyword);
            }

            String query = "{\n " +
                    "\"query\": { \n" +
                    "    \"bool\" : { \n" +
                    "        \"must\": { \n" +
                    "            \"match\" : { \"createdByUserID\" : \"" + UserID + "\" }\n " +
                    "        }, \n" +
                    "        \"should\" : [ \n" +
                    "            { \"match\" : { \"title\" : \"" + combinedKeywords + "\" } }, \n" +
                    "            { \"match\" : { \"description\" : \"" + combinedKeywords + "\" } }, \n" +
                    "            { \"match\" : { \"comment\" : \"" + combinedKeywords + "\" } } \n" +
                    "        ], \n" +
                    "        \"minimum_should_match\" : 1 \n" +
                    "        } \n" +
                    "    } \n" +
                    "}";

            Log.d("GetByKeywords", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .setParameter(SIZE, 10000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<PatientRecord> PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);
                        PatientRecords.addAll(PatientRecordList);
                        for (PatientRecord patientRecord : PatientRecords) {
                            Log.d("GetByKeywords", "Fetched PatientRecord: " + patientRecord.toString());
                        }
                        return PatientRecords;
                    }

                } catch (IOException e) {
                    Log.d("GetByKeywords", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return PatientRecords;

        }
    }

    public static class GetPatientRecordsCreatedByUserIDTask extends AsyncTask<String, Void, ArrayList<PatientRecord>> {
        @Override
        protected ArrayList<PatientRecord> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<PatientRecord> PatientRecords = new ArrayList<>();
            String query;

            if (UserIDs.length < 1) {
                return null;
            }

            //query for patientRecords created by user id
            query =
                    "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : { \"createdByUserID\" : \"" + UserIDs[0] + "\" }" +
                            "    }\n" +
                            "}";

            Log.d("PtntRcrdQueryByUserID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("PatientRecord")
                    .setParameter(SIZE, 10000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<PatientRecord> PatientRecordList = result.getSourceAsObjectList(PatientRecord.class);
                        PatientRecords.addAll(PatientRecordList);
                        for (PatientRecord patientRecord : PatientRecords) {
                            Log.d("PtntRcrdQueryByUserID", "Fetched PatientRecord: " + patientRecord.toString());
                        }
                        return PatientRecords;
                    }

                } catch (IOException e) {
                    Log.d("PtntRcrdQueryByUserID", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return PatientRecords;
        }
    }

}
