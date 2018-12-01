package Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;
import Controllers.OfflineLoadController;
import Controllers.OfflineSaveController;
import Controllers.SignUpController;
import Exceptions.MustBeProviderOrPatientException;
import Exceptions.NoConnectionInSignUpException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

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
            //TODO write a test to make sure when you add, the get size is increased by 1
            //TODO implement restriction about unique UserIDs
            /* if provider is checked, create provider */
            if (ProviderCheckBox.isChecked()) {
                SignUpController.addProvider(
                        UserIDBox.getText().toString(),
                        EmailBox.getText().toString(),
                        PhoneBox.getText().toString(),
                        SignUp.this);

                /* if patient is checked, create patient */
            } else if (PatientCheckBox.isChecked()) {
                SignUpController.addPatient(
                        UserIDBox.getText().toString(),
                        EmailBox.getText().toString(),
                        PhoneBox.getText().toString(),
                        SignUp.this);

                //neither checkbox was ticked
            } else {
                throw new MustBeProviderOrPatientException();
            }

            //allow the check security token activity to handle login
            startActivity( new Intent(this, CheckSecurityToken.class));

        } catch (NoConnectionInSignUpException e) {
            Toast.makeText(this, "Device is offline", Toast.LENGTH_LONG).show();
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(this, "User ID must be at least 8 characters long",
                    Toast.LENGTH_LONG).show();
        } catch (UserAlreadyExistsException e) {
            Toast.makeText(this, "User with that User ID already exists",
                    Toast.LENGTH_LONG).show();
        } catch (MustBeProviderOrPatientException e) {
            Toast.makeText(this, "You must select either patient or provider",
                    Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Execution Exception in processing request",
                    Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interrupted Exception in processing request",
                    Toast.LENGTH_LONG).show();
        }
    }
}
