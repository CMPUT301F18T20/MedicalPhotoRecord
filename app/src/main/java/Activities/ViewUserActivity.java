package Activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
/** Used to View a user.  Must be passed UserID, Email and Phone through intent.
 *
 * @author mwhackma
 * @version 1.0
 * @see PatientHomeMenuActivity
 * @see ProviderHomeMenuActivity
 * @see GlobalSettings.GlobalSettings
 * @since 1.0
 */
public class ViewUserActivity extends AppCompatActivity {
    /** Links UI Elements and displays text on them.
     * Provided with UserID Email and Phone through intent
     * @param savedInstanceState used by onCreate super
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        TextView UserIDBox = findViewById(R.id.UserIDBox),
                EmailBox = findViewById(R.id.EmailBox),
                PhoneBox = findViewById(R.id.PhoneBox);

        Intent intent = getIntent();

        UserIDBox.setText(intent.getStringExtra(USERIDEXTRA));
        EmailBox.setText(intent.getStringExtra(EMAILEXTRA));
        PhoneBox.setText(intent.getStringExtra(PHONEEXTRA));
    }

    /** Terminates activity on back press
     * @param v Unused
     */
    public void onBackClick(View v) {
        finish();
    }
}
