/*
 * Class name: SearchActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 11:21 AM
 *
 * Last Modified: 02/12/18 11:21 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.cmput301f18t20.medicalphotorecord.SearchableObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchPatientRecordController;
import Controllers.ElasticsearchProblemController;
import Controllers.ElasticsearchRecordController;
import Enums.USER_TYPE;

import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static GlobalSettings.GlobalSettings.USERTYPEEXTRA;
import static android.widget.Toast.LENGTH_LONG;

public class SearchActivity extends AppCompatActivity {

    protected ListView QueryResults;
    protected EditText SearchKeywords;
    protected ArrayAdapter<SearchableObject> QueryAdapter;
    protected ArrayList<SearchableObject> objects = new ArrayList<>();
    protected String userID;
    protected String[] assignedPatientIDs;
    protected USER_TYPE user_type;
    protected Filter filter = new Filter();
    protected  boolean filterListShown;

    //for dropdown checkbox
    final String[] selectFilter = {"Select Filters", "Problem", "Record"
            , "PatientRecord", "BodyLocation"
            ,"GeoLocation"};
    protected Spinner filterDropDown;


    //TODO we need a way to change the filter settings!!! If Location or Body Location are specified, we will only be searching patient records so make sure to reflect that if the user selects "Location" or "BodyLocation", deselect "Record" and "Problem"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //setup list view and keywords
        QueryResults = findViewById(R.id.QueryResults);
        SearchKeywords = findViewById(R.id.SearchKeywords);

        //fetch user ID and user type from intent
        Intent intent = getIntent();
        userID = intent.getStringExtra(USERIDEXTRA);
        user_type = (USER_TYPE) intent.getSerializableExtra(USERTYPEEXTRA); //TODO if user type is PROVIDER, then fetch all their patients and get their UserIDs for using with search

        //adapt to objects class
        QueryAdapter = new ArrayAdapter<>(this, R.layout.item_list, objects);
        QueryResults.setAdapter(QueryAdapter);

        if (user_type == PROVIDER) {
            populateAssignedPatients();
        }

        //set Spinner checkbox
        this.filterDropDown = (Spinner)findViewById(R.id.FilterList);
        ArrayList<FilterCheckBoxState> stateList = new ArrayList<>();
        for (int i=0;i<selectFilter.length;i++){
            FilterCheckBoxState state = new FilterCheckBoxState();
            state.setTitle(selectFilter[i]);
            state.setSelected(false);
            stateList.add(state);
        }

        FilterArrayAdapter filterAdapter = new FilterArrayAdapter(this,0,stateList);
        filterDropDown.setAdapter(filterAdapter);
        this.filterListShown = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!this.filterListShown){
            this.filterDropDown.setVisibility(View.GONE);
        } else{
            this.filterDropDown.setVisibility(View.VISIBLE);
        }
    }

    public void populateAssignedPatients() {
        ArrayList<Patient> patients;
        try {
            patients = new ElasticsearchPatientController
                    .GetPatientsAssociatedWithProviderUserIDTask().execute(userID).get();

            //erase previous values
            assignedPatientIDs = new String[patients.size()];

            //add all patient userIDs to the list
            for (int i = 0; i < patients.size(); i++) {
                assignedPatientIDs[i] = patients.get(i).getUserID();
            }
        } catch (ExecutionException e) {
            Toast.makeText(this,
                    "Execution Exception while fetching assigned patients",
                    LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this,
                    "Interrupted Exception while fetching assigned patients",
                    LENGTH_LONG).show();
        }
    }

    private String[] extractKeywords(String text) {
        String[] keywords = null;
        if (text.length() > 0) {
            keywords = text.split("\\s+");
            for (String word : keywords) {
                Log.d("SearchActivity", "keyword: '" + word + "'");
            }
        }
        return keywords;
    }

    //would be best if each type of search were threaded, but then we would need a synchronized data structure
    public void OnSearchClick(View v) {

        //TODO body location query and location query, all searches need to be incorporated in case
        //TODO Location, keywords and body locations are specifid
        String keywordsString = SearchKeywords.getText().toString();
        String[] keywords = extractKeywords(keywordsString);

        // if filter says to search for problems, then that's what we'll do
        if (filter.SearchForProblems()) {
            SearchForProblems(keywordsString, keywords);
        }

        // if filter says to search for records, do that too
        if (filter.SearchForRecords()) {
            SearchForRecords(keywordsString, keywords);
        }

        // if filter says to search for patient records, include those too
        if (filter.SearchForPatientRecords()) {
            SearchForPatientRecords(keywordsString, keywords);
        }

    }

    public void SearchForProblems(String keywordString, String[] keywords) {
        
        try {
            ArrayList<Problem> problems;

            //fetch, either by keyword and UserID or just by UserID
            if (keywordString.length() == 0) {
                if (user_type == PATIENT) {
                    problems = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                            .execute(userID).get();
                } else {
                    problems = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                            .execute(assignedPatientIDs).get();
                }
            } else {
                if (user_type == PATIENT) {
                    problems = new ElasticsearchProblemController.QueryByUserIDWithKeywords(userID)
                            .execute(keywords).get();
                } else {
                    problems = new ElasticsearchProblemController.QueryByUserIDWithKeywords(assignedPatientIDs)
                            .execute(keywords).get();
                }
            }

            if (problems != null) {
                if (problems.size() > 0) {
                    objects.addAll(problems);
                    QueryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "no problems found", LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "problems was null", LENGTH_LONG).show();
            }

        } catch (ExecutionException e) {
            Toast.makeText(this, "Execution Exception While querying", LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interrupted Exception While querying", LENGTH_LONG).show();
        }
    }



    public void SearchForRecords(String keywordString, String[] keywords) {
        try {
            ArrayList<Record> records;

            //fetch, either by keyword and UserID or just by UserID
            if (keywordString.length() == 0) {
                if (user_type == PATIENT) {
                    records = new ElasticsearchRecordController.GetRecordsByAssociatedPatientUserIDTask()
                            .execute(userID).get();

                //provider
                } else {
                    records = new ElasticsearchRecordController.GetRecordsByAssociatedPatientUserIDTask()
                            .execute(assignedPatientIDs).get();
                }
            } else {
                if (user_type == PATIENT) {
                    records = new ElasticsearchRecordController
                            .QueryByAssociatedPatientUserIDWithKeywords(userID).execute(keywords)
                            .get();

                //provider
                } else {
                    records = new ElasticsearchRecordController
                            .QueryByAssociatedPatientUserIDWithKeywords(assignedPatientIDs)
                            .execute(keywords).get();
                }
            }

            if (records != null) {
                if (records.size() > 0) {
                    objects.addAll(records);
                    QueryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "no records found", LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "records was null", LENGTH_LONG).show();
            }

        } catch (ExecutionException e) {
            Toast.makeText(this, "Execution Exception While querying", LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interrupted Exception While querying", LENGTH_LONG).show();
        }
    }

    public void SearchForPatientRecords(String keywordString, String[] keywords) {
        try {
            ArrayList<PatientRecord> patientRecords;

            //fetch, either by keyword and UserID or just by UserID
            if (keywordString.length() == 0) {
                if (user_type == PATIENT) {
                    patientRecords = new ElasticsearchPatientRecordController
                            .GetPatientRecordsCreatedByUserIDTask().execute(userID).get();
                } else {
                    patientRecords = new ElasticsearchPatientRecordController
                            .GetPatientRecordsCreatedByUserIDTask().execute(assignedPatientIDs).get();
                }
            } else {
                if (user_type == PATIENT) {
                    patientRecords = new ElasticsearchPatientRecordController
                            .QueryByUserIDWithKeywords(userID)
                            .execute(keywords).get();
                } else {
                    patientRecords = new ElasticsearchPatientRecordController
                            .QueryByUserIDWithKeywords(assignedPatientIDs)
                            .execute(keywords).get();
                }
            }

            if (patientRecords != null) {
                if (patientRecords.size() > 0) {
                    objects.addAll(patientRecords);
                    QueryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "no patientRecords found", LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "patientRecords was null", LENGTH_LONG).show();
            }

        } catch (ExecutionException e) {
            Toast.makeText(this, "Execution Exception While querying", LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interrupted Exception While querying", LENGTH_LONG).show();
        }
    }

    public void querySettingsClick(View view){
        if (!this.filterListShown){
            this.filterListShown = true;
            this.filterDropDown.setVisibility(View.VISIBLE);
        } else{
            this.filterListShown = false;
            this.filterDropDown.setVisibility(View.GONE);
        }
    }

    public void setFilter(Filter filter) {
        //allows test to change filter status
        this.filter = filter;
    }
}
