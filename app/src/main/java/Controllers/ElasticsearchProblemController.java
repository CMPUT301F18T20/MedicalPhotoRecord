package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Problem;
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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchProblemController {

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

    public static class DeleteProblemsTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... ProblemIDs) {
            setClient();
            String query;

            //if the ProblemIDs are 1 entry or longer, do a query for the individual ids
            if (ProblemIDs.length >= 1) {
                String CombinedProblemIDs = "";

                //add all strings to combined ProblemIDs for query
                for (String ProblemID : ProblemIDs) {
                    CombinedProblemIDs = CombinedProblemIDs.concat(" " + ProblemID);
                }

                //query for all supplied IDs
                query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"_id\" : \"" + CombinedProblemIDs + "\" }" +
                    "    }\n" +
                    "}";

            } else {
                query = matchAllquery;
            }

            Log.d("ProblemQuery", query);


            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .build();

            try {
                JestResult result=client.execute(deleteByQueryTask);

                if(result.isSucceeded()){
                    return TRUE;
                }

                return FALSE;

            } catch(IOException e){
                Log.d("GetProblem", "IOEXCEPTION");
            }

            return FALSE;
        }
    }
    
    public static class GetProblemsByProblemIDsTask extends AsyncTask<String, Void, ArrayList<Problem>>{
        @Override
        protected ArrayList<Problem> doInBackground(String... ProblemIDs) {
            setClient();
            ArrayList<Problem> Problems = new ArrayList<>();

            String query;

            //if the ProblemIDs are 1 entry or longer, do a query for the individual ids
            if (ProblemIDs.length >= 1) {
                String CombinedProblemIDs = "";

                //add all strings to combined ProblemIDs for query
                for (String ProblemID : ProblemIDs) {
                    CombinedProblemIDs = CombinedProblemIDs.concat("\"" + ProblemID + "\" " );
                }

                //query for all supplied IDs
                query =
                        "{\n" +
                        "    \"query\": {\n" +
                        "        \"terms\": {\n" +
                        "            \"_id\": [" + CombinedProblemIDs + "]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}";

            } else {
                query = matchAllquery;
            }

            Log.d("ProblemQuery", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Problem> ProblemList;
                    ProblemList = result.getSourceAsObjectList(Problem.class);
                    Problems.addAll(ProblemList);
                }

                for (Problem problem : Problems) {
                    Log.d("GetProblem", "Fetched Problem: " + problem.toString());
                }

            } catch(IOException e){
                Log.d("GetProblem", "IOEXCEPTION");

            }

            return Problems;
        }
    }
/*
    public static class GetProblemsCreatedByUserIDTask extends AsyncTask<String, Void, ArrayList<Problem>>{
        @Override
        protected ArrayList<Problem> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<Problem> Problems = new ArrayList<>();
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

                //no user IDs to query, return empty array
                if (CombinedUserIDs.length() == 0) {
                    return Problems;
                }

                //query for all supplied IDs greater than 7 characters
                query =
                        "{\n" +
                                "    \"query\": {\n" +
                                "        \"match\" : { \"UserID\" : \"" + CombinedUserIDs + "\" }" +
                                "    }\n" +
                                "}";

            } else {
                query = matchAllquery;
            }

            Log.d("ProblemQuery", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Problem")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Problem> ProblemList;
                    ProblemList=result.getSourceAsObjectList(Problem.class);
                    Problems.addAll(ProblemList);
                }

                for (Problem problem : Problems) {
                    Log.d("GetProblem", "Fetched Problem: " + problem.toString());
                }

            } catch(IOException e){
                Log.d("GetProblem", "IOEXCEPTION");

            }

            return Problems;
        }
    }
*/
    public static class AddProblemTask extends AsyncTask<Problem, Void, Void>{
        @Override
        protected Void doInBackground(Problem... UserIDs){
            setClient();

            Problem problem = UserIDs[0];
            Index index=new Index.Builder(problem)
                    .index(getIndex())
                    .type("Problem")
                    .build();

            try {
                DocumentResult result = client.execute(index);
                if(result.isSucceeded()){
                    //add id to current object
                    problem.setElasticSearchID(result.getId());
                    Log.d("AddProblem", "Success, added " + problem.toString());
                } else {
                    Log.d("AddProblem", "Failed to add " + problem.toString());
                }

            }catch(IOException e){
                //do something here
                Log.d("AddProblem", "IOEXCEPTION");
            }
            return null;

        }
    }

    public static class SaveModifiedProblem extends AsyncTask<Problem, Void, Void> {
        @Override
        protected Void doInBackground(Problem... UserID) {
            setClient();
            Problem problem = UserID[0];
            try {
                JestResult result = client.execute(
                        new Index.Builder(problem)
                                .index(getIndex())
                                .type("Problem")
                                .id(problem.getElasticSearchID())
                                .build()
                );

                if (result.isSucceeded()) {
                    Log.d("ModifyProblem", "Success, modified " + problem.toString());
                } else {
                    Log.d("ModifyProblem", "Failed to modify " + problem.toString());
                }
            } catch (IOException e) {
                Log.d("ModifyProblem", "IOEXCEPTION");
            }
            return null;
        }
    }
}
