package Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.PhotoController;

// Custom image adapter for grid view
public class ImageAdapter extends BaseAdapter {

    private ArrayList<Photo> recordPhotos;
    private ArrayList<Bitmap> recordBitmaps;
    private ArrayList<String> recordLabels;
    private Context context;

    // Get context and bit maps, labels for that specific records
    public ImageAdapter(Context context, String recordUUID, String normalOrBody){
        this.context = context;

        if (normalOrBody == "normal"){
            this.recordPhotos = new PhotoController().getPhotosForRecord(context, recordUUID);
            this.recordBitmaps = new PhotoController().getBitMapForPhotoList(context, this.recordPhotos);
            this.recordLabels = new PhotoController().getLabelForPhotoList(context, this.recordPhotos);
        }

        if (normalOrBody == "body"){
            this.recordPhotos = new PhotoController().getBodyPhotosForRecord(context, recordUUID);
            this.recordBitmaps = new PhotoController().getBitMapForPhotoList(context, this.recordPhotos);
            this.recordLabels = new PhotoController().getLabelForPhotoList(context, this.recordPhotos);
        }
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

        // Get layout inflater to set image view and text view
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.browse_images_adapter_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.browse_image_view_id);
        TextView textView = convertView.findViewById(R.id.browse_image_text_id);

        // Display each image bit map and label text
        imageView.setImageBitmap(this.recordBitmaps.get(position));
        textView.setText(this.recordLabels.get(position));
        return convertView;

    }
}
