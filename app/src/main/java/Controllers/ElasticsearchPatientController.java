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

import static GlobalSettings.GlobalSettings.getIndex;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchPatientController {

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

    public static class GetPatientTask extends AsyncTask<String, Void, ArrayList<Patient>>{
        @Override
        protected ArrayList<Patient> doInBackground(String... UserIDs) {
            setClient();
            ArrayList<Patient> Patients = new ArrayList<>();
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
                    return Patients;
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

            Log.d("PatientQuery", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Patient")
                    .build();

            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<Patient> PatientList;
                    PatientList=result.getSourceAsObjectList(Patient.class);
                    Patients.addAll(PatientList);
                }

                for (Patient patient : Patients) {
                    Log.d("GetPatient", "Fetched PatientID: " + patient.getUserID());
                }

            } catch(IOException e){
                Log.d("GetPatient", "IOEXCEPTION");

            }

            return Patients;
        }
    }

    public static class AddPatientTask extends AsyncTask<Patient, Void, Void>{
        @Override
        protected Void doInBackground(Patient... UserIDs){
            setClient();

            Patient patient = UserIDs[0];
            Index index=new Index.Builder(patient)
                    .index(getIndex())
                    .type("Patient")
                    .build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {

                    //add id to current object
                    patient.setElasticSearchID(result.getId());
                    Log.d("AddPatient", "Success, added " + patient.getUserID());
                } else {
                    Log.d("AddPatient", "Failed to add " + patient.getUserID());
                }

            } catch(IOException e){
                Log.d("AddPatient", "IOEXCEPTION");
            }
            return null;
        }
    }


    public static class SaveModifiedPatient extends AsyncTask<Patient, Void, Void> {
        @Override
        protected Void doInBackground(Patient... UserID) {
            setClient();
            Patient patient = UserID[0];
            try {
                JestResult result = client.execute(
                        new Index.Builder(patient)
                                .index(getIndex())
                                .type("Patient")
                                .id(patient.getElasticSearchID())
                                .build()
                );

                if (result.isSucceeded()) {
                    Log.d("ModifyPatient", "Success, modified " + patient.getUserID());
                } else {
                    Log.d("ModifyPatient", "Failed to modify " + patient.getUserID());
                }
            } catch (IOException e) {
                Log.d("ModifyPatient", "IOEXCEPTION");
            }
            return null;
        }
    }
}
