package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SignUp extends AppCompatActivity {
    CheckBox PatientCheckBox, ProviderCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        PatientCheckBox = findViewById(R.id.PatientCheckBox);
        ProviderCheckBox = findViewById(R.id.ProviderCheckBox);
    }

    public void onProviderClick(View v) {

        /* if provider is checked, uncheck patient */
        if (PatientCheckBox.isChecked()) {
            PatientCheckBox.setChecked(false);
        }
    }

    public void onPatientClick(View v) {

        /* if patient is checked, uncheck provider */
        if (ProviderCheckBox.isChecked()) {
            ProviderCheckBox.setChecked(false);
        }
    }

    public void onSaveClick(View v) {
        User user = new User("9944888855");
        user.setEmail("hello@gmail.com");
        user.setPhoneNumber("00778854545");
        new ElasticsearchUserController.AddUserTask().execute(user);
    }
}
