package com.payasyoustay.payasyoustay;

/**
 * Created by imarchand on 6/28/2015.
 */

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class RestClient {
    private String mServerHost = "45.33.81.168";
    private String mServerPort = "10110";
    private JSONObject mJsonPost;

    public RestClient(JSONObject jsonObject) {
        mJsonPost = jsonObject;
    }

    public RestClient() {
    }

    public String readJSONFeed(String urlString, Boolean post) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            if (post) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                //urlConnection.setDoInput(true);
                //urlConnection.setDoOutput(true);

                Log.d("RestClient", mJsonPost.toString());
                OutputStreamWriter out = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                out.write(mJsonPost.toString());
                out.flush();
                out.close();
                Log.d("RestClient", mJsonPost.toString());

            }
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("RestClient", "Connection failed : " + Integer.toString(statusCode));
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.d("RestClient", "Error " + e.toString());
        }
        String result = stringBuilder.toString();
        Log.d("RestClient", "JsonDoc: " + result);
        if (result.toLowerCase().equals("ok")) {
            result = "{\"result\":\"OK\"}";
        }
        return result;
    }

    protected JSONObject get(String path) {
        JSONObject json = new JSONObject();
        if (!mServerHost.isEmpty() && !mServerPort.isEmpty()) {
            try {
                String url = "http://" + mServerHost + ":" + mServerPort + path;
                Log.d("RestClient", "GET " + path);
                json = new JSONObject(readJSONFeed(url, false));
            } catch (Exception e) {
                Log.d("RestClient", "Error " + e.getMessage());
            }
        }
        return json;
    }
    protected JSONObject post(String path) {
        JSONObject json = new JSONObject();
        if (!mServerHost.isEmpty() && !mServerPort.isEmpty()) {
            try {
                String url = "http://" + mServerHost + ":" + mServerPort + path;
                Log.d("RestClient", "POST " + path);
                json = new JSONObject(readJSONFeed(url, true));
            } catch (Exception e) {
                Log.d("RestClient", "Error " + e.getMessage());
            }
        }
        return json;
    }
}
