package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
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
public class ElasticsearchPatientController {

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

    public static class GetPatientTask extends AsyncTask<String, Void, ArrayList<Patient>>{
        @Override
        //String instead of void to implement search
        protected ArrayList<Patient> doInBackground(String... params) {
            setClient();
            ArrayList<Patient> Patients =new ArrayList<Patient>();
            String query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"UserID\" : \"" + params[0] + "\" }" +
                    "    }\n" +
                    "}";

            String query1 =
                    "{\n" +
                        "    \"query\": {\n" +
                        "        \"match_all\" : {}" +
                        "    }\n" +
                        "}";

            Log.d("Hello", query);
            Log.d("Hello", query1);

            Search search = new Search.Builder(query1)
                    .addIndex("cmput301f18t20")
                    .addType("Patient")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Patient> PatientList;
                    PatientList=result.getSourceAsObjectList(Patient.class);
                    Patients.addAll(PatientList);

                    Log.d("Hi", Patients.toString());
                }

                for (Patient patient : Patients) {
                    Log.d("Hello", patient.getUserID());
                }

            }catch(IOException e){}

            return Patients;
        }
    }

    public static class AddPatientTask extends AsyncTask<Patient, Void, Void>{
        @Override
        protected Void doInBackground(Patient... params){
            setClient();

            Patient Patient = params[0];
            Index index=new Index.Builder(Patient)
                    .index("cmput301f18t20")
                    .type("Patient")
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
