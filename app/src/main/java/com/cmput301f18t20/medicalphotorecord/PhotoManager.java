package com.cmput301f18t20.medicalphotorecord;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class PhotoManager {
    ArrayList<ContactsContract.Contacts.Photo> Photos;

    public ArrayList<ContactsContract.Contacts.Photo> getPhotos() {
        return Photos;
    }

    public void setPhotos(ArrayList<ContactsContract.Contacts.Photo> photos) {
        Photos = photos;
    }

    public void addPhoto(ContactsContract.Contacts.Photo photo) {
        Photos.add(photo);
    }

    public void deletePhoto(ContactsContract.Contacts.Photo photo) {
        Photos.remove(photo);
    }

    public void clearPhotos() {
        Photos.clear();
    }
}
