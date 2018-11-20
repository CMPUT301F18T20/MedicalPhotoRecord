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

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.ArrayList;
import java.util.List;

import Controllers.BrowseProblemRecordsController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class BrowseProblemRecords extends AppCompatActivity /*implements AdapterView.OnItemClickListener */{

    private ListView browse_problem_record_list_view;
    private Button add_record_button;
    private ArrayList<Record> records;
    private BrowseProblemRecordsController browseProblemRecordsController = new BrowseProblemRecordsController();
    private String userId;
    private int position;
    private ArrayAdapter<Record> adapter;

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.position = intent.getIntExtra("position", 0);
        this.records = browseProblemRecordsController.getRecordList(BrowseProblemRecords.this ,this.userId, this.position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_problem_records);

        this.browse_problem_record_list_view = (ListView)findViewById(R.id.browse_problem_records_id);
        this.add_record_button = (Button)findViewById(R.id.add_record_button_id);
        //this.browse_problem_record_list_view.setOnItemClickListener(this);

        //Initialize menu for long click
        registerForContextMenu(this.browse_problem_record_list_view);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes

=======
>>>>>>> Stashed changes


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_problem_records_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_problem_records_menu,menu);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_problem_records_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_problem_records_menu,menu);
        }
=======


>>>>>>> Stashed changes
=======


>>>>>>> Stashed changes
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_problem_records_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_problem_records_menu,menu);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // this.records = browseProblemRecordsController.getRecordList(BrowseProblemRecords.this ,this.userId, this.position);
        this.adapter = new ArrayAdapter<Record>(this, R.layout.item_list, this.records);
        this.browse_problem_record_list_view.setAdapter(this.adapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;
        switch(item.getItemId()){
            case R.id.modify_record_id:
                return true;

            case R.id.delete_record_key:
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    public void addRecord (View view) {
        // Todo add Record
        Intent intent = new Intent(BrowseProblemRecords.this, AddRecordActivity.class);
        intent.putExtra("position", this.position);
        intent.putExtra(USERIDEXTRA, this.userId);
        startActivity(intent);
    }


}
