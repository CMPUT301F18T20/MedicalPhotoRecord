package Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.PhotoController;

/**
 * SlideShowViewPagerAdapter
 * Custom view pager adapter that shows slide show photos of all records for a problem
 * @version 2.0
 * @see Photo
 * @see com.cmput301f18t20.medicalphotorecord.Problem
 * @see com.cmput301f18t20.medicalphotorecord.Record
 */
public class SlideShowViewPagerAdapter extends PagerAdapter {

    private ArrayList<Photo> problemPhotos;
    private ArrayList<Bitmap> problemBitmaps;
    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * Get all photos, bitmaps for a problems
     * @param context
     * @param problemUUID
     */
    public SlideShowViewPagerAdapter(Context context, String problemUUID){
        this.context = context;
        this.problemPhotos = new PhotoController().getPhotosForProblem(context, problemUUID);
        this.problemBitmaps = new PhotoController().getBitMapsForPhotoList(context, problemPhotos);
    }


    @Override
    public int getCount() {
        return this.problemBitmaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * Instantiate a view for image to be shown
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position){

        // Get inflater and image view
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_show_custom_layout,null);
        ImageView imageView = view.findViewById(R.id.slide_show_custom_layout_image_view_id);

        // Change it to a bigger image
        Bitmap bitmapCompressed = Bitmap.createScaledBitmap(this.problemBitmaps.get(position), 700, 700, true);
        imageView.setImageBitmap(bitmapCompressed);

        // Add view to view pager
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    /**
     * Destroy previously shown view (for new one to be shown)
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        // Next view
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}
