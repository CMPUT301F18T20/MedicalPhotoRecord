package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;

import Controllers.ModifyPatientController;
import Exceptions.NoSuchUserException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * ModifyPatientActivity
 * Simply allows the Patient to change their
 * contact information (Email and phone number)
 *
 * @version 1.0
 * @since   2018-12-01
 */

public class ModifyPatientActivity extends AppCompatActivity {

    private TextView userId_edit;
    private EditText email_edit;
    private EditText phone_edit;

    protected String gotEmail;
    protected String gotPhone;
    private Patient patient;
    protected String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_patient);

        // Get edit text views
        this.userId_edit = findViewById(R.id.user_text_id);
        this.email_edit = findViewById(R.id.email_edit_id);
        this.phone_edit = findViewById(R.id.phone_edit_id);

        // Get selected user
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        try {
            this.patient = new ModifyPatientController().getPatient(ModifyPatientActivity.this, this.userId);
        } catch (NoSuchUserException e) {
            Toast.makeText(ModifyPatientActivity.this, "Patient does not exist", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Display user's information
        this.userId_edit.setText(this.patient.getUserID());
        this.email_edit.setText(this.patient.getEmail());
        this.phone_edit.setText(this.patient.getPhoneNumber());

    }

    /**
     * This method is called when save_button_id button is clicked
     * This saves the details modified in the patients information
     * @param view - current view
     */
    public void saveButton(View view){

        // Get modified user's information
        this.gotEmail = this.email_edit.getText().toString();
        this.gotPhone = this.phone_edit.getText().toString();

        // Save modified patient, toast
        new ModifyPatientController().saveModifyPatient(ModifyPatientActivity.this, this.patient,this.gotEmail, this.gotPhone);
        Toast.makeText(ModifyPatientActivity.this, "Your patient info have been saved", Toast.LENGTH_LONG).show();
    }
}
