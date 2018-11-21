/*
 * Class name: ElasticsearchProblemController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 1:59 PM
 *
 * Last Modified: 15/11/18 1:59 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;

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
public class ElasticsearchProblemController {

    public final static String matchAllquery =
            "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_all\" : {}" +
                    "    }\n" +
                    "}";

    static JestDroidClient client = null;

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

    public static class GetProblemsTask extends AsyncTask<String, Void, ArrayList<Problem>> {
        
        @Override
        protected ArrayList<Problem> doInBackground(String... params) {
            setClient();
            ArrayList<Problem> Problems = new ArrayList<Problem>();

            //no query yet
            Search search = new Search.Builder("")
                    .addIndex(getIndex())
                    .addType("Problem")
                    .setParameter(SIZE,"10000")
                    .build();
            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Problem> ProblemList;
                    ProblemList=result.getSourceAsObjectList(Problem.class);
                    Problems.addAll(ProblemList);
                }

            }catch(IOException e){}

            return Problems;
        }
    }

    public static class AddProblemTask extends AsyncTask<Problem, Void, Void>{
        @Override
        protected Void doInBackground(Problem... params){
            setClient();

            Problem Problem = params[0];
            Index index=new Index.Builder(Problem)
                    .index(getIndex())
                    .type("Problem")
                    .build();

            try {
                DocumentResult result = client.execute(index);
                if(result.isSucceeded()){
                }

            }catch(IOException e){
                //do something here
                Log.d("Hello", "IOEXCEPTION");
            }
            return null;
        }
    }
}
