package com.cmput301f18t20.medicalphotorecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    CheckBox PatientCheckBox, ProviderCheckBox;
    EditText UserIDBox, EmailBox, PhoneBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        PatientCheckBox = findViewById(R.id.PatientCheckBox);
        ProviderCheckBox = findViewById(R.id.ProviderCheckBox);

        UserIDBox = findViewById(R.id.UserIDBox);
        EmailBox = findViewById(R.id.EmailBox);
        PhoneBox = findViewById(R.id.PhoneBox);
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

        try {
//TODO write a test to make sure you can't check both checkboxes at the same time
            /* if provider is checked, create provider */
            if (ProviderCheckBox.isChecked()) {
                Provider user = new Provider(
                        UserIDBox.getText().toString(),
                        EmailBox.getText().toString(),
                        PhoneBox.getText().toString());
                new ElasticsearchProviderController.AddProviderTask().execute(user);

            /* if patient is checked, create patient */
            } else if (PatientCheckBox.isChecked()) {
                Patient user = new Patient(
                        UserIDBox.getText().toString(),
                        EmailBox.getText().toString(),
                        PhoneBox.getText().toString());
                new ElasticsearchPatientController.AddPatientTask().execute(user);
            }

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(this, "User ID must be at least 8 characters long",
                    Toast.LENGTH_LONG).show();
        }
    }
}
