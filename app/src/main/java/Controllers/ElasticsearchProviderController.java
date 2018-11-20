package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Provider;
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

import static GlobalSettings.GlobalSettings.getIndex;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchProviderController {

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

    public static class DeleteProvidersTask extends AsyncTask<String, Void, Boolean>{
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
                        "        \"match\" : { \"UserID\" : \"" + CombinedUserIDs + "\" }" +
                        "    }\n" +
                        "}";

            } else {
                query = matchAllquery;
            }

            Log.d("ProviderQuery", query);


            DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                    .addIndex(getIndex())
                    .addType("Provider")
                    .build();

            try {
                JestResult result=client.execute(deleteByQueryTask);

                if(result.isSucceeded()){
                    return TRUE;
                }

                return FALSE;

            } catch(IOException e){
                Log.d("GetProvider", "IOEXCEPTION");
            }

            return FALSE;
        }
    }

    public static class GetProviderTask extends AsyncTask<String, Void, ArrayList<Provider>>{
        @Override
        protected ArrayList<Provider> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<Provider> Providers = new ArrayList<>();
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
                    return Providers;
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

            Log.d("ProviderQuery", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Provider")
                    .setParameter(Parameters.SIZE,"10000")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Provider> ProviderList;
                    ProviderList=result.getSourceAsObjectList(Provider.class);
                    Providers.addAll(ProviderList);
                }

                for (Provider provider : Providers) {
                    Log.d("GetProvider", "Fetched ProviderID: " + provider.getUserID());
                }

            } catch(IOException e){
                Log.d("GetProvider", "IOEXCEPTION");

            }

            return Providers;
        }
    }

    public static class AddProviderTask extends AsyncTask<Provider, Void, Void>{
        @Override
        protected Void doInBackground(Provider... UserIDs){
            setClient();

            Provider provider = UserIDs[0];
            Index index=new Index.Builder(provider)
                    .index(getIndex())
                    .type("Provider")
                    .build();

            try {
                DocumentResult result = client.execute(index);
                if(result.isSucceeded()){
                    Log.d("AddProvider", "Success, added " + provider.getUserID());
                } else {
                    Log.d("AddProvider", "Failed to add " + provider.getUserID());
                }

            }catch(IOException e){
                //do something here
                Log.d("AddProvider", "IOEXCEPTION");
            }
            return null;

        }
    }
}
