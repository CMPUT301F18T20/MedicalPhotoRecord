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
public class ElasticsearchSecurityTokenAndUserIDPair extends ElasticsearchController {
/*
    , 
    getUserSecurityTokenByUserID, 
    addSecurityTokenIDPair and 
    deleteSecurityTokenIDPairsByID
    */
    private static Boolean DeleteCode(String... UserIDs) {
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
                return FALSE;
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

        Log.d("DeleteProviderQuery", query);

        DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("Provider")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }

            } catch (IOException e) {
                Log.d("DeleteProviderQuery", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    public static class DeleteProvidersTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... UserIDs) {
            setClient();
            return DeleteCode(UserIDs);
        }
    }

    public static class getByUserSecurityTokenTask extends 
            AsyncTask<String, Void, SecurityTokenAndUserIDPair>{
        
        @Override
        protected SecurityTokenAndUserIDPair doInBackground(String... SecurityTokens) {
            
            setClient();
            SecurityTokenAndUserIDPair returnSecurityToken = null;
            String query;
            
            //if the SecurityTokens are not at least 1 entry just return null
            if (SecurityTokens.length >= 1) {
                return null;
            }

            //do a query for the first provided entry
            String securityToken = SecurityTokens[0];

            //query for the user token
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"UserSecurityToken\" : \"" + securityToken + "\" }" +
                    "    }\n" +
                    "}";

            Log.d("GetbySecurityToken", query);

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
                            Log.d("GetbySecurityToken", "Fetched SecurityTokenAndUserIDPairID: " +
                                    returnSecurityToken.toString());
                        }

                        return returnSecurityToken;
                    }

                } catch (IOException e) {
                    Log.d("GetbySecurityToken", "Try:" + tryCounter + ", IOEXCEPTION");

                }
                tryCounter--;
            }

            return null;
        }
    }

    //public static class getUserSecurityTokenByUserID

    public static class AddProviderTask extends AsyncTask<Provider, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Provider... Providers){
            setClient();

            if (Providers.length < 1) {
                return FALSE;
            }

            Provider provider = Providers[0];
            Index index=new Index.Builder(provider)
                    .index(getIndex())
                    .type("Provider")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        provider.setElasticSearchID(result.getId());
                        Log.d("AddProvider", "Success, added " + provider.getUserID());
                        return TRUE;
                    } else {
                        Log.d("AddProvider", "Try:" + tryCounter +
                                ", Failed to add " + provider.getUserID());
                    }

                } catch (IOException e) {
                    DeleteCode(provider.getUserID());
                    Log.d("AddProvider", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class SaveModifiedProvider extends AsyncTask<Provider, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Provider... providers) {
            setClient();

            //can't be empty
            if (providers.length < 1) {
                return FALSE;
            }

            Provider provider = providers[0];

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(
                            new Index.Builder(provider)
                                    .index(getIndex())
                                    .type("Provider")
                                    .id(provider.getElasticSearchID())
                                    .build()
                    );

                    if (result.isSucceeded()) {
                        Log.d("ModifyProvider", "Success, modified " + provider.getUserID());
                        return TRUE;
                    } else {
                        Log.d("ModifyProvider", "Try:" + tryCounter +
                                ", Failed to modify " + provider.getUserID());
                    }
                } catch (IOException e) {
                    Log.d("ModifyProvider", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }
}
