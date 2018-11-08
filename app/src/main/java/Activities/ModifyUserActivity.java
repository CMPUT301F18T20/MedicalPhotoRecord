package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;

import Controllers.ModifyUserController;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyUserActivity extends AppCompatActivity {

    private EditText userId_edit;
    private EditText email_edit;
    private EditText phone_edit;
    private String gotUserId;
    private String gotEmail;
    private String gotPhone;
    private User user;
    private int position;
    private ModifyUserController modifyUserController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        // Get edit text views
        this.userId_edit = (EditText)findViewById(R.id.userid_edit_id);
        this.email_edit = (EditText)findViewById(R.id.email_edit_id);
        this.phone_edit = (EditText)findViewById(R.id.phone_edit_id);

        // Get selected user
        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",0);
        this.modifyUserController = new ModifyUserController(ModifyUserActivity.this);
        this.user = modifyUserController.getUser(position);
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
        this.gotUserId = this.userId_edit.getText().toString();
        this.gotEmail = this.email_edit.getText().toString();
        this.gotPhone = this.phone_edit.getText().toString();

        // Save
        this.modifyUserController.saveUser(ModifyUserActivity.this,this.position, this.gotUserId, this.gotEmail, this.gotPhone);
    }
}
