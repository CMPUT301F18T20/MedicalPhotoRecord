package com.cmput301f18t20.medicalphotorecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BrowseUserActivity extends AppCompatActivity {

    private ListView browse_user_list_view;
    ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);
        browse_user_list_view = (ListView)findViewById(R.id.browse_user_id);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // This will be replaced with loading patient's list from file or elastic search database
        Patient p1 = null;
        try {
            p1 = new Patient("aaaaapatient1", "p1@email.com", "111111111111");
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            e.printStackTrace();
        }
        Patient p2 = null;
        try {
            p2 = new Patient("aaaapatient2","p2@email.com", "222222222222");
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            e.printStackTrace();
        }

        this.users.add(p1);
        this.users.add(p2);

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, R.layout.item_list,users);
        browse_user_list_view.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position){

        // Get position of clicked item and pass it on to ViewUserActivity for later processing
        Intent intent = new Intent(BrowseUserActivity.this,ViewUserActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
