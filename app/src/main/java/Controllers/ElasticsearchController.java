package Controllers;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

public class ElasticsearchController {

    static JestDroidClient client = null;

    public final static String matchAllquery =
            "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_all\" : {}" +
                    "    }\n" +
                    "}";

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
}
