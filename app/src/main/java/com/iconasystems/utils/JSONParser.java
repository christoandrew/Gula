package com.iconasystems.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private final Context context;
    HttpParams httpParams = new BasicHttpParams();

    // Set the timeout in milliseconds until a connection is established.
    // The default value is zero, that means the timeout is not used.
    int timeoutConnection = 300000;

    // Set the default socket timeout (SO_TIMEOUT)
    // in milliseconds which is the timeout for waiting for data.
    int timeoutSocket = 500000;


    // constructor
    public JSONParser(Context context) {
        this.context = context;
    }

    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.setParams(httpParams);

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.equals("GET")) {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.setParams(httpParams);

                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {

            Toast.makeText(context, "Server not responding please try again", Toast.LENGTH_LONG).show();
        } catch (ClientProtocolException e) {

            Toast.makeText(context, "Server not responding please try again", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            /* TODO Add context to instances of the JSONParser
            * Then now Toast the error
            */

            e.printStackTrace();

//            Toast.makeText(context, "Server not responding please try again", Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}