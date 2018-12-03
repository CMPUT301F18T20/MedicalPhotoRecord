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
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.cmput301f18t20.medicalphotorecord.SearchableObject;

import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProblemController;
import Controllers.ElasticsearchRecordController;
import Enums.USER_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static Controllers.ElasticsearchProblemController.QueryByUserIDWithKeywords;
import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static GlobalSettings.GlobalSettings.USERTYPEEXTRA;
import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SearchActivity extends AppCompatActivity {

    protected ListView QueryResults;
    protected EditText SearchKeywords;
    protected ArrayAdapter<SearchableObject> QueryAdapter;
    protected ArrayList<SearchableObject> objects = new ArrayList<>();
    protected String userID;
    protected USER_TYPE user_type;
    protected Filter filter = new Filter();

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
        QueryAdapter = new ArrayAdapter<SearchableObject>(this, R.layout.item_list, objects);
        QueryResults.setAdapter(QueryAdapter);

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
            //TODO patient record search
            //SearchForPatientRecords(keywordsString, keywords);
        }

    }

    public void SearchForProblems(String keywordString, String[] keywords) {
        if (user_type == PATIENT) {
            try {
                ArrayList<Problem> problems;

                //fetch, either by keyword and UserID or just by UserID
                if (keywordString.length() == 0) {
                    problems = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                            .execute(userID).get();
                } else {
                    problems = new ElasticsearchProblemController.QueryByUserIDWithKeywords(userID)
                            .execute(keywords).get();
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

        //TODO PROVIDER, redefine GetProblemsCreatedByUserIDTask to take multiple userIDs,
        //TODO same with QueryByUserIDWithKeywords
    }

    public void SearchForRecords(String keywordString, String[] keywords) {
        if (user_type == PATIENT) {
            try {
                ArrayList<Record> records;

                //fetch, either by keyword and UserID or just by UserID
                if (keywordString.length() == 0) {
                    records = new ElasticsearchRecordController.GetRecordsByAssociatedPatientUserIDTask()
                            .execute(userID).get();
                } else {
                    records = new ElasticsearchRecordController
                            .QueryByAssociatedPatientUserIDWithKeywords(userID).execute(keywords).get();
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

        } else {
            try {
                ArrayList<Record> records;

                //fetch, either by keyword and UserID or just by UserID
                if (keywordString.length() == 0) {
                    records = new ElasticsearchRecordController.GetRecordsCreatedByUserIDTask()
                            .execute(userID).get();
                } else {
                    //TODO redefine QueryByAssociatedPatientUserIDWithKeywords to take multiple userIDs or just one
                    //records = new ElasticsearchRecordController
                            //.QueryByAssociatedPatientUserIDWithKeywords(userID).execute(keywords).get();
                    records = null;
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
    }
/*
    public void SearchForProblems(String keywordString, String[] keywords) {
        try {
            ArrayList<Problem> problems;

            //fetch, either by keyword and UserID or just by UserID
            if (keywordString.length() == 0) {
                problems = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                        .execute(userID).get();
            } else {
                problems = new ElasticsearchProblemController.QueryByUserIDWithKeywords(userID)
                        .execute(keywords).get();
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
    */
    public void setFilter(Filter filter) {
        //allows test to change filter status
        this.filter = filter;
    }
}
