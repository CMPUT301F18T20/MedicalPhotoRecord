package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cmput301f18t20.medicalphotorecord.R;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Controllers.AddCommentRecordController;
import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderAddCommentRecord extends AppCompatActivity {
    protected String providerID;
    protected String patientID;
    protected String problemUUID;
    protected EditText commentTitleEditText, commentEditText;
    protected String commentTitle, comment;
    protected AddCommentRecordController controller = new AddCommentRecordController();
    private Date record_date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_add_comment_record);

        Intent intent = getIntent();
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.patientID = intent.getStringExtra(USERIDEXTRA);
        this.providerID = intent.getStringExtra(PROVIDERID);

        this.commentTitleEditText = findViewById(R.id.provider_add_comment_title_id);
        this.commentEditText = findViewById(R.id.provider_add_comment_multi_id);

    }

    public void addCommentButton(View View) throws TitleTooLongException, CommentTooLongException, UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {
        this.commentTitle = this.commentTitleEditText.getText().toString();
        this.comment = this.commentEditText.getText().toString();

        this.controller.addRecord(this.providerID, this.commentTitle, this.comment, this.record_date, this.patientID, problemUUID);

    }
}
