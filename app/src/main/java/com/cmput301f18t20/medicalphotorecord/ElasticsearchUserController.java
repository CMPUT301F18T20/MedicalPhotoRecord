package com.cmput301f18t20.medicalphotorecord;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;

/* TODO CREDIT we will need to credit this to the lonelyTwitter lab guy */
public class ElasticsearchUserController {

    static JestDroidClient client = null;

    public static void setClient(){
        if(client == null){

            DroidClientConfig config = new DroidClientConfig
                    .Builder("http://cmput301.softwareprocess.es:8080/")
                    .build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client=(JestDroidClient) factory.getObject();
        }
    }

    public static class GetUserTask extends AsyncTask<String, Void, ArrayList<User>>{
        @Override
        //String instead of void to implement search
        protected ArrayList<User> doInBackground(String... params) {
            setClient();
            ArrayList<User> User=new ArrayList<User>();
            String query = "{\n" +
                    //"    \"id\": \"myTemplateId\"," +
                    "    \"params\": {\n" +
                    "        \"query_string\" : \"test\"" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder("")//query)
                    .addIndex("cmput301f18t20")
                    .addType("User")
                    .build();
            try {
                JestResult result=client.execute(search);

                if(result.isSucceeded()){
                    List<User> UserList;
                    UserList=result.getSourceAsObjectList(User.class);
                    User.addAll(UserList);
                }

            }catch(IOException e){}

            return User;
        }
    }

    public static class AddUserTask extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User... params){
            setClient();

            User User = params[0];
            Index index=new Index.Builder(User)
                    .index("cmput301f18t20")
                    .type("User")
                    .build();

            try {
                DocumentResult result = client.execute(index);
                if(result.isSucceeded()){
                }

            }catch(IOException e){
                //do something here
                Log.d("Hello", "IOEXCEPTION");
            }
            return null;

        }
    }
}
