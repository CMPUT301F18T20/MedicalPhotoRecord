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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;

import org.w3c.dom.Text;

import java.util.Date;

import Controllers.AddProblemController;
import Controllers.ModifyUserController;

public class AddProblemActivity extends AppCompatActivity {

    private EditText problem_title_edit;
    private EditText problem_description_edit;
    private TextView problem_date_text;
    private Date problem_date = new Date();
    private String problem_title;
    private String problem_description;
    private String userId;
    private Button save_problem_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        // Get problem's views
        this.problem_title_edit = (EditText)findViewById(R.id.problem_title_id);
        this.problem_description_edit = (EditText)findViewById(R.id.problem_des_id);
        this.problem_date_text = (TextView)findViewById(R.id.problem_date_id);
        this.save_problem_button = (Button)findViewById(R.id.save_button_id);
        this.problem_date_text.setText(problem_date.toString());
    }

    public void addProblemButton(View view){

        // Get problem's infos
        this.problem_title = this.problem_title_edit.getText().toString();
        this.problem_description = this.problem_description_edit.getText().toString();
        this.userId = "patientidhastobelong"; // TODO

        // Add
        new AddProblemController().saveProblem("add", AddProblemActivity.this, -1, this.userId, this.problem_title, this.problem_date, this.problem_description);
    }
}
