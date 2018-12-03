package Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

/**
 * SlideshowActivity
 * Show slide show photos for all records in a problem
 * @version 2.0
 * @see Photo
 * @see com.cmput301f18t20.medicalphotorecord.Problem
 * @see com.cmput301f18t20.medicalphotorecord.Record
 */
public class SlideshowActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private String problemUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        // Get problem uuid
        Intent intent = getIntent();
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        // Pass problem uuid to adapter to show slide show
        viewPager = findViewById(R.id.slide_show_view_pager_id);
        this.viewPager.setAdapter(new SlideShowViewPagerAdapter(SlideshowActivity.this, this.problemUUID));
    }
}
