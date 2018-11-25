/*
 * Class name: BrowseProblemsActivity
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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.AddDeleteProblemController;
import Controllers.BrowseProblemsController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class BrowseProblemsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView browse_user_problem_list_view;
    private Button add_problem_button;
    private ArrayList<Problem> problems;
    private ArrayAdapter<Problem> adapter;
    private String userId;
    private String problemUUID;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        // Get user id and corresponding problem list
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.problems = new BrowseProblemsController().getProblemList(BrowseProblemsActivity.this, this.userId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user_problems);

        // Set all views, button, context menu
        this.browse_user_problem_list_view = findViewById(R.id.browse_user_problems_id);
        this.add_problem_button = findViewById(R.id.add_problem_button_id);
        this.browse_user_problem_list_view.setOnItemClickListener(this);
        registerForContextMenu(this.browse_user_problem_list_view);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Adapter for problem list view
        this.adapter = new ArrayAdapter<>(this, R.layout.item_list, this.problems);
        this.browse_user_problem_list_view.setAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Long click context menu
        if (v.getId() == R.id.browse_user_problems_id) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_user_problem_menu_list, menu);
        }
    }

    // MODIFY PROBLEM; DELETE PROBLEM
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Long click context menu option
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;

        switch (item.getItemId()) {

            case R.id.modify_problem_id:

                // Get long clicked problem
                Intent intent = new Intent(BrowseProblemsActivity.this, ModifyProblemActivity.class);
                Problem modifiedProblem = adapter.getItem(longClickPos);

                // Pass problemUUID and userID to ModifyProblemActivity
                intent.putExtra(PROBLEMIDEXTRA, modifiedProblem);
                intent.putExtra(USERIDEXTRA, this.userId);
                startActivity(intent);
                return true;

            case R.id.delete_problem_id:

                // Get long clicked problem
                Problem deletedProblem = adapter.getItem(longClickPos);
                new AddDeleteProblemController().saveDeleteProblem(BrowseProblemsActivity.this, deletedProblem);
                Toast.makeText(BrowseProblemsActivity.this, "Your problem " + deletedProblem.getTitle() + " have been deleted", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    // VIEW PROBLEM
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        // Get clicked problem
        Intent intent = new Intent(BrowseProblemsActivity.this, ViewProblemActivity.class);
        Problem chosenProblem = (Problem) l.getItemAtPosition(position);
        this.problemUUID = chosenProblem.getUUID();

        // Pass problemUUID and userId to ViewProblemActivity
        // intent.putExtra("CHOSENPROBLEM", chosenProblem);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra(USERIDEXTRA, this.userId);
        startActivity(intent);
    }


    // ADD PROBLEM
    public void addProblem(View view) {
        Intent intent = new Intent(BrowseProblemsActivity.this, AddProblemActivity.class);
        intent.putExtra(USERIDEXTRA, this.userId);
        startActivity(intent);
    }
}
