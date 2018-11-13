/*
 * Class name: BrowseUserProblems
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 2:28 PM
 *
 * Last Modified: 12/11/18 2:26 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import Controllers.BrowseUserController;
import Controllers.BrowseUserProblemsController;

public class BrowseUserProblems extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView browse_user_problem_list_view;
    private Button add_problem_button;
    private ArrayList<Problem> problems;
    private BrowseUserProblemsController browseUserProblemsController = new BrowseUserProblemsController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user_problems);

        this.browse_user_problem_list_view = (ListView)findViewById(R.id.browse_user_problems_id);
        this.add_problem_button = (Button)findViewById(R.id.add_problem_button_id);
        this.browse_user_problem_list_view.setOnItemClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        /*try {
            this.users = new ElasticsearchPatientController.GetPatientTask().execute().get();
        } catch (Exception e) {
            //TODO handle exceptions
        }*/

        // Display list of users/ patients
        this.problems = this.browseUserProblemsController.getProblemList(BrowseUserProblems.this);
        ArrayAdapter<Problem> adapter = new ArrayAdapter<Problem>(this, R.layout.item_list,problems);
        this.browse_user_problem_list_view.setAdapter(adapter);

    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id){

        // Get position of clicked item and pass it on to Item Activity for later processing
        Intent intent = new Intent(BrowseUserProblems.this,ViewProblemActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void addProblem(View view){
        Intent intent = new Intent(BrowseUserProblems.this,AddProblemActivity.class);
        startActivity(intent);
    }
}
