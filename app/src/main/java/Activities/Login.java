package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;
import Exceptions.NoSuchUserException;

public class Login extends AppCompatActivity {

    protected Button LoginButton, SignUpButton;
    protected EditText UserIDText;

    public final static int REQUEST_CODE_SIGNUP = 1;
    public static final String USER_ID_EXTRA = "UserID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.LoginButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        UserIDText = findViewById(R.id.UserIDText);
    }

    private String DecidePatientProviderOrNone(
            ArrayList<Patient> patients, ArrayList<Provider> providers, String userID) {

        //in place of a proper query for now
        for (Patient patient: patients) {
            if (patient.getUserID().equals(userID)) {
                return "patient";
            }
        }
        for (Provider provider: providers) {
            if (provider.getUserID().equals(userID)) {
                return "provider";
            }
        }
        return "patient"; //XXX TODO HACKED, if this is still patient, change it back to null
    }

    /**Run when the Login button is clicked on.  On successful login, transitions to home screen
     * for Provider or Patient.
     * @param view Unused as function only applies to one view anyways
     */
    public void onLoginClick(View view) {
        Intent intent;
        ArrayList<Patient> patients;
        ArrayList<Provider> providers;

        String userID = UserIDText.getText().toString();

        //TODO actual query!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //in place of a proper query for now
        try {
            patients = new ElasticsearchPatientController.GetPatientTask().execute().get();
            providers = new ElasticsearchProviderController.GetProviderTask().execute().get();

            //in place of a proper query for now
            switch (DecidePatientProviderOrNone(patients, providers, userID)) {
                case "patient":
                    intent = new Intent(this, PatientHomeMenuActivity.class);
                    break;
                case "provider":
                    intent = new Intent(this, ProviderHomeMenuActivity.class);
                    break;
                default:
                    throw new NoSuchUserException();
            }

            startActivity(intent);

        } catch (NoSuchUserException e) {
            Toast.makeText(this, "User not found",
                    Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            //TODO handle exceptions
        }

    }

    /**Run when the Sign up button is clicked on.  Transitions to sign up page.
     * @param view Unused as function only applies to one view anyways
     */
    public void onSignUpClick(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if the return is from a successful signup, set the userID entry to the newly created user
        if (requestCode == REQUEST_CODE_SIGNUP) {
            if (resultCode == RESULT_OK) {
                UserIDText.setText(
                        data.getStringExtra(USER_ID_EXTRA));
            }
        }
    }
}
