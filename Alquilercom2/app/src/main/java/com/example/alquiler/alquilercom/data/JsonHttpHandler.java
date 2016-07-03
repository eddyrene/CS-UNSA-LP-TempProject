package com.example.alquiler.alquilercom.data;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonHttpHandler {
    static InputStream is = null;
    static JSONObject jObj = new JSONObject();
    static String json = "";

    public JSONObject getJSONfromUrl(String url) throws JSONException, IllegalStateException, IOException{

        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        StatusLine status = response.getStatusLine();


        int statusCode = status.getStatusCode();
        if (statusCode == 200) {
            Log.d("http-200", "status 200");
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            json = sb.toString();
            jObj = new JSONObject(json);
        }
        else {
            Log.e("Error status code: ", String.valueOf(statusCode));
        }

        return jObj;
    }

    public void postJSONfromUrl(String url, JSONObject data) throws ClientProtocolException, IOException{

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(data.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        client.execute(httpPost);

        return;
    }
}
*/
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class JsonHttpHandler {

    String charset = "UTF-8";
    HttpURLConnection conn;

    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    //static JSONObject jObj = new JSONObject();

    public JSONObject getJSONfromUrl(String url) throws JSONException, IllegalStateException, IOException, NullPointerException {
            // request method is GET

            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(false);

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setConnectTimeout(15000);

                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (NullPointerException e) {
            Log.e("JSON Parser", "Puntero nulo " + e.toString());
        }

        // return JSON Object
        return jObj;
    }

    public JSONObject postJSONfromUrl(String url, JSONObject data) throws JSONException, IllegalStateException, IOException{


            // request method is POST
            try {
                urlObj = new URL(url);
                DataOutputStream wr;
                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                wr = new DataOutputStream(conn.getOutputStream());
                Log.d("WARNINGGGGGGGGGG",data.toString());
                wr.writeBytes(data.toString());
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }



        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }
}

