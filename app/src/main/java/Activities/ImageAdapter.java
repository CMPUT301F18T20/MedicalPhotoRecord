package Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import Controllers.PhotoController;

// Custom image adapter for grid view
public class ImageAdapter extends BaseAdapter {

    private ArrayList<Bitmap> recordBitmaps;

    private Context context;

    // Get context and bit maps for that specific records
    public ImageAdapter(Context context, String recordUUID){
        this.context = context;
        this.recordBitmaps = new PhotoController().getBitMapsForRecord(context, recordUUID);
    }

    @Override
    public int getCount() {
        return this.recordBitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        // If image view is not set, set it
        if (convertView == null){
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(400,400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }else{
            imageView = (ImageView) convertView;
        }

        // Display each image bit map
        imageView.setImageBitmap(this.recordBitmaps.get(position));
        return imageView;
    }
}
