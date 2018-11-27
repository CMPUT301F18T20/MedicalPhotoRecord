package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserIDPair;
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
import io.searchbox.params.Parameters;

import static GlobalSettings.GlobalSettings.NumberOfElasticsearchRetries;
import static GlobalSettings.GlobalSettings.getIndex;
import static io.searchbox.params.Parameters.SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchSecurityTokenAndUserIDPairController extends ElasticsearchController {

    private static Boolean DeleteCode(String... UserIDs) {
        String query;

        //delete the entry in the elasticsearch with this userID
        if (UserIDs.length >= 1) {
            String UserID = UserIDs[0];

            //delete entries with UserID
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"UserID\" : \"" + UserID + "\" }" +
                    "    }\n" +
                    "}";

        } else {
            //delete all entries
            query = matchAllquery;
        }

        Log.d("DeleteSecurityToken", query);

        DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("SecurityTokenAndUserIDPair")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }

            } catch (IOException e) {
                Log.d("DeleteSecurityToken", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    public static class DeleteSecurityTokenAndUserIDPairByUserIDTask 
            extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... UserIDs) {
            setClient();
            return DeleteCode(UserIDs);
        }
    }

    private static SecurityTokenAndUserIDPair getCode(String queryToMatch, String... StringsToMatch) {
        setClient();
        SecurityTokenAndUserIDPair returnSecurityToken = null;
        String query;

        //if the StringsToMatch are not at least 1 entry just return null
        if (StringsToMatch.length < 1) {
            return null;
        }

        //do a query for the first provided entry
        String stringToMatch = StringsToMatch[0];

        //query for the user token
        query =
                "{\n" +
                "    \"query\": {\n" +
                "        \"match\" : { \"" + queryToMatch + "\" : \"" + stringToMatch + "\" }" +
                "    }\n" +
                "}";

        Log.d("GetSecurityToken", query);

        Search search = new Search.Builder(query)
                .addIndex(getIndex())
                .addType("SecurityTokenAndUserIDPair")
                .setParameter(SIZE,"10000")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(search);

                if (result.isSucceeded()) {
                    List<SecurityTokenAndUserIDPair> SecurityTokenAndUserIDPairList;
                    SecurityTokenAndUserIDPairList =
                            result.getSourceAsObjectList(SecurityTokenAndUserIDPair.class);

                    //if we actually got a result, set it
                    if (SecurityTokenAndUserIDPairList.size() > 0) {
                        returnSecurityToken = SecurityTokenAndUserIDPairList.get(0);
                        Log.d("GetSecurityToken", "Fetched SecurityTokenAndUserIDPairID: " +
                                returnSecurityToken.toString());
                    }

                    return returnSecurityToken;
                }

            } catch (IOException e) {
                Log.d("GetSecurityToken", "Try:" + tryCounter + ", IOEXCEPTION");

            }
            tryCounter--;
        }

        return null;
    }

    public static class getByUserSecurityTokenTask extends 
            AsyncTask<String, Void, SecurityTokenAndUserIDPair>{
        @Override
        protected SecurityTokenAndUserIDPair doInBackground(String... SecurityTokens) {
            return getCode("UserSecurityToken", SecurityTokens);
        }
    }

    public static class getByUserIDTask extends
            AsyncTask<String, Void, SecurityTokenAndUserIDPair>{
        @Override
        protected SecurityTokenAndUserIDPair doInBackground(String... SecurityTokens) {
            return getCode("UserID", SecurityTokens);
        }
    }
    
    public static class AddSecurityTokenAndUserIDPairTask extends AsyncTask<SecurityTokenAndUserIDPair, Void, Boolean>{
        @Override
        protected Boolean doInBackground(SecurityTokenAndUserIDPair... SecurityTokenAndUserIDPairs){
            setClient();

            if (SecurityTokenAndUserIDPairs.length < 1) {
                return FALSE;
            }

            SecurityTokenAndUserIDPair securityTokenAndUserIDPair = SecurityTokenAndUserIDPairs[0];
            Index index=new Index.Builder(securityTokenAndUserIDPair)
                    .index(getIndex())
                    .type("SecurityTokenAndUserIDPair")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        Log.d("AddSecurityToken", "Success, added " + securityTokenAndUserIDPair.getUserID());
                        return TRUE;
                    } else {
                        Log.d("AddSecurityToken", "Try:" + tryCounter +
                                ", Failed to add " + securityTokenAndUserIDPair.getUserID());
                    }

                } catch (IOException e) {
                    DeleteCode(securityTokenAndUserIDPair.getUserID());
                    Log.d("AddSecurityToken", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }
}
