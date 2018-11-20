package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

public class ViewRecordActivity extends AppCompatActivity {
    protected TextView title,date,description;
    protected ImageButton body_location,photo;
    protected Button geolocation;

    private Record currentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        //initialize text and buttons

        this.title = (TextView)findViewById(R.id.view_record_title);
        this.date = (TextView)findViewById(R.id.view_record_date);
        this.description = (TextView)findViewById(R.id.view_record_description);
        this.body_location = (ImageButton)findViewById(R.id.view_record_body_location);
        this.photo = (ImageButton)findViewById(R.id.view_record_photo);
        this.geolocation = (Button)findViewById(R.id.view_record_geo);

        //Get Record object through intent
        Intent intent = getIntent();
        this.currentRecord = (Record)intent.getSerializableExtra("CHOSENRECORD");

        //Set text
        String tempString = "Record: "+ this.currentRecord.getTitle();
        this.title.setText(tempString);

        tempString = "Date: " + this.currentRecord.getDate();
        this.date.setText(tempString);

        tempString = "Description: "+ this.currentRecord.getDescription();
        this.description.setText(tempString);

        //TODO Add setting of body_location photo + photo + geolocation


    }
}
