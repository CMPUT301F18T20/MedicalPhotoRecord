/*
 * Class name: AddProblemActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 6:19 PM
 *
 * Last Modified: 12/11/18 6:18 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.Date;

import Controllers.AddDeleteProblemController;
import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * AddProblemActivity
 * Allow patient to add a new problem by inputing title and description
 * @version 1.0
 * @see Problem
 * @see com.cmput301f18t20.medicalphotorecord.Patient
 */
public class AddProblemActivity extends AppCompatActivity {

    private EditText problem_title_edit;
    private EditText problem_description_edit;
    private TextView problem_date_text;
    private Button save_problem_button;
    private Date problem_date = new Date();
    private String problem_title;
    private String problem_description;
    private String userId;


    /**
     * Get all necessary views and button
     * Get from intent userId
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        // Get problem's views
        this.problem_title_edit = (EditText) findViewById(R.id.problem_title_id);
        this.problem_description_edit = (EditText) findViewById(R.id.problem_des_id);
        this.problem_date_text = (TextView) findViewById(R.id.problem_date_id);
        this.save_problem_button = (Button) findViewById(R.id.save_button_id);
        this.problem_date_text.setText(problem_date.toString());

        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
    }

    /**
     * Runs when Add button is clicked
     * Create a new problem associated with patient user id and save it to online and offline database
     * @param view
     */
    public void addProblemButton(View view) {

        // Get problem's infos
        this.problem_title = this.problem_title_edit.getText().toString();
        this.problem_description = this.problem_description_edit.getText().toString();

        // Create new problem with controller, toast with activity
        Problem addedProblem = null;
        try {
            addedProblem = new AddDeleteProblemController().createProblem(AddProblemActivity.this, this.userId, this.problem_title, this.problem_date, this.problem_description);

            // Add problem with controller, toast with activity
            new AddDeleteProblemController().saveAddProblem(AddProblemActivity.this, addedProblem);
            Toast.makeText(AddProblemActivity.this, "Your problem have been added", Toast.LENGTH_LONG).show();

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(AddProblemActivity.this, "Your userId has to contains more than 8 characters", Toast.LENGTH_LONG).show();
        } catch (TitleTooLongException e) {
            Toast.makeText(AddProblemActivity.this, "Your title is too long", Toast.LENGTH_LONG).show();
        } catch (ProblemDescriptionTooLongException e) {
            Toast.makeText(AddProblemActivity.this, "Your description is too long", Toast.LENGTH_LONG).show();
        }
    }
}
