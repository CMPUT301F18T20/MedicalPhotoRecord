package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;

import Controllers.AddDeleteRecordController;
import Controllers.BrowseProblemRecordsController;
import Controllers.ProviderRecordsController;
import Controllers.PhotoController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * BrowseProblemRecords
 * For a specific problem:
 * + Patient can see a list of all records associated to that problem
 * + Patient can long click to delete a record associated to that problem
 * + Patient can long click to modify a record associated to that problem
 * + Patient can click a button to add a record associated to that problem
 * + Patient can click to view a record associated to that problem
 * @version 1.0
 * @see Record
 * @see com.cmput301f18t20.medicalphotorecord.Problem
 * @see com.cmput301f18t20.medicalphotorecord.Patient
 */
public class BrowseProblemRecords extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView browse_problem_record_list_view;
    private Button add_record_button;
    private ArrayList<Record> records;
    private BrowseProblemRecordsController browseProblemRecordsController = new BrowseProblemRecordsController();
    private String userID;
    private String problemUUID;
    private ArrayAdapter<Record> adapter;

    /**
     * Get from intent userID, problemUUID
     * Get list of records from that userId, problemUUID
     */
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        this.userID = intent.getStringExtra(USERIDEXTRA);
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.records = new ProviderRecordsController().getRecords(this,this.problemUUID,this.userID);
    }

    /**
     * Set all necessay views, buttons and context menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_problem_records);

        this.browse_problem_record_list_view = (ListView)findViewById(R.id.browse_problem_records_id);
        this.add_record_button = (Button)findViewById(R.id.add_record_button_id);
        this.browse_problem_record_list_view.setOnItemClickListener(this);

        //Initialize menu for long click
        registerForContextMenu(this.browse_problem_record_list_view);


    }

    /**
     * Showing list view for all records
     */
    @Override
    protected void onResume(){
        super.onResume();
        this.adapter = new ArrayAdapter<Record>(this, R.layout.item_list, this.records);
        this.browse_problem_record_list_view.setAdapter(this.adapter);


    }

    /**
     * Context menu created for long clicked
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_problem_records_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_problem_records_menu,menu);
        }
    }

    /**
     * Add record button clicked, go to necessary steps to add a new record
     * @param view
     */
    public void addRecord (View view) {
        // Todo add Record
        new PhotoController().clearTempPhotos(this);
        Intent intent = new Intent(BrowseProblemRecords.this, BodyLocationActivity.class);
        intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
        intent.putExtra("USERIDEXTRA", this.userID);
        startActivity(intent);
    }

    /**
     * If patient long clicked on a specific record
     * + Modify: patient record (go to ModifyRecordActivity), record (can't be modify)
     * + Delete: patient record (delete record online and offline ), record (can't be modify)
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;
        switch(item.getItemId()){
            case R.id.modify_record_id:

                if (!(adapter.getItem(longClickPos) instanceof Record)){

                    Toast.makeText(this,"Can't modify comment record",Toast.LENGTH_LONG).show();
                    return true;
                }

                PatientRecord record = (PatientRecord) adapter.getItem(longClickPos);
                Intent intent = new Intent(this,ModifyRecordActivity.class);
                intent.putExtra("PATIENTRECORDIDEXTRA",record.getUUID());
                intent.putExtra("USERIDEXTRA",this.userID);
                startActivity(intent);
                return true;

            case R.id.delete_record_key:


                if (!(adapter.getItem(longClickPos) instanceof Record)){
                    Toast.makeText(this,"Can't delete comment record",Toast.LENGTH_LONG).show();
                    return true;
                }

                PatientRecord rec = (PatientRecord) adapter.getItem(longClickPos);
                new AddDeleteRecordController().deleteRecord(this,rec);
                Toast.makeText(this,"Record has been deleted.",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * If patient click a specific record
     * + Patient record: (go to ViewRecordActivity)
     * + Record: (go to ViewCommentRecordActivity)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Record record = (Record)parent.getItemAtPosition(position);

        if (record instanceof PatientRecord){
            Intent intent = new Intent(this,ViewRecordActivity.class);
            intent.putExtra("PATIENTRECORDIDEXTRA", record.getUUID());
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this,ViewCommentRecordActivity.class);
            intent.putExtra("RECORDID", record.getUUID());
            startActivity(intent);

        }
    }
}