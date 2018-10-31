package com.example.user.facedetectwithhellosystem.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.user.facedetectwithhellosystem.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by jenny on 2017/4/12.
 */

public class AllNetwork {

    private String TAG = "AllNetwork";

    public int Method_Get = 101;
    public int Method_Post = 102;
    public int Method_Delete = 103;
    public int Method_Put = 104;

    //region Method Name
//    public String Http_FaceFF = "http://118.163.149.163:23760/";
    public String Http_Face = "http://www.fongfuapi.com:23760/";
    public String PutFace = "putFaces";
    public String SearchFaces = "searchFaces";
    public String DeleteFaces = "deleteFaces";
    public String faceRecognition = "faceRecognition";

    public String HTTP_FUNCS = "https://us-central1-babyplusm-f3586.cloudfunctions.net/";
    public String Pwd2SID = "pwd2sid";
    public String UTCTime = "getUTCTime";
    //endregion

    private Resources resources;

    public AllNetwork(Context context) {
        resources = context.getResources();
    }

    private String getHttpContent(int _method, String url, String content, Object header) {
        String errorMessage = "";
        StringBuilder result = new StringBuilder("");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            if (_method == Method_Get) {
                conn.setRequestMethod("GET");
                conn.setDoOutput(false);
            } else if (_method == Method_Post) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            } else if (_method == Method_Delete) {
                conn.setRequestMethod("DELETE");
                conn.setDoOutput(true);
            } else if (_method == Method_Put) {
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
            }


            if (header != null) {
                for (Map.Entry<String, String> entry : ((Map<String, String>) header).entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (content != null) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(content);
                bw.flush();
                bw.close();
            }

            conn.connect();

            int rc = conn.getResponseCode();
            BufferedReader reader = null;

            if (rc == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
            } else {
                reader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream(), "utf-8"));
            }

            if (reader != null) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line + "\n");
                }
                reader.close();
            }

            return result.toString() + ";;;" + String.valueOf(rc);
        } catch (Exception e) {
            Log.d(TAG, "Exception - " + e.toString());
            errorMessage = e.toString();
            e.printStackTrace();
        }

        return errorMessage + ";;;" + resources.getString(R.string.error);
    }

    public String setPutFace(String _url) {
        return getHttpContent(Method_Put, _url, null, null);
    }

    public String setSearchFace(String _url) {
        return getHttpContent(Method_Get, _url, null, null);
    }

    public String setDeleteFace(String _url) {
        return getHttpContent(Method_Delete, _url, null, null);
    }

    public String setFaceRecognition(String _url) {
        return getHttpContent(Method_Get, _url, null, null);
    }

    public String getPwd2SID(String url) {
        return getHttpContent(Method_Post, url, null, null);
    }

    public String getUTCTime(String url) {
        return getHttpContent(Method_Post, url, null, null);
    }

}
