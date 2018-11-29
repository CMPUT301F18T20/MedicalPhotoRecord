package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.cmput301f18t20.medicalphotorecord.ViewCommentRecordActivity;

import java.util.ArrayList;

import Controllers.ProviderRecordsController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * ProviderBrowseProblemRecords
 *  Displays a chosen problem's list of records to the provider
 *  this allows the provider the chance to add a comment record.
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

public class ProviderBrowseProblemRecords extends AppCompatActivity implements AdapterView.OnItemClickListener{

    protected String patientId;
    protected String problemUUID;
    protected ArrayList<Record> records;
    protected ListView problem_record_list_view;
    protected Button add_record_button;
    protected ArrayAdapter<Record> adapter;
    protected String providerID;
    protected ProviderRecordsController providerRecordsController = new ProviderRecordsController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_browse_problem_records);

        Intent intent = getIntent();

        this.providerID = intent.getStringExtra(PROVIDERID);
        this.patientId = intent.getStringExtra(USERIDEXTRA);
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        this.records = providerRecordsController.getRecords(this,this.problemUUID,this.patientId);

        this.problem_record_list_view = findViewById(R.id.provider_browse_problem_records_listView_id);
        this.add_record_button = findViewById(R.id.provider_add_record_button_id);
        this.problem_record_list_view.setOnItemClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        this.adapter = new ArrayAdapter<>(this, R.layout.item_list, this.records);
        this.problem_record_list_view.setAdapter(this.adapter);
    }

    /**
     * onItemClick
     * When a Record is clicked its information will be displayed
     *
     * @param parent - adapter
     * @param view - current view
     * @param position - position of click
     * @param id - id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record)parent.getItemAtPosition(position);

        if (record instanceof PatientRecord){
            Intent intent = new Intent(this,ViewRecordActivity.class);
            intent.putExtra("PATIENTRECORDIDEXTRA", record.getUUID());
            intent.putExtra("USERIDEXTRA",this.patientId);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this,ViewCommentRecordActivity.class);
            intent.putExtra("RECORDID", record.getUUID());
            startActivity(intent);

        }

    }

    /**
     * providerAddCommentRecord
     * This method is called when the add_record_button is clicked.
     * Goes to ProviderAddCommentRecord activity which
     * allows the provider to add a comment record into the selected problem.
     *
     * passes the providerID, patientID and problemUUID through intent.
     * @param view - current view
     */

    public void providerAddCommentRecord(View view){
        Intent intent = new Intent(this,ProviderAddCommentRecord.class);
        intent.putExtra(PROVIDERID, this.providerID);
        intent.putExtra(USERIDEXTRA, this.patientId);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);

    }
}