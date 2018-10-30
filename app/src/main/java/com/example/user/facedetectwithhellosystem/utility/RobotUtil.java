package com.example.user.facedetectwithhellosystem.utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.asus.robotframework.API.RobotAPI;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.example.user.facedetectwithhellosystem.callback.RobotUtilCallback;

import org.json.JSONObject;

/**
 * Created by jenny on 2017/5/5.
 */

public class RobotUtil {

    private static RobotUtil uniqueInstance;
    public final int iSayHello = 11111, iRegisteredFace = 11112;

    private String TAG = "RobotUtil";
    private Context context;
    private Resources resources;
    private RobotUtilCallback robotUtilCallback;
    private RobotAPI robotAPI;

    public static synchronized RobotUtil getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new RobotUtil();
        }
        return uniqueInstance;
    }

    private RobotUtil() {
    }

    public void changeSetting(Context context, RobotUtilCallback robotUtilCallback) {
        this.context = context;
        this.resources = context.getResources();
        this.robotUtilCallback = robotUtilCallback;
        this.robotAPI = new RobotAPI(context, robotCallback);
    }

    private RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
            Log.d(TAG, "onResult cmd - " + cmd + "     serial - " + serial + "     err_code - " + err_code + "     result - " + result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
            Log.d(TAG, "onStateChange cmd - " + cmd + "     serial - " + serial + "     err_code - " + err_code + "     state - " + state);
//            if(state.toString().startsWith("5"))
//                Log.d(TAG, "SUCCEED");
            robotUtilCallback.RobotonStateChange(state.toString());
        }
    };

    private RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {
        @Override
        public void onFinishRegister() {
            Log.i(TAG, "robotListenCallback onFinishRegister()");
        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {
            Log.i(TAG, "robotListenCallback onVoiceDetect() - " + jsonObject.toString());
        }

        @Override
        public void onSpeakComplete(String s, String s1) {
            Log.i(TAG, "robotListenCallback onSpeakComplete() - s:" + s + "  s1 - " + s1);
        }

        @Override
        public void onEventUserUtterance(JSONObject _jsonObject) {
            Log.i(TAG, "robotListenCallback onEventUserUtterance() - " + _jsonObject.toString());

//            try {
//                JSONObject jsonObject = _jsonObject.getJSONObject("event_user_utterance");
//                JSONArray jA = new JSONArray(jsonObject.getString("user_utterance"));
//                JSONArray jsonArray = jA.getJSONObject(0).getJSONArray("result");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    String parseStr = jsonArray.getString(i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.d(TAG, "JSONException - " + e.toString());
//            }
//
//            robotUtilCallback.RobotonEventUserUtterance(0);
        }

        @Override
        public void onResult(JSONObject jsonObject) {
            Log.i(TAG, "robotListenCallback onResult() - " + jsonObject.toString());

        }

        @Override
        public void onRetry(JSONObject jsonObject) {
            Log.i(TAG, "robotListenCallback onRetry() - " + jsonObject.toString());

        }
    };

    public void Pause() {
        robotAPI.robot.unregisterListenCallback();
    }

    public void Resume() {
        if (robotListenCallback != null)
            robotAPI.robot.registerListenCallback(robotListenCallback);
        View decorView = ((Activity) context).getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);
    }

    public void Destroy() {
        robotAPI.release();
    }

    public void robotStop() {
        robotAPI.robot.stopSpeak();
    }

    public void robotSpeak(String _speak) {
        robotAPI.robot.speak(_speak);
    }

    public void robotExpression(RobotFace _face) {
        robotAPI.robot.setExpression(_face);
    }

}
