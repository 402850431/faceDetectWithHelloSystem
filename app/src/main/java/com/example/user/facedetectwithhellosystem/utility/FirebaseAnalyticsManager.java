package com.example.user.facedetectwithhellosystem.utility;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.example.user.facedetectwithhellosystem.R;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by jenny on 2017/6/9.
 */

public class FirebaseAnalyticsManager {

    private String TAG = "FAManager";
    private FirebaseAnalytics firebaseAnalytics;
    private Resources resources;

    private static int type_base = 0;
    public static String type_log = "type_log";
    public static String type_error = "type_error";
    public static String type_openpage = "type_openpage";
    public static String type_openfunction = "type_openfunction";
    public static String type_faceDetectionResult = "type_faceDetectionResult";
    public static String type_faceDetectionConsumionTime = "type_faceDetectionConsumionTime";
    public static String type_asyncFaceRecognitionRequestTime = "type_asyncFRRequestTime"; //FR -> FaceRecognition
    public FirebaseAnalyticsManager(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        resources = context.getResources();
    }

    public void putErrorMessage(String event, String message) {
        this.logErrorEvent(event,message);
    }


    public void logErrorEvent(String event, String message) {

        Log.d(TAG, "setErrorMessage() : event - " + event + "\t\tmessage - " + message);
        Bundle bundle = new Bundle();
        bundle.putString(resources.getString(R.string.Event), event);
        bundle.putString(resources.getString(R.string.Message), message);
        firebaseAnalytics.logEvent(this.type_error, bundle);

    }


    public void logPage(String pageName) {

        //Log.d(TAG, "setErrorMessage() : event - " + event + "\t\tmessage - " + message);
        Bundle bundle = new Bundle();
        bundle.putString(resources.getString(R.string.Event), this.type_openpage);
        bundle.putString(resources.getString(R.string.Message), pageName);
        firebaseAnalytics.logEvent(this.type_openpage, bundle);

    }

    public void logFaceDetectionResult(int faces) {
        //Log.d(TAG, "setErrorMessage() : event - " + event + "\t\tmessage - " + message);
        Bundle bundle = new Bundle();
        bundle.putString(resources.getString(R.string.Event), this.type_faceDetectionResult);
        bundle.putString(resources.getString(R.string.Message), String.valueOf(faces));
        firebaseAnalytics.logEvent(this.type_faceDetectionResult, bundle);


    }



    public void logFaceRecognitionAPITime(long time) {
        Bundle bundle = new Bundle();
        bundle.putString(resources.getString(R.string.Event), this.type_asyncFaceRecognitionRequestTime);
        bundle.putString(resources.getString(R.string.Message), String.valueOf(time));
        firebaseAnalytics.logEvent(this.type_asyncFaceRecognitionRequestTime, bundle);


    }


    public void logFaceDetectionTime(long time) {
        Bundle bundle = new Bundle();
        bundle.putString(resources.getString(R.string.Event), this.type_faceDetectionConsumionTime);
        bundle.putString(resources.getString(R.string.Message), String.valueOf(time));
        firebaseAnalytics.logEvent(this.type_faceDetectionConsumionTime, bundle);
    }



































}
