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
import com.cmput301f18t20.medicalphotorecord.User;

import Controllers.ModifyUserController;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import GlobalSettings.GlobalSettings;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;


public class ModifyUserActivity extends AppCompatActivity {

    private TextView userId_edit;
    private EditText email_edit;
    private EditText phone_edit;
    private String gotUserId;
    private String gotEmail;
    private String gotPhone;
    private Patient user;
    private String userId;
    private ModifyUserController modifyUserController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        // Get edit text views
        this.userId_edit = (TextView)findViewById(R.id.user_text_id);
        this.email_edit = (EditText)findViewById(R.id.email_edit_id);
        this.phone_edit = (EditText)findViewById(R.id.phone_edit_id);

        // Get selected user
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.modifyUserController = new ModifyUserController();
        this.user = modifyUserController.getPatient(ModifyUserActivity.this, this.userId);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Display user's information
        this.userId_edit.setText(this.user.getUserID());
        this.email_edit.setText(this.user.getEmail());
        this.phone_edit.setText(this.user.getPhoneNumber());

    }

    public void saveButton(View view){

        // Get modified user's information
        this.gotEmail = this.email_edit.getText().toString();
        this.gotPhone = this.phone_edit.getText().toString();

        // Save
        Patient patient = this.modifyUserController.createNewPatient(ModifyUserActivity.this, this.userId, this.gotEmail, this.gotPhone);
        this.modifyUserController.savePatient(ModifyUserActivity.this, patient);
    }
}
