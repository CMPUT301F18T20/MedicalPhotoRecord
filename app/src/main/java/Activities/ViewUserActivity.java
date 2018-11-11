package Activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.User;

public class ViewUserActivity extends AppCompatActivity {
    private EditText UserIDBox,
            EmailBox,
            PhoneBox;

    //TODO make these extra tags a global setting
    public static final String
            UserIDExtra = "UserID",
            EmailExtra = "Email",
            PhoneExtra = "Phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        UserIDBox = findViewById(R.id.UserIDBox);
        EmailBox = findViewById(R.id.EmailBox);
        PhoneBox = findViewById(R.id.PhoneBox);

        Intent intent = getIntent();

        UserIDBox.setText(intent.getStringExtra(UserIDExtra));
        EmailBox.setText(intent.getStringExtra(EmailExtra));
        PhoneBox.setText(intent.getStringExtra(PhoneExtra));
    }

    public void onBackClick(View v) {
        finish();
    }
}
