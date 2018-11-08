package Activities;

import android.content.Intent;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
=======
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
>>>>>>> 1ecbe36d5e72a9e8b8f67e145a1f20ec30cac6ea
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;
<<<<<<< HEAD
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;

import Controllers.BrowseUserController;
import Controllers.OfflineSaveController;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class BrowseUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView browse_user_list_view;
    private ArrayList<User> users;
    private BrowseUserController browseUserController = new BrowseUserController();
=======
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import Controllers.ElasticsearchPatientController;

public class BrowseUserActivity extends AppCompatActivity {

    private ListView browse_user_list_view;
    private FloatingActionButton fab;

    ArrayList<Patient> users = new ArrayList<Patient>();
>>>>>>> 1ecbe36d5e72a9e8b8f67e145a1f20ec30cac6ea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);
<<<<<<< HEAD

        this.browse_user_list_view = (ListView)findViewById(R.id.browse_user_id);
        this.browse_user_list_view.setOnItemClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();

        // Display list of users/ patients
        this.users = this.browseUserController.getUserList(BrowseUserActivity.this);
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, R.layout.item_list,users);
        this.browse_user_list_view.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id){

        // Get position of clicked item and pass it on to ModifyUserActivity for later processing
        Intent intent = new Intent(BrowseUserActivity.this,ModifyUserActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
=======
        browse_user_list_view = (ListView)findViewById(R.id.browse_user_id);
        fab =  findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add an item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            this.users = new ElasticsearchPatientController.GetPatientTask().execute().get();
        } catch (Exception e) {
            //TODO handle exceptions
        }
        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, R.layout.item_list,users);
        browse_user_list_view.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position){

        // Get position of clicked item and pass it on to ViewUserActivity for later processing
        Intent intent = new Intent(BrowseUserActivity.this, ViewUserActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
>>>>>>> 1ecbe36d5e72a9e8b8f67e145a1f20ec30cac6ea
