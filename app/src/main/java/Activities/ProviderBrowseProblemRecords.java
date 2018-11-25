package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchRecordController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderBrowseProblemRecords extends AppCompatActivity implements AdapterView.OnItemClickListener{

    protected String patientId;
    protected String problemUUID;
    protected ArrayList<Record> records;
    protected ListView problem_record_list_view;
    protected Button add_record_button;
    private ArrayAdapter<Record> adapter;
    protected String providerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_browse_problem_records);

        Intent intent = getIntent();

        this.providerID = intent.getStringExtra(PROVIDERID);
        this.patientId = intent.getStringExtra(USERIDEXTRA);
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        try {
            this.records = new ElasticsearchRecordController.GetRecordsWithProblemUUID().execute(this.problemUUID).get();
        } catch (ExecutionException e){
            throw new RuntimeException(e);
        }catch (InterruptedException i){
            throw new RuntimeException(i);
        }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record)parent.getItemAtPosition(position);
        Intent intent = new Intent(this,ViewRecordActivity.class);

        //
        // intent.putExtra("CHOSENRECORD",record);
        //
        startActivity(intent);
    }

    public void providerAddCommentRecord(View view){
        Intent intent = new Intent(this,ProviderAddCommentRecord.class);
        intent.putExtra(PROVIDERID, this.providerID);
        intent.putExtra(USERIDEXTRA, this.patientId);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);

    }
}
