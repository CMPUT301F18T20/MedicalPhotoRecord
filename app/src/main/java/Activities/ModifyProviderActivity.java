/*
 * Class name: ModifyProviderActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 4:10 PM
 *
 * Last Modified: 11/15/18 4:08 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;

import Controllers.ModifyProviderController;
import Exceptions.NoSuchUserException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * ModifyProviderActivity
 * Simply allows the Provider to modify
 * their contact information (Email and Phone number)
 *
 * @version 1.0
 * @since   2018-12-01
 */

public class ModifyProviderActivity extends AppCompatActivity {

    private TextView provider_userId_edit;
    private EditText provider_email_edit;
    private EditText provider_phone_edit;
    protected String gotEmail;
    protected String gotPhone;
    private Provider provider;
    protected String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_provider);

        // Get edit text views
        this.provider_userId_edit = findViewById(R.id.provider_user_text_id);
        this.provider_email_edit = findViewById(R.id.provider_email_edit_id);
        this.provider_phone_edit = findViewById(R.id.provider_phone_edit_id);

        // Get selected user
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        try {
            this.provider = new ModifyProviderController().getProvider(ModifyProviderActivity.this, this.userId);
        } catch (NoSuchUserException e) {
            Toast.makeText(ModifyProviderActivity.this, "Provider does not exist", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Display user's information
        this.provider_userId_edit.setText(this.provider.getUserID());
        this.provider_email_edit.setText(this.provider.getEmail());
        this.provider_phone_edit.setText(this.provider.getPhoneNumber());

    }

    /**
     * This method is called when provider_save_button_id button is clicked.
     * Simply save the changes made on the provider's contact information.
     * @param view - current view
     */

    public void providerSaveButton(View view){

        // Get modified user's information
        this.gotEmail = this.provider_email_edit.getText().toString();
        this.gotPhone = this.provider_phone_edit.getText().toString();

        // Save
        new ModifyProviderController().saveModifiedProvider(ModifyProviderActivity.this, this.provider, this.gotEmail, this.gotPhone);
        Toast.makeText(ModifyProviderActivity.this, "Your provider info have been saved", Toast.LENGTH_LONG).show();
    }
}
