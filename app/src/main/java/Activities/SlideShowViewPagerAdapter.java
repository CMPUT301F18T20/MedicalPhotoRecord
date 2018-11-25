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

import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.PhotoController;

public class SlideShowViewPagerAdapter extends PagerAdapter {

    private ArrayList<Bitmap> recordBitmaps;
    private Context context;
    private LayoutInflater layoutInflater;

    public SlideShowViewPagerAdapter(Context context, String recordUUID){
        this.context = context;
        this.recordBitmaps = new PhotoController().getBitMapsForRecord(context, recordUUID);
    }


    @Override
    public int getCount() {
        return recordBitmaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_show_custom_layout,null);
        ImageView imageView = view.findViewById(R.id.slide_show_custom_layout_image_view_id);

        Bitmap bitmapCompressed = Bitmap.createScaledBitmap(this.recordBitmaps.get(position), 700, 700, true);
        imageView.setImageBitmap(bitmapCompressed);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}
