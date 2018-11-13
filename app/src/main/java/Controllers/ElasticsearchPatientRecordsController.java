package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
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
public class ElasticsearchPatientRecordsController {

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

    public static class GetPatientRecordsTask extends AsyncTask<String, Void, ArrayList<PatientRecord>>{
        @Override
        //String instead of void to implement search
        protected ArrayList<PatientRecord> doInBackground(String... params) {
            setClient();
            ArrayList<PatientRecord> PatientRecords=new ArrayList<PatientRecord>();
            String query = "{\n" +
                    //"    \"id\": \"myTemplateId\"," +
                    "    \"params\": {\n" +
                    "        \"query_string\" : \"test\"" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder("")//query)
                    .addIndex("cmput301f18t20")
                    .addType("PatientRecord")
                    .build();
            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<PatientRecord> PatientRecordList;
                    PatientRecordList=result.getSourceAsObjectList(PatientRecord.class);
                    PatientRecords.addAll(PatientRecordList);
                }

            }catch(IOException e){}

            return PatientRecords;
        }
    }

    public static class AddPatientRecordTask extends AsyncTask<PatientRecord, Void, Void>{
        @Override
        protected Void doInBackground(PatientRecord... params){
            setClient();

            PatientRecord PatientRecord = params[0];
            Index index=new Index.Builder(PatientRecord)
                    .index("cmput301f18t20")
                    .type("PatientRecord")
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
