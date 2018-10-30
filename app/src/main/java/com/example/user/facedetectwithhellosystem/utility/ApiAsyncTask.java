package com.example.user.facedetectwithhellosystem.utility;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.callback.ApiAsyncTaskCallback;


/**
 * Created by jenny on 2017/4/12.
 */

public class ApiAsyncTask extends AsyncTask<Object, Void, String> {

    private String TAG = "ApiAsyncTask";

    private Context context;
    private AllNetwork allNetwork;
    private Resources resources;
    private ApiAsyncTaskCallback apiAsyncTaskCallback;
    private String kind;

    private String _count;

    public ApiAsyncTask(Context context, AllNetwork allNetwork, ApiAsyncTaskCallback apiAsyncTaskCallback) {
        this.context = context;
        this.allNetwork = allNetwork;
        resources = context.getResources();
        this.apiAsyncTaskCallback = apiAsyncTaskCallback;
    }

    @Override
    protected String doInBackground(Object... params) {
        kind = (String) params[0];
        String errorMessage = "";
        try {
            if (kind.equals(allNetwork.PutFace)) {
                return allNetwork.setPutFace((String) params[1]);
            } else if (kind.equals(allNetwork.SearchFaces)) {
                _count = (String) params[2];
                return allNetwork.setSearchFace((String) params[1]);
            } else if (kind.equals(allNetwork.DeleteFaces)) {
                return allNetwork.setDeleteFace((String) params[1]);
            } else if (kind.equals(allNetwork.faceRecognition)) {
                _count = (String) params[2];
                return allNetwork.setFaceRecognition((String) params[1]);
            } else if (kind.equals(allNetwork.Pwd2SID)) {
                return allNetwork.getPwd2SID((String) params[1]);
            } else if (kind.equals(allNetwork.UTCTime)) {
                return allNetwork.getUTCTime((String) params[1]);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception - " + e.toString());
            errorMessage = e.toString();
        }
        return errorMessage + ";;;" + resources.getString(R.string.Error);
    }

    @Override
    protected void onPostExecute(String result) {
        if (kind.equals(allNetwork.SearchFaces) || kind.equals(allNetwork.faceRecognition))
            result = result + ";;;" + _count;
        if (apiAsyncTaskCallback != null)
            apiAsyncTaskCallback.onApiAsyncTaskFinished(kind, result);
    }
}
