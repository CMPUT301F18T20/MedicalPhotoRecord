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

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class SearchByBodyLocationController {

    public static class TaskParams{
        protected ArrayList<String> bodyLocations;
        protected ArrayList<String> userIDs;

        TaskParams(ArrayList<String> bodyLocations,ArrayList<String> userIDs){
            this.bodyLocations = bodyLocations;
            this.userIDs = userIDs;
        }

        public ArrayList<String> getBodyLocations(){return this.bodyLocations;}

        public ArrayList<String> getUserIDs(){return this.userIDs;}
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

    public ArrayList<String> getNodeList(String bodyLocation) {
        ArrayList<String> list = new ArrayList<>();
        if (bodyLocation.equals("head")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upper back"
                    , "left arm"
                    , "right arm"));
            return list;
        } else if (bodyLocation.equals(("chest")) || bodyLocation.equals("upper back")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upper back"
                    , "left arm"
                    , "right arm"
                    , "abs", "lower back"));
            return list;

        } else if (bodyLocation.equals("left arm")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upper back"
                    , "left arm"
                    , "left hand"));
            return list;

        } else if (bodyLocation.equals("left hand")) {
            list.addAll(Arrays.asList("left hand", "left arm"));
            return list;

        } else if (bodyLocation.equals("right arm")) {
            list.addAll(Arrays.asList("head", "chest"
                    , "upper back"
                    , "right arm"
                    , "right hand"));
            return list;

        } else if (bodyLocation.equals("right hand")) {
            list.addAll(Arrays.asList("right hand", "right arm"));
            return list;

        } else if (bodyLocation.equals("abs") || bodyLocation.equals("lower back")) {
            list.addAll(Arrays.asList("abs", "lower back"
                    , "left leg"
                    , "right leg"
                    , "chest"
                    , "upper back"));
            return list;

        } else if (bodyLocation.equals("left leg")) {
            list.addAll(Arrays.asList("left leg", "left foot"
                    , "lower back"
                    , "abs"));
            return list;

        } else if (bodyLocation.equals("left foot")) {
            list.addAll(Arrays.asList("left foot", "left leg"));
            return list;

        } else if (bodyLocation.equals("right leg")) {
            list.addAll(Arrays.asList("right leg", "right foot"
                    , "lower back"
                    , "abs"));
            return list;

        } else if (bodyLocation.equals("right foot")) {
            list.addAll(Arrays.asList("right foot", "right leg"));
            return list;
        }

        list.add("Something went wrong");
        return list;
    }


}