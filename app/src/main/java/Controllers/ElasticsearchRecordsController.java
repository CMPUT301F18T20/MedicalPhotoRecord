package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Record;
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
public class ElasticsearchRecordsController {

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

    public static class GetRecordsTask extends AsyncTask<String, Void, ArrayList<Record>>{
        @Override
        //String instead of void to implement search
        protected ArrayList<Record> doInBackground(String... params) {
            setClient();
            ArrayList<Record> Records=new ArrayList<Record>();
            String query = "{\n" +
                    //"    \"id\": \"myTemplateId\"," +
                    "    \"params\": {\n" +
                    "        \"query_string\" : \"test\"" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder("")//query)
                    .addIndex("cmput301f18t20")
                    .addType("Record")
                    .build();
            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Record> RecordList;
                    RecordList=result.getSourceAsObjectList(Record.class);
                    Records.addAll(RecordList);
                }

            }catch(IOException e){}

            return Records;
        }
    }

    public static class AddRecordTask extends AsyncTask<Record, Void, Void>{
        @Override
        protected Void doInBackground(Record... params){
            setClient();

            Record Record = params[0];
            Index index=new Index.Builder(Record)
                    .index("cmput301f18t20")
                    .type("Record")
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
