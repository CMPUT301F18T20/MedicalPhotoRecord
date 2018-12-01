/*
 * Class name: Photo
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 7:47 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import Exceptions.PhotoTooLargeException;

/**
 * Photo class, contains bodylocation, bitmap and label
 *
 * @version 1.0
 * @see User
 * @see Problem
 * @see Record
 * @since 1.0
 */
public class Photo {

    protected final String UUID = java.util.UUID.randomUUID().toString();
    private String recordUUID;
    private String problemUUID;
    private String bodyLocation;
    private String label;
    private String isViewedBodyPhoto;
    private Bitmap bitmap;
    private String bitmapString;  // need to save as string for bitmap data to be properly saved in offline database
    private static int maxBytes=65536;

    public Photo() {}

    /**
     * Photo constructor: automatically save bitmap as string so that photo object can be saved online & offline
     * @param recordUUID
     * @param problemUUID
     * @param bodyLocation: head, chest, abdomen; arm, leg, hand, foot (right and left)
     * @param bitmap: image bitmap
     * @param label: user entered label
     * @throws PhotoTooLargeException:  thrown when >65536 bytes
     */
    public Photo(String recordUUID, String problemUUID, String bodyLocation, Bitmap bitmap, String label) throws PhotoTooLargeException {
        this.recordUUID = recordUUID;
        this.problemUUID = problemUUID;
        this.bodyLocation = bodyLocation;
        this.label = label;
        setBitmap(bitmap);
        saveBitMapAsString();
    }

    /**
     * isViewBodyPhoto setter: to either front and back
     * Used for displaying front and back body photos
     * @param isViewedBodyPhoto
     */
    public void setIsViewedBodyPhoto(String isViewedBodyPhoto){
        this.isViewedBodyPhoto = isViewedBodyPhoto;
    }

    /**
     * isViewedBodyPhoto getter
     * @return isViewedBodyPhoto
     */
    public String isViewedBodyPhoto() {
        return this.isViewedBodyPhoto;
    }

    /**
     * recordUUID getter
     * @return recordUUID
     */
    public String getRecordUUID(){
        return this.recordUUID;
    }

    /**
     * bodyLocation getter
     * @return bodyLocation
     */
    public String getBodyLocation(){ return this.bodyLocation;}

    /**
     * label getter
     * @return label
     */
    public String getLabel(){ return this.label;}

    /**
     * problemUUID getter
     * @return problemUUID
     */
    public String getProblemUUID() { return this.problemUUID;}

    /**
     * UUID getter
     * @return UUID for photo object
     */
    public String getUUID(){ return this.UUID;}

    /**
     * recordUUID setter
     * @param recordUUID
     */
    public void setRecordUUID(String recordUUID){
        this.recordUUID = recordUUID;
    }

    /**
     * bitmap setter
     * @param inBitmap
     * @throws PhotoTooLargeException:  thrown when > 65536 bytes
     */
    public void setBitmap(Bitmap inBitmap) throws PhotoTooLargeException {

        // Check for photo size of compressed bit map
        if (inBitmap.getByteCount() <= Photo.maxBytes) {
            this.bitmap = inBitmap;
        } else {
            Log.d ("Photo Exception","Photo size too large" + String.valueOf(inBitmap.getByteCount()));
            throw new PhotoTooLargeException();
        }
    }

    /**
     * turn bitmap as a string for online and offline saving
     */
    public void saveBitMapAsString(){

        // Turn bitmap -> byte  -> string
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        byte[] bitmapByte = output.toByteArray();
        this.bitmapString = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
    }

    /**
     * get bitmap from string after loading from online and offline
     * @return bitmap
     */
    public Bitmap getBitmapFromString() {

        // Turn string -> byte  -> bitmap
        byte[] imageByte = Base64.decode(this.bitmapString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
        return bitmap;
    }


}
