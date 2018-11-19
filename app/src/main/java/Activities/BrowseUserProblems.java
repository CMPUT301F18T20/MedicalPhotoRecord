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
import android.util.Log;
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
import com.cmput301f18t20.medicalphotorecord.User;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import Controllers.AddProblemController;
import Controllers.BrowseUserController;
import Controllers.BrowseUserProblemsController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class BrowseUserProblems extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView browse_user_problem_list_view;
    private Button add_problem_button;
    private ArrayList<Problem> problems;
    private BrowseUserProblemsController browseUserProblemsController = new BrowseUserProblemsController();
    private String userId;
    private ArrayAdapter<Problem> adapter;

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.problems = browseUserProblemsController.getProblemList(BrowseUserProblems.this, this.userId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user_problems);

        this.browse_user_problem_list_view = (ListView)findViewById(R.id.browse_user_problems_id);
        this.add_problem_button = (Button)findViewById(R.id.add_problem_button_id);
        this.browse_user_problem_list_view.setOnItemClickListener(this);
        registerForContextMenu(this.browse_user_problem_list_view);

    }

    @Override
    protected void onResume(){
        super.onResume();

        this.adapter = new ArrayAdapter<Problem>(this, R.layout.item_list,this.problems);
        this.browse_user_problem_list_view.setAdapter(adapter);

    }

    // Long click context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.browse_user_problems_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_user_problem_menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;
        switch(item.getItemId()){
            case R.id.modify_problem_id:
                return true;

            case R.id.delete_problem_id:
                Problem problem = adapter.getItem(longClickPos);
                new AddProblemController().saveProblem("delete",BrowseUserProblems.this, problem);
                Toast.makeText(BrowseUserProblems.this, "Your problem " + problem.getTitle() + " have been deleted",Toast.LENGTH_LONG).show();
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id){

        // Get position of clicked item and pass it on to Item Activity for later processing
        Intent intent = new Intent(BrowseUserProblems.this,ViewProblemActivity.class);
        intent.putExtra("position", position);
        intent.putExtra(USERIDEXTRA,this.userId);
        startActivity(intent);
    }

    public void addProblem(View view){
        Intent intent = new Intent(BrowseUserProblems.this,AddProblemActivity.class);
        intent.putExtra(USERIDEXTRA, this.userId);
        startActivity(intent);
    }

}
