package Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import Controllers.PhotoController;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> recordBitmaps;

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

        if (convertView == null){
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(400,400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }else{
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(this.recordBitmaps.get(position));
        return imageView;
    }
}
