/*
 * Class name: ViewProblemActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 3:08 PM
 *
 * Last Modified: 12/11/18 3:06 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Controllers.OfflineLoadController;

public class ViewProblemActivity extends AppCompatActivity {

    private TextView view_problem_title_text;
    private int position;
    private ArrayList<Problem> problems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problem);

        this.view_problem_title_text = (TextView)findViewById(R.id.view_problem_title_id);

        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",0);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
