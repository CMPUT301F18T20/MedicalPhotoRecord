package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.User;
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
public class ElasticsearchProblemController extends ElasticsearchController {

    //TODO test
    //TODO this also needs to delete all patient records and records associated to this problem
    public static class DeleteAllProblemsForUserIDsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... UserIDs) {
            setClient();
            String query;

            //if the UserIDs are 1 entry or longer, do a query for the individual ids
            if (UserIDs.length >= 1) {
                String CombinedUserIDs = "";

                //add all strings to combined user ids for query
                //filter out userIDs shorter than 8 chars
                for (String UserID : UserIDs) {
                    if (UserID.length() >= 8) {
                        CombinedUserIDs = CombinedUserIDs.concat(" " + UserID);
                    }
                }

                //no valid user IDs to query, we don't need to run the query
                if (CombinedUserIDs.length() == 0) {
                    return TRUE;
                }

                //query for all supplied IDs greater than 7 characters
                query =
                        "{\n" +
                        "    \"query\": {\n" +
                        "        \"match\" : { \"CreatedByUserID\" : \"" + CombinedUserIDs + "\" }" +
                        "    }\n" +
                        "}";

            } else {
                query = matchAllquery;
            }

            Log.d("ProblemDeleteQuery", query);


            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(deleteByQueryTask);

                    if (result.isSucceeded()) {
                        return TRUE;
                    }

                    return FALSE;

                } catch (IOException e) {
                    Log.d("ProblemDelete", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            //operation failed
            return FALSE;
        }
    }

    private static Boolean DeleteCode(String... ProblemUUIDs) {
        String query;

        //if the ProblemUUIDs are 1 entry or longer, do a query for the individual ids
        if (ProblemUUIDs.length >= 1) {
            String CombinedProblemUUIDs = "";

            //add all strings to combined ProblemUUIDs for query
            for (String ProblemID : ProblemUUIDs) {
                CombinedProblemUUIDs = CombinedProblemUUIDs.concat(" " + ProblemID);
            }

            //query for all supplied UUIDs
            query =
                    "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : { \"UUID\" : \"" + CombinedProblemUUIDs + "\" }" +
                            "    }\n" +
                            "}";

        } else {
            query = matchAllquery;
        }

        Log.d("DeleteProblemQuer", query);

        DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("Problem")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }

            } catch (IOException e) {
                Log.d("DeleteProblemQuer", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    /**
     * String input is nothing to delete all Problems in index, and a list of
     * UUIDs to delete specific Problems
     */
    public static class DeleteProblemsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... ProblemUUIDs) {
            setClient();
            return DeleteCode(ProblemUUIDs);
        }
    }

    public static class GetProblemByProblemUUIDTask extends AsyncTask<String, Void, Problem>{
        @Override
        protected Problem doInBackground(String... ProblemUUIDs) {
            setClient();
            Problem returnProblem = null;

            String query;

            //if the ProblemUUIDs are 1 entry or longer, do a query for the first id
            if (ProblemUUIDs.length >= 1) {
                String problemUUIDForQuery = "\"" + ProblemUUIDs[0] + "\"";

                //query for the supplied UUID
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"UUID\": " + problemUUIDForQuery +" }\n" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("RQueryByUUID", query + "\n" + ProblemUUIDs.toString());

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Problem> ProblemList;
                        ProblemList = result.getSourceAsObjectList(Problem.class);

                        //if we actually got a result, set it
                        if (ProblemList.size() > 0) {
                            returnProblem = ProblemList.get(0);
                            returnProblem.clearArrays();
                            Log.d("RQueryByRUUID", "Fetched Problem: " + returnProblem.toString());
                        }

                        //might be null if the search returned no results
                        return returnProblem;

                    }

                } catch (IOException e) {
                    Log.d("RQueryByRUUID", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return returnProblem;
        }
    }

    public static class GetProblemsCreatedByUserIDTask extends AsyncTask<String, Void, ArrayList<Problem>>{
        @Override
        protected ArrayList<Problem> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<Problem> Problems = new ArrayList<>();
            String query;

            if (UserIDs.length < 1) {
                return null;
            }

            //query for problems created by user id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"createdByUserID\" : \"" + UserIDs[0] + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("ProblemQueryByUserID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .setParameter(SIZE, 10000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Problem> ProblemList = result.getSourceAsObjectList(Problem.class);
                        Problems.addAll(ProblemList);
                        for (Problem problem : Problems) {
                            Log.d("GetProblemByUserID", "Fetched Problem: " + problem.toString());
                            problem.clearArrays();
                        }
                        return Problems;
                    }

                } catch (IOException e) {
                    Log.d("GetProblemByUserID", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return Problems;
        }
    }

    public static class AddProblemTask extends AsyncTask<Problem, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Problem... Problems){
            setClient();

            if (Problems.length < 1) {
                return FALSE;
            }

            Problem problem = Problems[0];
            problem.clearArrays();
            Index index=new Index.Builder(problem)
                    .index(getIndex())
                    .id(problem.getUUID())
                    .type("Problem")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("AddProblem", "Success, added " + problem.toString());
                        return TRUE;
                    } else {
                        Log.d("AddProblem", "Try:" + tryCounter +
                                ", Failed to add " + problem.toString());
                    }

                } catch (IOException e) {
                    DeleteCode(problem.getUUID());
                    Log.d("AddProblem", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class SaveModifiedProblem extends AsyncTask<Problem, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Problem... problems) {
            setClient();

            //can't be empty
            if (problems.length < 1) {
                return FALSE;
            }

            Problem problem = problems[0];
            problem.clearArrays();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(
                            new Index.Builder(problem)
                                    .index(getIndex())
                                    .type("Problem")
                                    .id(problem.getUUID())
                                    .build()
                    );

                    if (result.isSucceeded()) {
                        Log.d("ModifyProblem", "Success, modified " + problem.toString());
                        return TRUE;
                    } else {
                        Log.d("ModifyProblem", "Try:" + tryCounter +
                                ", Failed to modify " + problem.toString());
                    }
                } catch (IOException e) {
                    Log.d("ModifyProblem", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    //due to network restrictions on the main thread in android, this must be called like so:
    //problems = new ElasticsearchProblemController.QueryByUserIDWithKeywords(UserID).execute(keywords).get();
    public static class QueryByUserIDWithKeywords extends AsyncTask<String, Void, ArrayList<Problem>> {
        String UserID = "";
        public QueryByUserIDWithKeywords(String UserID) {
            this.UserID = UserID;
        }

        @Override
        protected ArrayList<Problem> doInBackground(String... keywords) {
            setClient();

            //if userID is blank, no need to even query, return null
            if (UserID.equals("")) {
                return null;
            }

            ArrayList<Problem> Problems = new ArrayList<>();

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
                    "            { \"match\" : { \"description\" : \"" + combinedKeywords + "\" } } \n" +
                    "        ], \n" +
                    "        \"minimum_should_match\" : 1 \n" +
                    "        } \n" +
                    "    } \n" +
                    "}";

            Log.d("GetByKeywords", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .setParameter(SIZE, 10000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Problem> ProblemList = result.getSourceAsObjectList(Problem.class);
                        Problems.addAll(ProblemList);
                        for (Problem problem : Problems) {
                            Log.d("GetByKeywords", "Fetched Problem: " + problem.toString());
                            problem.clearArrays();
                        }
                        return Problems;
                    }

                } catch (IOException e) {
                    Log.d("GetByKeywords", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }

            return Problems;
        }
    }
}
