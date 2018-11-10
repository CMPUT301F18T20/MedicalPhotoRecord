package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301f18t20.medicalphotorecord.R;

public class PatientHomeMenuActivity extends AppCompatActivity {

    protected Button EditContactInfoButton,
            ListOfProblemsButton,
            ViewProfileButton,
            DeleteProfileButton,
            LogOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_menu);

        EditContactInfoButton = findViewById(R.id.EditContactInfoButton);
        ListOfProblemsButton = findViewById(R.id.ListOfProblemsButton);
        ViewProfileButton = findViewById(R.id.ViewProfileButton);
        DeleteProfileButton = findViewById(R.id.DeleteProfile);
        LogOutButton = findViewById(R.id.LogOutButton);
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(this, ModifyUserActivity.class);
        startActivity(intent);
    }

    public void onListOfProblemsClick(View v) {
        //Intent intent = new Intent(this, BrowseUserProblems.class);
        //startActivity(intent);
    }

    public void onViewProfileClick(View v) {
        Intent intent = new Intent(this, ViewUserActivity.class);
        startActivity(intent);
    }

    public void onDeleteClick(View v) {
        //create fragment that comes up and asks if the user is sure
    }

    public void onLogoutClick(View v) {
        finish();
    }


}
