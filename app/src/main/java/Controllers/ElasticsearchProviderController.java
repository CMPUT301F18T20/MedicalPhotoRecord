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
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchProviderController {

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

    public static class GetProviderTask extends AsyncTask<String, Void, ArrayList<Provider>>{
        @Override
        //String instead of void to implement search
        protected ArrayList<Provider> doInBackground(String... params) {
            setClient();
            ArrayList<Provider> Providers =new ArrayList<Provider>();
            String query = "{\n" +
                    //"    \"id\": \"myTemplateId\"," +
                    "    \"params\": {\n" +
                    "        \"query_string\" : \"test\"" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder("")//query)
                    .addIndex("cmput301f18t20")
                    .addType("Provider")
                    .build();
            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Provider> ProviderList;
                    ProviderList=result.getSourceAsObjectList(Provider.class);
                    Providers.addAll(ProviderList);
                }

            }catch(IOException e){}

            return Providers;
        }
    }

    public static class AddProviderTask extends AsyncTask<Provider, Void, Void>{
        @Override
        protected Void doInBackground(Provider... params){
            setClient();

            Provider Provider = params[0];
            Index index=new Index.Builder(Provider)
                    .index("cmput301f18t20")
                    .type("Provider")
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
