package com.example.user.facedetectwithhellosystem.mibo;

import android.content.Context;
import android.util.Log;

import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.VoiceEventListener;


public class Mibo {
    private NuwaRobotAPI mRobot;
    public boolean isReady = false;
    private Context context;
    private IClientId id;

    public Mibo(Context context) {
        // Please turn on WiFi first to get auth token first otherwise it will be failed to init.
        // ID name: Assigned by Nuwa
        this.context = context;
        id = new IClientId("your_app_package_name");
        mRobot = new NuwaRobotAPI(context, id);
        setMiboListener(false, null);
        setMiboCallBack();
        isReady = mRobot.isKiWiServiceReady();
    }

    private void setMiboListener(final boolean isNeedSpeak, final String word){
        mRobot.registerRobotEventListener(new RobotEventListener() {
            @Override
            public void onWikiServiceStart() {
                // Once mRobot is created, you will receive a callback
                isReady = true;
                if (isNeedSpeak) {
                    speak(word);
                }
            }

            @Override
            public void onWikiServiceStop() {
                Log.e(">>>", "onWikiServiceStop");
            }

            @Override
            public void onWikiServiceCrash() {
                Log.e(">>>", "onWikiServiceCrash");
            }

            @Override
            public void onWikiServiceRecovery() {
                Log.e(">>>", "onWikiServiceRecovery");
            }

            @Override
            public void onStartOfMotionPlay(String s) {

            }

            @Override
            public void onPauseOfMotionPlay(String s) {

            }

            @Override
            public void onStopOfMotionPlay(String s) {

            }

            @Override
            public void onCompleteOfMotionPlay(String s) {

            }

            @Override
            public void onPlayBackOfMotionPlay(String s) {

            }

            @Override
            public void onErrorOfMotionPlay(int i) {

            }

            @Override
            public void onPrepareMotion(boolean b, String s, float v) {

            }

            @Override
            public void onCameraOfMotionPlay(String s) {

            }

            @Override
            public void onGetCameraPose(float v, float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, float v9, float v10, float v11) {

            }

            @Override
            public void onTouchEvent(int type, int touch) {

            }

            @Override
            public void onPIREvent(int i) {

            }

            @Override
            public void onTap(int i) {

            }

            @Override
            public void onLongPress(int i) {

            }

            @Override
            public void onWindowSurfaceReady() {

            }

            @Override
            public void onWindowSurfaceDestroy() {

            }

            @Override
            public void onTouchEyes(int i, int i1) {

            }

            @Override
            public void onFaceSpeaker(float v) {

            }

            @Override
            public void onActionEvent(int i, int i1) {

            }

            @Override
            public void onDropSensorEvent(int i) {

            }

            @Override
            public void onMotorErrorEvent(int i, int i1) {

            }
        });

        mRobot.registerVoiceEventListener(new VoiceEventListener() {
            @Override
            public void onWakeup(boolean isError, String score, float v) {
                Log.e(">>>", "onWakeup " + "isError = " + isError);
                // When Robot hear "小丹小丹"，Kiwi agent will receive a callback onWakeup() of VoiceEventListener once onWakeup
                // score: a json string given by IFly engine
                // isError: if IFLY engine works normally
                // Ex: {"eos":3870,"score":80,"bos":3270,"sst":"wakeup","id":0}
                // score: confidence value
                // id: which wakeup command is used

                //id 0: 哈搂米宝
                //id 1: 奇异宝贝
                //id 2: 停止不要动
                //id 3: 叮当叮当
                //id 4: 小智機器人
            }

            @Override
            public void onTTSComplete(boolean isError) {
                // receive callback onTTSComplete(boolean isError) of VoiceEventListener
            }

            @Override
            public void onSpeechRecognizeComplete(boolean b, ResultType resultType, String s) {

            }

            @Override
            public void onSpeech2TextComplete(boolean b, String s) {

            }

            @Override
            public void onMixUnderstandComplete(boolean b, ResultType resultType, String s) {

            }

            @Override
            public void onSpeechState(ListenType listenType, SpeechState speechState) {

            }

            @Override
            public void onSpeakState(SpeakType speakType, SpeakState speakState) {

            }

            @Override
            public void onGrammarState(boolean b, String s) {

            }

            @Override
            public void onListenVolumeChanged(ListenType listenType, int i) {

            }
        });
    }

    private void setMiboCallBack(){

    }

    public void wakeUpBySound(){
        mRobot.startWakeUp(true); // When Robot hear "小丹小丹"
    }

    public void stopListen() {
        mRobot.stopListen();
    }

    public void speak(final String word) {
        if (isReady) {
            mRobot.startTTS(word);
        } else {
            setMiboListener(true, word);
        }
    }

    public void stopSpeak() {
        mRobot.stopTTS();
    }
    public void release() {
        // release Nuwa SDK resource while App closed.(or Activity in pause/destroy state)
        mRobot.release();
    }

    public void startSoundTest() {

        mRobot.registerRobotEventListener(new RobotEventListener() {
            @Override
            public void onWikiServiceStart() {
//                mRobot.startTTS("嗨你好!阿量辛苦你了");
            }

            @Override
            public void onWikiServiceStop() {

            }

            @Override
            public void onWikiServiceCrash() {

            }

            @Override
            public void onWikiServiceRecovery() {

            }

            @Override
            public void onStartOfMotionPlay(String s) {

            }

            @Override
            public void onPauseOfMotionPlay(String s) {

            }

            @Override
            public void onStopOfMotionPlay(String s) {

            }

            @Override
            public void onCompleteOfMotionPlay(String s) {

            }

            @Override
            public void onPlayBackOfMotionPlay(String s) {

            }

            @Override
            public void onErrorOfMotionPlay(int i) {

            }

            @Override
            public void onPrepareMotion(boolean b, String s, float v) {

            }

            @Override
            public void onCameraOfMotionPlay(String s) {

            }

            @Override
            public void onGetCameraPose(float v, float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, float v9, float v10, float v11) {

            }

            @Override
            public void onTouchEvent(int i, int i1) {

            }

            @Override
            public void onPIREvent(int i) {

            }

            @Override
            public void onTap(int i) {

            }

            @Override
            public void onLongPress(int i) {

            }

            @Override
            public void onWindowSurfaceReady() {

            }

            @Override
            public void onWindowSurfaceDestroy() {

            }

            @Override
            public void onTouchEyes(int i, int i1) {

            }

            @Override
            public void onFaceSpeaker(float v) {

            }

            @Override
            public void onActionEvent(int i, int i1) {

            }

            @Override
            public void onDropSensorEvent(int i) {

            }

            @Override
            public void onMotorErrorEvent(int i, int i1) {

            }
        });
    }

    }
