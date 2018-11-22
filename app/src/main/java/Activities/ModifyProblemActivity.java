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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import Controllers.AddProblemController;
import Controllers.BrowseUserProblemsController;
import Controllers.ModifyProblemController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyProblemActivity extends AppCompatActivity {
        protected EditText problem_title_edit,
                problem_description_edit;
        protected TextView problem_date_edit,problem_header;
        protected Button problem_save;
        private String new_title,
                new_description,
                userId;
        protected Problem chosen_problem,new_problem;
        public ModifyProblemController controller = new ModifyProblemController();

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_modify_problem);

            //initialize text and buttons
            this.problem_title_edit = (EditText)findViewById(R.id.modify_problem_title);
            this.problem_date_edit = (TextView)findViewById(R.id.modify_problem_date);
            this.problem_description_edit = (EditText)findViewById(R.id.modify_problem_description);
            this.problem_header = (TextView)findViewById(R.id.modify_problem_welcome);
            this.problem_save = (Button)findViewById(R.id.modify_problem_save);

            //retrieve problem object and userID
            Intent intent = getIntent();
            this.chosen_problem = (Problem) intent.getSerializableExtra("CHOSENPROBLEM");
            this.userId = intent.getStringExtra("USERIDEXTRA");

            //set text
            this.problem_title_edit.setText(this.chosen_problem.getTitle());
            this.problem_date_edit.setText(this.chosen_problem.getDate().toString());
            this.problem_description_edit.setText(this.chosen_problem.getDescription());

        }

        public void onClickSave (View v){
            //Get updated info
            this.new_title = this.problem_title_edit.getText().toString();
            this.new_description = this.problem_description_edit.getText().toString();

            //Create new Problem object with updated info
            try {
                this.new_problem = controller.createNewProblem(this.userId, this.new_title,this.chosen_problem.getDate(), this.new_description);
            } catch(UserIDMustBeAtLeastEightCharactersException e1){
                Toast.makeText(this,"UserID Must Be At Least 8 Characters", Toast.LENGTH_LONG).show();
            } catch(TitleTooLongException e2){
                Toast.makeText(this,"Title is Too Long, Please Re-enter a Shorter Title"
                        ,Toast.LENGTH_LONG)
                        .show();
            }


            //Delete old Problem object and save new one
            //controller.saveProblem(this,this.new_problem,this.chosen_problem,this.userId);
            //Delete old problem entry
            new AddProblemController().saveProblem("delete",ModifyProblemActivity.this,chosen_problem);
            //Add new problem entry to user's problem list
            new AddProblemController().saveProblem("add",ModifyProblemActivity.this,new_problem);
            Toast.makeText(this,"Problem info has Been saved!",Toast.LENGTH_LONG).show();

        }

}
