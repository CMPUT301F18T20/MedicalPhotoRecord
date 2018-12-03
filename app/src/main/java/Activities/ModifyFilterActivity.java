/*
 * Class name: ModifyFilterActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 9:26 AM
 *
 * Last Modified: 03/12/18 9:26 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.R;

import static GlobalSettings.GlobalSettings.FILTEREXTRA;
import static java.lang.Boolean.FALSE;

public class ModifyFilterActivity extends AppCompatActivity {
    protected CheckBox IncludeLocation, IncludeBodyLocation, SearchForProblems, SearchForRecords,
                        SearchForPatientRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_filter);

        //setup checkboxes
        IncludeLocation = findViewById(R.id.IncludeLocation);
        IncludeBodyLocation = findViewById(R.id.IncludeBodyLocation);
        SearchForProblems = findViewById(R.id.SearchForProblems);
        SearchForRecords = findViewById(R.id.SearchForRecords);
        SearchForPatientRecord = findViewById(R.id.SearchForPatientRecord);

        //get filter from intent
        Intent intent = getIntent();
        Filter filter = (Filter) intent.getSerializableExtra(FILTEREXTRA);
        
        //use it to set initial values
        IncludeLocation.setChecked(filter.GeoIncluded());
        IncludeBodyLocation.setChecked(filter.BodyLocationIncluded());
        SearchForProblems.setChecked(filter.SearchForProblems());
        SearchForRecords.setChecked(filter.SearchForRecords());
        SearchForPatientRecord.setChecked(filter.SearchForPatientRecords());
        
    }

    public void onCheckboxClick(View v) {

        CheckBox checkBox = (CheckBox) v;

        if (checkBox == IncludeLocation || checkBox == IncludeBodyLocation) {
            //can't search these by location
            SearchForProblems.setChecked(FALSE);
            SearchForRecords.setChecked(FALSE);
        }

        if (checkBox == SearchForProblems || checkBox == SearchForRecords ) {
            //can't use location or body location search for these
            IncludeLocation.setChecked(FALSE);
            IncludeBodyLocation.setChecked(FALSE);
        }

    }

}
