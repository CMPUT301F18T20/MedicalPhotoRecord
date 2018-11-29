package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import static GlobalSettings.GlobalSettings.NumberOfElasticsearchRetries;
import static GlobalSettings.GlobalSettings.getIndex;
import static io.searchbox.params.Parameters.SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ElasticsearchPhotoController {

    static JestDroidClient client = null;

    public final static String matchAllquery =
            "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_all\" : {}" +
                    "    }\n" +
                    "}";


    public static void setClient(){
        if (client == null){

            DroidClientConfig config = new DroidClientConfig
                    .Builder("http://cmput301.softwareprocess.es:8080/")
                    .build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client  = (JestDroidClient)factory.getObject();
        }
    }

    public static Boolean DeleteCode(String... photoUUIDs) {
        String query;
        Log.d("xyz","Im here");
        if (photoUUIDs.length >= 1) {
            String combinedPhotoUUIDs = "";

            for (String photoID : photoUUIDs) {
                combinedPhotoUUIDs = combinedPhotoUUIDs.concat("" + photoID);
            }

            query =
                    "(\n "
                            + "   \"query:\": + (\n"
                            + "      \"match\": (\"UUID\" : \"" + combinedPhotoUUIDs + "\" }"
                            + "   }\n"
                            + "}";
        } else {
            query = matchAllquery;
        }
        Log.d("DeletePhoto", query);

        DeleteByQuery deleteQuery = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("Photo")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;

        while (tryCounter > 0){
            try {
                JestResult result = client.execute(deleteQuery);
                if (result.isSucceeded()) {
                    Log.d("DeletePhoto", "Successfully deleted photos");
                    return TRUE;
                }else{
                    Log.d("wtf","???");

                }

            } catch (IOException e1) {
                Log.d("DeletePhoto", "Try: " + tryCounter + "IOException");
            }
            tryCounter--;
        }
        return FALSE;
    }

    public static class DeletePhotosTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... photoUUIDs) {
            setClient();

            return DeleteCode(photoUUIDs);
        }
    }

    public static class AddPhotoTask extends AsyncTask<Photo,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Photo  ... photos) {
            setClient();

            if(photos.length < 1 ){
                return FALSE;
            }

            Photo photo = photos[0];
            Index index = new Index.Builder(photo)
                    .index(getIndex())
                    .type("Photo")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter >0){
                try {
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded()){
                        //add elastic id to object
                        photo.setElasticsearchID(result.getId());
                        Log.d("AddPhoto","Success! Added photo UUID: " + photo.getUUID());
                        return TRUE;
                    } else{
                        Log.d("AddPhoto", "Try: " + tryCounter
                                + ", Failed to add photo with UUID: " + photo.getUUID());
                    }
                } catch (IOException e1){
                    DeleteCode(photo.getUUID());
                    Log.d("AddPhoto","Try: " + tryCounter + "IOException");
                    e1.printStackTrace();
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class GetPhotoByPhotoUUIDTask extends AsyncTask<String,Void, Photo>{

        @Override
        protected Photo doInBackground(String... photoUUIDs) {
            setClient();
            Photo chosenPhoto = null;

            String query;

            //if Photolist is >1, choose first photo
            if (photoUUIDs.length >= 1) {
                String photoUUIDQuery = "\"" + photoUUIDs[0] + "\"";

                query =
                        "{\n"
                                + "     \"query\": { \n"
                                + "          \"match\": {\"UUID\": " + photoUUIDQuery + " }\n"
                                + "      }\n"
                                + "}";

            } else {
                query = matchAllquery;
            }

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Photo")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<Photo> photoList;
                        photoList = result.getSourceAsObjectList(Photo.class);

                        if (photoList.size() > 0) {
                            chosenPhoto = photoList.get(0);
                            Log.d("PhotoQueryByUUID", " Fetched Photo with UUID: " + chosenPhoto.getUUID());
                        }
                        return chosenPhoto;
                    }

                } catch (IOException e1) {
                    Log.d("PhotoQueryByUUID", "Try: " + tryCounter + ", IOException");
                }
                tryCounter--;
            }
            return chosenPhoto;
        }
    }

    public static class GetPhotosByRecordUUIDTask extends AsyncTask<String, Void, ArrayList<Photo>>{
        @Override
        protected ArrayList<Photo> doInBackground(String... recordUUIDs) {
            ArrayList<Photo> photoList = new ArrayList<>();
            String query;
            setClient();

            if (recordUUIDs.length < 1) {
                return photoList;
            }

            //query for matching recordUUID
            query =
                    "{\n"
                    + "     \"query\": {\n"
                    +"          \"match\":  { \"associatedRecordUUID\": \""+ recordUUIDs[0] + "\" }"
                    +"      }\n"
                    +"}";

            //create search builder
            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("Photo")
                    .setParameter(SIZE,1000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter >0){
                try{
                    JestResult result = client.execute(search);

                    if(result.isSucceeded()){
                        List<Photo> photos = result.getSourceAsObjectList(Photo.class);
                        photoList.addAll(photos);
                        for(Photo photo: photoList){
                            Log.d("GtPhtsByPtntRecrdIDTest"
                                    , "Fetched Photos(UUID): "+ photo.getUUID());
                        }
                        return photoList;
                    }
                }catch (IOException e1){
                    Log.d("GtPhtsByPtntRecrdIDTest", "IOEXCEPTION");
                }
                tryCounter--;
            }

            return photoList;
        }
    }
}
