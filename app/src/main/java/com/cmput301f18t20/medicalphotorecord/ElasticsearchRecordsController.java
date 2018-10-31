package com.cmput301f18t20.medicalphotorecord;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchRecordsController {

    static JestDroidClient client = null;

    public static void setClient(){
        if(client==null){
            DroidClientConfig config= new DroidClientConfig
                    .Builder("http://cmput301.softwareprocess.es:8080/")
                    .build();

            JestClientFactory factory=new JestClientFactory();
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

            Search search = new Search.Builder(params[0])
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
                    .index("COMMENTRECORDS")
                    .type("Record")
                    .build();
            try {
                client.execute(new CreateIndex.Builder("articles").build());

                DocumentResult result = client.execute(index);
                if(result.isSucceeded()){
                }

            }catch(IOException e){
                //do something here
            }
            return null;
        }

    }
}
