/*
 * Class name: ModifyProblemActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 19/11/18 12:56 AM
 *
 * Last Modified: 19/11/18 12:56 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchProblemController;
import Controllers.ModifyProblemController;
import Exceptions.NoSuchProblemException;
import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ModifyProblemActivity extends AppCompatActivity {

    protected EditText problem_title_edit, problem_description_edit;
    protected TextView problem_date_edit, problem_header;
    protected Button problem_save;
    private String new_title, new_description;
    protected Problem chosen_problem;
    private String problemUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_problem);

        //initialize text and buttons
        this.problem_title_edit = (EditText) findViewById(R.id.modify_problem_title);
        this.problem_date_edit = (TextView) findViewById(R.id.modify_problem_date);
        this.problem_description_edit = (EditText) findViewById(R.id.modify_problem_description);
        this.problem_header = (TextView) findViewById(R.id.modify_problem_welcome);
        this.problem_save = (Button) findViewById(R.id.modify_problem_save);

        //retrieve problem uuid and userID
        Intent intent = getIntent();

        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        try {

            this.chosen_problem = new ModifyProblemController().getProblem(this,this.problemUUID);
        } catch (NoSuchProblemException e) {
            Toast.makeText(this, "Problem does not exist", Toast.LENGTH_LONG).show();
            finish();
        }

        //set text
        this.problem_title_edit.setText(this.chosen_problem.getTitle());
        this.problem_date_edit.setText(this.chosen_problem.getDate().toString());
        this.problem_description_edit.setText(this.chosen_problem.getDescription());

    }

    public void onClickSave(View v) {

        //Get updated info
        this.new_title = this.problem_title_edit.getText().toString();
        this.new_description = this.problem_description_edit.getText().toString();


        //Save modified problem, toast
        try {
            new ModifyProblemController().saveModifyProblem(this, this.chosen_problem, this.new_title, this.new_description);
            Toast.makeText(this, "Problem info has Been saved!", Toast.LENGTH_LONG).show();
        } catch (TitleTooLongException e) {
            Toast.makeText(this, "Title is too long", Toast.LENGTH_LONG).show();
        } catch (ProblemDescriptionTooLongException e) {
            Toast.makeText(this, "Description is too long", Toast.LENGTH_LONG).show();
        }
    }
}
