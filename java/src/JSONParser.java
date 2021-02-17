

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;


public class JSONParser {

        final String TAG = "JsonParser.java";

        private static InputStream is = null;
        private static JSONObject jObj = null;

        private static String json = "";

        public JSONObject getJSONFromUrl(String url) {

            // make connection
            try {

                URL oracle = new URL(url);
                URLConnection yc = oracle.openConnection();
                yc.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                is = yc.getInputStream();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n"); // appending all data to StringBuilder object
                }
                is.close();
                json = sb.toString(); // assigning toString value of StringBuilder object

            } catch (Exception e) {
                System.out.println( "Error converting result " + e.toString());
            }

            // creating JSONObject with String object argument;
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                System.out.println("Error parsing data " + e.toString());
            }
            // return JSON String
            return jObj;
        }


    }
