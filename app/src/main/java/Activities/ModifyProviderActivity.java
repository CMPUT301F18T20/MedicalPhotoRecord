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

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ModifyProviderActivity extends AppCompatActivity {

    private TextView provider_userId_edit;
    private EditText provider_email_edit;
    private EditText provider_phone_edit;
    private String gotEmail;
    private String gotPhone;
    private Provider provider;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_provider);

        // Get edit text views
        this.provider_userId_edit = (TextView)findViewById(R.id.provider_user_text_id);
        this.provider_email_edit = (EditText)findViewById(R.id.provider_email_edit_id);
        this.provider_phone_edit = (EditText)findViewById(R.id.provider_phone_edit_id);

        // Get selected user
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.provider = new ModifyProviderController().getProvider(ModifyProviderActivity.this, this.userId);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Display user's information
        this.provider_userId_edit.setText(this.provider.getUserID());
        this.provider_email_edit.setText(this.provider.getEmail());
        this.provider_phone_edit.setText(this.provider.getPhoneNumber());

    }

    public void providerSaveButton(View view){

        // Get modified user's information
        this.gotEmail = this.provider_email_edit.getText().toString();
        this.gotPhone = this.provider_phone_edit.getText().toString();

        // Save
        new ModifyProviderController().saveModifiedProvider(ModifyProviderActivity.this, this.provider, this.gotEmail, this.gotPhone);
        Toast.makeText(ModifyProviderActivity.this, "Your provider info have been saved", Toast.LENGTH_LONG).show();
    }
}
