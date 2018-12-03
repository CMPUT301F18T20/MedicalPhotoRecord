package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.GeoLocation;
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

public class ElasticsearchGeoLocationController extends ElasticsearchController {

    public static Boolean DeleteCode(String... geoUUIDs) {
        String query;
        if (geoUUIDs.length >= 1) {
            String combinedgeoUUIDs = "";

            for (String geoID : geoUUIDs) {
                combinedgeoUUIDs = combinedgeoUUIDs.concat("" + geoID);
            }

            query =
                    "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : { \"UUID\" : \"" + combinedgeoUUIDs + "\" }" +
                            "    }\n" +
                            "}";
        } else {
            query = matchAllquery;
        }
        Log.d("DeleteGeo", query);

        DeleteByQuery deleteQuery = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("GeoLocation")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;

        while (tryCounter > 0){
            try {
                JestResult result = client.execute(deleteQuery);
                if (result.isSucceeded()) {
                    Log.d("DeleteGeo", "Successfully deleted geos");
                    return TRUE;
                }else{
                    Log.d("DeleteGeo","Failed to delete geos");

                }

            } catch (IOException e1) {
                Log.d("DeleteGeo", "Try: " + tryCounter + "IOException");
            }
            tryCounter--;
        }
        return FALSE;
    }

    public static class DeleteGeosTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... geoUUIDs) {
            setClient();

            return DeleteCode(geoUUIDs);
        }
    }

    public static class AddGeoTask extends AsyncTask<GeoLocation,Void,Boolean>{
        @Override
        protected Boolean doInBackground(GeoLocation  ... geos) {
            setClient();

            if(geos.length < 1 ){
                return FALSE;
            }

            GeoLocation geo = geos[0];
            Index index = new Index.Builder(geo)
                    .index(getIndex())
                    .id(geo.getUUID())
                    .type("GeoLocation")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter >0){
                try {
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded()){
                        Log.d("AddGeo","Success! Added geo UUID: " + geo.getUUID());
                        return TRUE;
                    } else{
                        Log.d("AddGeo", "Try: " + tryCounter
                                + ", Failed to add geo with UUID: " + geo.getUUID());
                    }
                } catch (IOException e1){
                    DeleteCode(geo.getUUID());
                    Log.d("AddGeo","Try: " + tryCounter + "IOException");
                    e1.printStackTrace();
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

    public static class GetGeoByGeoRecordUUIDTask extends AsyncTask<String,Void, GeoLocation>{

        @Override
        protected GeoLocation doInBackground(String... recordUUIDs) {
            setClient();
            GeoLocation chosenGeo = null;

            String query;

            //if Geolist is >1, choose first geo
            if (recordUUIDs.length >= 1) {
                String recordUUIDQuery = "\"" + recordUUIDs[0] + "\"";

                query =
                        "{\n"
                                + "     \"query\": { \n"
                                + "          \"match\": {\"recordUUID\": " + recordUUIDQuery + " }\n"
                                + "      }\n"
                                + "}";

            } else {
                query = matchAllquery;
            }

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("GeoLocation")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<GeoLocation> geoList;
                        geoList = result.getSourceAsObjectList(GeoLocation.class);

                        if (geoList.size() > 0) {
                            chosenGeo = geoList.get(0);
                            Log.d("GeoQueryByUUID", " Fetched Geo with UUID: " + chosenGeo.getUUID());
                        }
                        return chosenGeo;
                    }

                } catch (IOException e1) {
                    Log.d("GeoQueryByUUID", "Try: " + tryCounter + ", IOException");
                }
                tryCounter--;
            }
            return chosenGeo;
        }
    }

    public static class GetGeosByProblemUUIDTask extends AsyncTask<String, Void, ArrayList<GeoLocation>>{
        @Override
        protected ArrayList<GeoLocation> doInBackground(String... problemUUIDs) {
            ArrayList<GeoLocation> geoList = new ArrayList<>();
            String query;
            setClient();

            if (problemUUIDs.length < 1) {
                return geoList;
            }

            //query for matching problemUUID
            query =
                    "{\n"
                            + "     \"query\": {\n"
                            +"          \"match\":  { \"problemUUID\": \""+ problemUUIDs[0] + "\" }"
                            +"      }\n"
                            +"}";

            //create search builder
            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("GeoLocation")
                    .setParameter(SIZE,1000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter >0){
                try{
                    JestResult result = client.execute(search);

                    if(result.isSucceeded()){
                        List<GeoLocation> geos = result.getSourceAsObjectList(GeoLocation.class);
                        geoList.addAll(geos);
                        for(GeoLocation geo: geoList){
                            Log.d("GtGeosByPrblmIDTest"
                                    , "Fetched Geos(UUID): "+ geo.getUUID());
                        }
                        return geoList;
                    }
                }catch (IOException e1){
                    Log.d("GtGeosByPrblmIDTest", "IOEXCEPTION");
                }
                tryCounter--;
            }

            return geoList;
        }
    }

    /*public static class GetGeosByRecordUUIDTask extends AsyncTask<String, Void, ArrayList<GeoLocation>>{
        @Override
        protected ArrayList<GeoLocation> doInBackground(String... recordUUIDs) {
            ArrayList<GeoLocation> geoList = new ArrayList<>();
            String query;
            setClient();

            if (recordUUIDs.length < 1) {
                return geoList;
            }

            //query for matching recordUUID
            query =
                    "{\n"
                            + "     \"query\": {\n"
                            +"          \"match\":  { \"recordUUID\": \""+ recordUUIDs[0] + "\" }"
                            +"      }\n"
                            +"}";

            //create search builder
            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("GeoLocation")
                    .setParameter(SIZE,1000)
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter >0){
                try{
                    JestResult result = client.execute(search);

                    if(result.isSucceeded()){
                        List<GeoLocation> photos = result.getSourceAsObjectList(GeoLocation.class);
                        geoList.addAll(photos);
                        for(GeoLocation geo: geoList){
                            Log.d("GtGeosByPtntRecrdIDTest"
                                    , "Fetched Geos(UUID): "+ geo.getUUID());
                        }
                        return geoList;
                    }
                }catch (IOException e1){
                    Log.d("GtPhtsByPtntRecrdIDTest", "IOEXCEPTION");
                }
                tryCounter--;
            }

            return geoList;
        }
    }*/
}
