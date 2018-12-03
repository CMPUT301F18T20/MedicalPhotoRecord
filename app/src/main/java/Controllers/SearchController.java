/*
 * Class name: SearchByBodyLocationController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 30/11/18 8:00 PM
 *
 * Last Modified: 30/11/18 8:00 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.util.Log;
import android.widget.CheckBox;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import Activities.FilterArrayAdapter;

public class SearchController {

    public class TaskParams{
        protected ArrayList<String> bodyLocations;
        protected ArrayList<String> userIDs;
        protected ArrayList<String> GeoLocations;
        protected String[] keywords;

        public TaskParams(ArrayList<String> userIDs,ArrayList<String> bodyLocations){
            this.bodyLocations = bodyLocations;
            this.userIDs = userIDs;
        }
        public TaskParams(ArrayList<String> userIDs, ArrayList<String> bodyLocations, String[] keywords){
            this.userIDs=userIDs;
            this.bodyLocations = bodyLocations;
            this.keywords = keywords;
        }

        public ArrayList<String> getBodyLocations(){return this.bodyLocations;}

        public ArrayList<String> getUserIDs(){return this.userIDs;}

        public String[] getKeywords(){return this.keywords;}
    }

    public ArrayList<PatientRecord> fetchNearBodyLocation(ArrayList<String> userIDs, String bodyLocation){
        ArrayList<String> nodeList = getNodeList(bodyLocation);
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        TaskParams params = new TaskParams(userIDs,nodeList);
        try {
            patientRecords = new ElasticsearchPatientRecordController.GetPatientRecordsByBodyLocation().execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return patientRecords;

    }

    public ArrayList<PatientRecord> fetchUsingKeyWordAndBody(ArrayList<String> userIDs, String bodyLocation,String[] keyWords){
        ArrayList<String> nodeList = getNodeList(bodyLocation);
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        TaskParams params = new TaskParams(userIDs,nodeList,keyWords);
        try {
            patientRecords = new ElasticsearchPatientRecordController
                    .GetPatientRecordsByKeywordAndBodyLocation().execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return patientRecords;

    }

    public ArrayList<String> getNodeList(String bodyLocation) {
        ArrayList<String> list = new ArrayList<>();
        if (bodyLocation.equals("head")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upperBack"
                    , "leftArm"
                    , "rightArm"));
            return list;
        } else if (bodyLocation.equals(("chest")) || bodyLocation.equals("upper back")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upperBack"
                    , "leftArm"
                    , "rightArm"
                    , "abs", "lowerBack"));
            return list;

        } else if (bodyLocation.equals("leftArm")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upperBack"
                    , "leftArm"
                    , "leftHand"));
            return list;

        } else if (bodyLocation.equals("leftHand")) {
            list.addAll(Arrays.asList("leftHand", "leftArm"));
            return list;

        } else if (bodyLocation.equals("rightArm")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upperBack"
                    , "rightArm"
                    , "rightHand"));
            return list;

        } else if (bodyLocation.equals("rightHand")) {
            list.addAll(Arrays.asList("rightHand", "rightArm"));
            return list;

        } else if (bodyLocation.equals("abs") || bodyLocation.equals("lowerBack")) {
            list.addAll(Arrays.asList("abs", "lowerBack"
                    , "leftLeg"
                    , "rightLeg"
                    , "chest"
                    , "upperBack"));
            return list;

        } else if (bodyLocation.equals("leftLeg")) {
            list.addAll(Arrays.asList("leftLeg", "leftFoot"
                    , "lowerBack"
                    , "abs"));
            return list;

        } else if (bodyLocation.equals("leftFoot")) {
            list.addAll(Arrays.asList("leftFoot", "leftLeg"));
            return list;

        } else if (bodyLocation.equals("rightLeg")) {
            list.addAll(Arrays.asList("rightLeg", "rightFoot"
                    , "lowerBack"
                    , "abs"));
            return list;

        } else if (bodyLocation.equals("rightFoot")) {
            list.addAll(Arrays.asList("rightFoot", "rightLeg"));
            return list;
        }

        list.add("Something went wrong");
        return list;
    }


    //checks which boxes are checked and adjusts the filter accordingly
    public Filter checkFilter(FilterArrayAdapter filterAdapter){
        Filter filter = new Filter();
        ArrayList<CheckBox> boxList = new ArrayList<>();
        boxList = filterAdapter.getCheckBoxes();

        if (!boxList.get(0).isChecked()){
            filter.setProblemIncludedStatus(false);
        }
        if(boxList.get(1).isChecked()){
            filter.setRecordIncludedStatus(true);
        }else{
            filter.setRecordIncludedStatus(false);
        }
        if(boxList.get(2).isChecked()){
            filter.setPatientRecordIncludedStatus(true);
        }else{
            filter.setPatientRecordIncludedStatus(false);
        }
        if(boxList.get(3).isChecked()){
            filter.setBodyLocationIncludedStatus(true);
        }else{
            filter.setBodyLocationIncludedStatus(false);
        }
        if(boxList.get(4).isChecked()){
            filter.setGeoIncludedStatus(true);
        }else{
            filter.setGeoIncludedStatus(false);
        }
        return filter;
    }
}
