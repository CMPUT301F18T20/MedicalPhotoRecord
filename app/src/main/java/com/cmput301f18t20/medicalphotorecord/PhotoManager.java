package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class PhotoManager {
    ArrayList<Photo> Photos;

    public ArrayList<Photo> getPhotos() {
        return Photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        Photos = photos;
    }
    
    public void addPhoto(Photo photo) {
        Photos.add(photo);
    }

    public void deletePhoto(Photo photo) {
        Photos.remove(photo);
    }

    public void clearPhotos() {
        Photos.clear();
    }
}
