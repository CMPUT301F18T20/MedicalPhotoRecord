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

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.SearchableObject;

import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProblemController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static Controllers.ElasticsearchProblemController.QueryByUserIDWithKeywords;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SearchActivity extends AppCompatActivity {

    protected ListView QueryResults;
    protected EditText SearchKeywords;
    protected ArrayAdapter<SearchableObject> QueryAdapter;
    protected ArrayList<SearchableObject> objects = new ArrayList<>();
    protected String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //setup list view and keywords
        QueryResults = findViewById(R.id.QueryResults);
        SearchKeywords = findViewById(R.id.SearchKeywords);

        //fetch user ID from intent
        Intent intent = getIntent();
        userID = intent.getStringExtra(USERIDEXTRA);

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

    public void OnSearchClick(View v) {


        String keywordsString = SearchKeywords.getText().toString();
        String[] keywords = extractKeywords(keywordsString);

        try {
            ArrayList<Problem> problems;
            //fetch, either by keyword or just normal
            if (keywordsString.length() == 0) {
                problems = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                        .execute(userID).get();
            } else {
                problems = QueryByUserIDWithKeywords(userID, keywords);
            }

            if (problems != null) {
                if (problems.size() > 0) {
                    objects.addAll(problems);
                    QueryAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "added objects", LENGTH_LONG).show();
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
}
