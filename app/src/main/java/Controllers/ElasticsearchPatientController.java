package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.User;
import com.google.common.reflect.Parameter;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public static Boolean DeleteCode(String... UserIDs) {
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

        Log.d("DeletePatientQuery", query);

        DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("Patient")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }

            } catch (IOException e) {
                Log.d("DeletePatientQuery", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    public static class DeletePatientsTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... UserIDs) {
            setClient();
            return DeleteCode(UserIDs);
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

            Log.d("GetPatientQuery", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Patient")
                    .setParameter(SIZE,"10000")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Patient> PatientList;
                        PatientList = result.getSourceAsObjectList(Patient.class);
                        Patients.addAll(PatientList);
                        for (Patient patient : Patients) {
                            Log.d("GetPatient", "Fetched PatientID: " + patient.getUserID());
                        }
                        return Patients;
                    }

                } catch (IOException e) {
                    Log.d("GetPatient", "Try:" + tryCounter + ", IOEXCEPTION");

                }
                tryCounter--;
            }

            return Patients;
        }
    }

    public static class AddPatientTask extends AsyncTask<Patient, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Patient... Patients){
            setClient();

            if (Patients.length < 1) {
                return FALSE;
            }

            Patient patient = Patients[0];
            Index index=new Index.Builder(patient)
                    .index(getIndex())
                    .type("Patient")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        patient.setElasticSearchID(result.getId());
                        Log.d("AddPatient", "Success, added " + patient.getUserID());
                        return TRUE;
                    } else {
                        Log.d("AddPatient", "Try:" + tryCounter +
                                ", Failed to add " + patient.getUserID());
                    }

                } catch (IOException e) {
                    Log.d("AddPatient", "Try:" + tryCounter + ", IOEXCEPTION");
                    DeleteCode(patient.getUserID());
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class GetPatientsAssociatedWithProviderUserIDTask extends AsyncTask<String, Void, ArrayList<Patient>> {
        @Override
        protected ArrayList<Patient> doInBackground(String... ProviderUserID) {
            setClient();
            ArrayList<Patient> patients = new ArrayList<>();
            String query;

            if (ProviderUserID.length < 1) {
                return null;
            }

            //query for all patients assigned to provider user id
            query =
                    "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : { \"associatedProviderIDs\" : \"" + ProviderUserID[0] + "\" }" +
                    "    }\n" +
                    "}";
            Log.d("GetPatientByProviderID", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Patient")
                    .setParameter(SIZE,"10000")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Patient> PatientList;
                        PatientList = result.getSourceAsObjectList(Patient.class);
                        patients.addAll(PatientList);
                        for (Patient patient : patients) {
                            Log.d("GetPatientByProviderID", "Fetched PatientID: " + patient.getUserID());
                        }
                        return patients;
                    }

                } catch (IOException e) {
                    Log.d("GetPatientByProviderID", "IOEXCEPTION");

                }
                tryCounter--;
            }

            return patients;
        }
    }

    public static class SaveModifiedPatient extends AsyncTask<Patient, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Patient... patients) {
            setClient();

            //can't be empty
            if (patients.length < 1) {
                return FALSE;
            }

            Patient patient = patients[0];

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
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
                        return TRUE;
                    } else {
                        Log.d("ModifyPatient", "Try:" + tryCounter +
                                ", Failed to modify " + patient.getUserID());
                    }
                } catch (IOException e) {
                    Log.d("ModifyPatient", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }
}
