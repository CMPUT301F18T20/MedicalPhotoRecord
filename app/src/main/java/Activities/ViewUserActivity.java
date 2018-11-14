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

public class ViewUserActivity extends AppCompatActivity {
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

    public void onBackClick(View v) {
        finish();
    }
}
