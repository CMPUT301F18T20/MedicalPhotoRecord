/*
 * Class name: ViewCommentRecordActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 29/11/18 2:09 PM
 *
 * Last Modified: 28/11/18 7:44 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchRecordController;

import static android.widget.Toast.LENGTH_LONG;

/**
 * ViewCommentRecordActivity
 *  Displays the information of a selected CommentRecord created by a Provider
 *          - creator UserID, comment, title, date created.
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

public class ViewCommentRecordActivity extends AppCompatActivity {

    protected TextView titleTextView,
    commentTextView, dateTextView,
    createdByTextView;

    protected Date date;
    protected String title,
    comment, createdBy,
    recordUUID;

    protected Record currentRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment_record);

        Intent intent = getIntent();
        recordUUID = intent.getStringExtra("RECORDID");

        try{
            this.currentRecord = new ElasticsearchRecordController.GetRecordByRecordUUIDTask().execute(recordUUID).get();
        }catch (InterruptedException e){
            Toast.makeText(this, "Interrupted while fetching current record", LENGTH_LONG).show();
            finish();
        }catch (ExecutionException e){
            Toast.makeText(this, "Execution exception while fetching current record", LENGTH_LONG).show();
            finish();
        }

        title = this.currentRecord.getTitle();
        comment = this.currentRecord.getComment();
        createdBy = this.currentRecord.getCreatedByUserID();
        date = this.currentRecord.getDate();

        titleTextView = findViewById(R.id.view_comment_record_title_id);
        commentTextView = findViewById(R.id.view_comment_record_comment_id);
        dateTextView = findViewById(R.id.view_comment_record_date_id);
        createdByTextView = findViewById(R.id.view_comment_record_createdBy_id);

        titleTextView.setText(title);
        commentTextView.setText(comment);
        dateTextView.setText(date.toString());
        createdBy = "Created By: " + createdBy;
        createdByTextView.setText(createdBy);

    }
}
