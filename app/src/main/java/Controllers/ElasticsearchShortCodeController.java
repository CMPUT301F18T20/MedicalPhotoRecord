package Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.ShortCode;

import java.io.IOException;
import java.util.List;

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

public class ElasticsearchShortCodeController extends ElasticsearchController {
    private static Boolean DeleteCode(String... ShortSecurityCodes) {
        String query;

        //delete the entry in the elasticsearch with this userID
        if (ShortSecurityCodes.length >= 1) {
            String shortSecurityCode = ShortSecurityCodes[0];

            //delete entries with shortSecurityCode
            query =
                    "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : { \"shortSecurityCode\" : \"" + shortSecurityCode + "\" }" +
                            "    }\n" +
                            "}";

        } else {
            //delete all entries
            query = matchAllquery;
        }

        Log.d("DeleteShortCode", query);

        DeleteByQuery deleteByQueryTask = new DeleteByQuery.Builder(query)
                .addIndex(getIndex())
                .addType("ShortCode")
                .build();

        int tryCounter = NumberOfElasticsearchRetries;
        while (tryCounter > 0) {
            try {
                JestResult result = client.execute(deleteByQueryTask);

                if (result.isSucceeded()) {
                    return TRUE;
                }

            } catch (IOException e) {
                Log.d("DeleteShortCode", "Try:" + tryCounter + ", IOEXCEPTION");
            }
            tryCounter--;
        }

        return FALSE;
    }

    public static class DeleteByShortSecurityCodeTask
            extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... ShortSecurityCodes) {
            setClient();
            return DeleteCode(ShortSecurityCodes);
        }
    }

    public static class getByShortSecurityCodeTask extends
            AsyncTask<String, Void, ShortCode>{
        @Override
        protected ShortCode doInBackground(String... ShortSecurityCodes) {

            setClient();
            ShortCode returnShortCode = null;
            String query;

            //if the StringsToMatch are not at least 1 entry just return null
            if (ShortSecurityCodes.length < 1) {
                return null;
            }

            //do a query for the first provided entry
            String shortSecurityCode = ShortSecurityCodes[0];

            //query for the shortSecurityCode
            query =
                    "{\n" +
                            "    \"query\": {\n" +
                            "        \"match\" : { \"shortSecurityCode\" : \"" + shortSecurityCode + "\" }" +
                            "    }\n" +
                            "}";

            Log.d("GetShortCode", query);

            Search search = new Search.Builder(query)
                    .addIndex(getIndex())
                    .addType("ShortCode")
                    .setParameter(SIZE,"10000")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    JestResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        List<ShortCode> ShortCodeList;
                        ShortCodeList =
                                result.getSourceAsObjectList(ShortCode.class);

                        //if we actually got a result, set it
                        if (ShortCodeList.size() > 0) {
                            returnShortCode = ShortCodeList.get(0);
                            Log.d("GetShortCode", "Fetched ShortCode with shortSecurityCode: " +
                                    returnShortCode.getShortSecurityCode());
                        }

                        return returnShortCode;
                    }

                } catch (IOException e) {
                    Log.d("GetShortCode", "Try:" + tryCounter + ", IOEXCEPTION");

                }
                tryCounter--;
            }

            return null;
        }
    }

    public static class AddShortCodeTask extends AsyncTask<ShortCode, Void, Boolean>{
        @Override
        protected Boolean doInBackground(ShortCode... ShortCodes){
            setClient();

            if (ShortCodes.length < 1) {
                return FALSE;
            }

            ShortCode ShortCode = ShortCodes[0];
            Index index=new Index.Builder(ShortCode)
                    .index(getIndex())
                    .type("ShortCode")
                    .build();

            int tryCounter = NumberOfElasticsearchRetries;
            while (tryCounter > 0) {
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //add id to current object
                        Log.d("AddShortCode", "Success, added short code for user: " +
                                ShortCode.getSecurityToken().getUserID() + " with code: " +
                                ShortCode.getShortSecurityCode());
                        return TRUE;
                    } else {
                        Log.d("AddShortCode", "Try:" + tryCounter +
                                ", Failed to add short code for user: " + ShortCode.getSecurityToken().getUserID());
                    }

                } catch (IOException e) {

                    DeleteCode(ShortCode.getShortSecurityCode());
                    Log.d("AddShortCode", "Try:" + tryCounter + ", IOEXCEPTION");
                }
                tryCounter--;
            }
            return FALSE;
        }
    }

}
