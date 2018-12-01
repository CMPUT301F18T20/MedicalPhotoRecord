package Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import java.util.ArrayList;

import Controllers.ElasticsearchProviderController;
import Controllers.ShortCodeController;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

public class ProviderHomeMenuActivity extends HomeMenuActivity {

    protected int getLayout() {
        return R.layout.activity_provider_home_menu;
    }

    protected Class<?> getModifyActivityClass() {
        return ModifyProviderActivity.class;
    }

    protected void FetchUserFile() {
        try {
            //get the user info for the signed in patient
            ArrayList<Provider> providers = new ElasticsearchProviderController
                    .GetProviderTask().execute(UserID).get();

            //grab the first (and hopefully only) provider in the results
            this.user = providers.get(0);

        } catch (Exception e) {
            Toast.makeText(this, "Exception while fetching patient file from database",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onListOfPatientsClick(View v) {
        Intent intent = new Intent(this, BrowseUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onDeleteClick(View v) {
        //create fragment that comes up and asks if the user is sure
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm Profile Deletion ... ");
        alertDialog.setMessage("Are you sure you want to delete your profile?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProviderHomeMenuActivity.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProviderHomeMenuActivity.this, "Not deleted ", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}
