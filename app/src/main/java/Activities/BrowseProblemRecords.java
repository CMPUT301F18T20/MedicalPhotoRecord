package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.BodyLocation;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.List;

import Controllers.AddRecordController;
import Controllers.BrowseProblemRecordsController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class BrowseProblemRecords extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView browse_problem_record_list_view;
    private Button add_record_button;
    private ArrayList<Record> records;
    private BrowseProblemRecordsController browseProblemRecordsController = new BrowseProblemRecordsController();
    private String userId;
    private int position;
    private ArrayAdapter<Record> adapter;
    private Problem currentProblem;

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.currentProblem = (Problem) intent.getSerializableExtra("CHOSENPROBEM");
        this.records = this.currentProblem.getRecords();
    }

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

    @Override
    protected void onResume(){
        super.onResume();
        this.adapter = new ArrayAdapter<Record>(this, R.layout.item_list, this.records);
        this.browse_problem_record_list_view.setAdapter(this.adapter);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_problem_records_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_problem_records_menu,menu);
        }
    }

    public void addRecord (View view) {
        // Todo add Record
        Intent intent = new Intent(BrowseProblemRecords.this, BodyLocationActivity.class);
        intent.putExtra("position", this.position);
        intent.putExtra(USERIDEXTRA, this.userId);
        startActivity(intent);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;
        switch(item.getItemId()){
            case R.id.modify_record_id:
                Record record = adapter.getItem(longClickPos);
                Intent intent = new Intent(this,ModifyRecordActivity.class);
                intent.putExtra("position",this.position);
                intent.putExtra("CHOSENRECORD",record);
                intent.putExtra("USERIDEXTRA",this.userId);
                startActivity(intent);
                return true;

            case R.id.delete_record_key:
                Record rec = adapter.getItem(longClickPos);
                new AddRecordController().saveRecord("delete",this,rec,this.position);
                Toast.makeText(this,"Record has been deleted.",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record)parent.getItemAtPosition(position);
        Intent intent = new Intent(this,ViewRecordActivity.class);
        intent.putExtra("CHOSENRECORD",record);
        startActivity(intent);
    }
}
