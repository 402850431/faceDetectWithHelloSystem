package com.example.user.facedetectwithhellosystem.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.asus.robotframework.API.RobotFace;
import com.crashlytics.android.Crashlytics;
import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.callback.ApiAsyncTaskCallback;
import com.example.user.facedetectwithhellosystem.callback.RobotUtilCallback;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.mibo.Mibo;
import com.example.user.facedetectwithhellosystem.utility.AllNetwork;
import com.example.user.facedetectwithhellosystem.utility.ApiAsyncTask;
import com.example.user.facedetectwithhellosystem.utility.FirebaseAnalyticsManager;
import com.example.user.facedetectwithhellosystem.utility.RobotUtil;
import com.example.user.facedetectwithhellosystem.utility.TransformUtil;
import com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class FaceDetectNoAnalysisActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, ApiAsyncTaskCallback, RobotUtilCallback {

    private static final String TAG = "FF::Activity";
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private CascadeClassifier mJavaDetector;
    private final String FaceData = "FaceData", Base64Data = "Base64Data";
    //from privious view
    private boolean isZenbo = true;
    private boolean isSearchFace;
    private boolean isSayHelloActivity = false;
    private String mLastSpeakName;
    private String mLastSpeakTime;
    private CameraBridgeViewBase mOpenCvCameraView;
    public AllNetwork allNetwork;
    private String[] mornings, afternoons, noons, nights;
    private String[] mDetectorName;
    private RobotUtil robotUtil;
    private Mibo mibo;
    private ArrayList<Bitmap> faceArray;
    MySQLite mySQLite;

    private final String SchoolData = "SchoolData", SID = "sID";
    private Resources resources;
    private FirebaseAnalyticsManager firebaseAnalyticsManager;
    public String sID;

    private boolean isSpeakFinish = true;
    private Bundle bundle;

    private long startAsyncRequestTime;
    private int function_count = 0;

    public TransformUtil transformUtil;

    //isDebug
    private boolean isDebug = true;
    private final int debugView = 11111;
    private TextView debug_text;
    SharedPreferences spf;
    ArrayList<Word> wordsList;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_cv2_face_detect);
        try {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                isSearchFace = bundle.getBoolean("isSearchFace", true);
                isSayHelloActivity = bundle.getBoolean("isSayHelloActivity", false);
            } else {
                isSearchFace = true;
            }
        } catch (Exception e) {
            isSearchFace = true;
        }

        init();
    }

    private void init() {
        mySQLite = new MySQLite(this);
        spf = PreferenceManager.getDefaultSharedPreferences(this);

        debug_text = findViewById(R.id.debug_text);
        if (isDebug)
            debug_text.setVisibility(View.VISIBLE);

        mOpenCvCameraView = findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        findViewById(R.id.backPressImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        allNetwork = new AllNetwork(this);

        if (isZenbo) {
            robotUtil = RobotUtil.getInstance();
            robotUtil.changeSetting(this, this);
            mibo = new Mibo(this);
        }
        mLastSpeakTime = "1496859000";

        resources = getResources();
        firebaseAnalyticsManager = new FirebaseAnalyticsManager(this);
        firebaseAnalyticsManager.logPage(this.getLocalClassName());

        SharedPreferences sharedPreferences = getSharedPreferences(SchoolData, 0);
        sID = sharedPreferences.getString(SID, "");

        mornings = resources.getStringArray(R.array.morning_arrary);
        afternoons = resources.getStringArray(R.array.afternoon_arrary);
        noons = resources.getStringArray(R.array.noon_array);
        nights = resources.getStringArray(R.array.night_arrary);
        faceArray = new ArrayList<Bitmap>();

        transformUtil = new TransformUtil();

    }

    @Override
    public void onPause() {
        super.onPause();
        isSpeakFinish = false;
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }


        if (isZenbo) {
            robotUtil.changeSetting(this, this);
            robotUtil.Resume();
            if (isSearchFace) {
                robotUtil.robotExpression(RobotFace.HAPPY);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        robotUtil.robotStop();
    }

    //opencv life cycle
    public void onCameraViewStarted(int width, int height) {


    }


    //opencv life cycle
    public void onCameraViewStopped() {


    }

    int mFaceProcessCounter = 0;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return faceDetection(inputFrame);
    }

    //region Debug Handler
    private Handler debugHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case debugView:
                    debug_text.setText((String) msg.obj);
                    break;
            }
        }
    };
    //endregion

    private Mat faceDetection(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        boolean isOverOnePerson = false;

        function_count++;
        int _count = function_count;
        Log.i(TAG, "FunctestTime " + _count + " - faceDetection() start");

        Mat rgbFrame = inputFrame.rgba();
        Mat grayFrame = new Mat();
        int resolutionScale = 2;
        Imgproc.resize(inputFrame.gray(), grayFrame, new Size(rgbFrame.width() / resolutionScale, rgbFrame.height() / resolutionScale));
        MatOfRect faces = new MatOfRect();

        //mJavaDetector.detectMultiScale(grayFrame, faces, 5, 2, 2, new Size(50,50), new Size(500, 500));

//        Log.i(TAG, "FunctestTime " + _count + " - faceDetection() start");
        //gray frame resolution is haft of original frame size

        long startTime = System.currentTimeMillis();
        mJavaDetector.detectMultiScale(grayFrame, faces, 1.3, 2, 2, new Size(50, 50), new Size(250, 250));
        firebaseAnalyticsManager.logFaceRecognitionAPITime(System.currentTimeMillis() - startTime);

        Log.e(TAG, "mJavaDetector time diff - " + String.valueOf(System.currentTimeMillis() - startTime));
//        Log.i(TAG, "FunctestTime " + _count + " - faceDetection() over");


//        Log.d(TAG,"faces: " + String.valueOf(faces.toArray().length));


        Rect[] facesArray = faces.toArray();
        firebaseAnalyticsManager.logFaceDetectionResult(facesArray.length);
        Log.e(TAG, "faceArray - " + facesArray.length);
        if (isDebug) {
            String result = "";
//            if (facesArray.length > 1) {
//                isOverOnePerson = true;
//                result = resources.getString(R.string.TooManyPeople);
//            }

            Message msg = new Message();
            msg.what = debugView;
            msg.obj = result;
            debugHandler.sendMessage(msg);
        }

        if (facesArray.length > 0) {
            Point pt1 = facesArray[0].tl();
            pt1.x = pt1.x * resolutionScale;
            pt1.y = pt1.y * resolutionScale;

            Point pt2 = facesArray[0].br();
            pt2.x = pt2.x * resolutionScale;
            pt2.y = pt2.y * resolutionScale;


            Rect faceRect = new Rect(pt1, pt2);


            int s = Math.min(faceRect.width, faceRect.height);
            int exp = (int) (s * 0.35);
            s = s + exp * 2;
            Rect squareFaceRect = new Rect((int) (pt1.x - exp), (int) (pt1.y - exp), s, s);

            Log.d(TAG, squareFaceRect.toString());

            //判斷是否超出範圍
            if (squareFaceRect.x < 0 || squareFaceRect.y < 0 ||
                    (squareFaceRect.x + squareFaceRect.width) > rgbFrame.width() ||
                    (squareFaceRect.y + squareFaceRect.height) > rgbFrame.height()) {
                Log.d(TAG, "out of range!!!");
                return rgbFrame;
            }

            Mat face = new Mat(rgbFrame, squareFaceRect);
            Mat face150 = new Mat();
            int faceWH = 150;
            Log.d(TAG, "face W/D: " + String.valueOf(squareFaceRect.width) + " " + String.valueOf(squareFaceRect.height));

            Imgproc.resize(face, face150, new Size(faceWH, faceWH));


            Imgproc.rectangle(rgbFrame, squareFaceRect.tl(), squareFaceRect.br(), FACE_RECT_COLOR, 3);

            //FaceDataManager.getInstance().addFace(face150);
            //在fps ~= 30的情況下 每秒五次送到到後端做辨識.
            mFaceProcessCounter = mFaceProcessCounter + 1;
            if (mFaceProcessCounter > 3) {
                mFaceProcessCounter = 0;
//            if (true) {
                //擷取出人臉(Bitmap)
                Bitmap face150BMP = Bitmap.createBitmap(faceWH, faceWH, Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(face150, face150BMP);

                if (isSearchFace) {
                    asyncSearchFaceRequest(transformUtil.bitmapToBase64(face150BMP), _count);
                    //face to register
                } else {
                    if (faceArray.size() < 16) {
                        try {
                            faceArray.add(face150BMP);
//                                progress.setProgress((faceArray.size() - 1) * 6);
                        } catch (Exception e) {
                            Log.e(TAG, "addFace Exception e - " + e.toString());
                        }
                    } else {
//                        stopTakePictureThread();
//                        turnFaceListActivity();
                    }


                }
            }

        }

        Log.i(TAG, "FunctestTime " + _count + " - faceDetection() over");
        return rgbFrame;

    }

/*
    private void turnFaceListActivity() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String _base64Array = "";
        for (Bitmap _bitmap : faceArray) {
            _base64Array += transformUtil.bitmapToBase64(_bitmap) + ";";
        }
        SharedPreferences sharedPreferences = getSharedPreferences(FaceData, 0);
        sharedPreferences.edit()
                .putString(Base64Data, _base64Array)
                .commit();

        Intent intent = new Intent();
        intent.setClass(FaceDetectNoAnalysisActivity.this, FaceListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
*/

    public void asyncSearchFaceRequest(String base64Data, int _count) {
        Log.i(TAG, "FunctestTime " + _count + " - asyncSearchFaceRequest() start");

        Map<String, String> dataToSend = new HashMap<>();
        dataToSend.put("face_image", base64Data);
        dataToSend.put("k", "9");
        dataToSend.put("schoolid", sID);
        startAsyncRequestTime = System.currentTimeMillis();
        new ApiAsyncTask(this, allNetwork, this).execute(allNetwork.faceRecognition, allNetwork.Http_Face + allNetwork.faceRecognition + "?" + transformUtil.getEncodedData(dataToSend), String.valueOf(_count));


        Log.i(TAG, "FunctestTime " + _count + " - asyncSearchFaceRequest() over");
    }

    private String getCurrentTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000 + 28800;
        return tsLong.toString();
    }

    @Override
    public void onApiAsyncTaskFinished(String kind, String result) {
        Log.i(TAG, "kind - " + kind + "\nresult - " + result);
        firebaseAnalyticsManager.logFaceRecognitionAPITime(System.currentTimeMillis() - startAsyncRequestTime);
        Log.e(TAG, "onApiAsyncTaskFinished time diff - " + String.valueOf(System.currentTimeMillis() - startAsyncRequestTime));
        startAsyncRequestTime = 0;


        int _count = -1;
        String[] _rescc = result.split(";;;");
        if (_rescc.length == 3)
            _count = Integer.parseInt(_rescc[2]);
        Log.i(TAG, "FunctestTime " + _count + " - onApiAsyncTaskFinished() start");

        if (isSearchFace) {
//            Log.d("ffapi", "got response!!");
//            Log.d("ffapi", "kind - " + kind + "\nresult - " + result);
            String[] _res = result.split(";;;");

            if (_res.length == 3) {
                if (_res[1].equals("200")) {

                    try {
                        JSONObject jsonObject = new JSONObject(_res[0]);
                        String status = jsonObject.getString(resources.getString(R.string.FaceAPIStatus));
                        if (status.equals(resources.getString(R.string.FaceAPIOK))) {
                            //region faceRecognition
                            String pID = jsonObject.getString("pid");
//                            String finalName = FirebaseDataManager.getInstance(this).getName(pID);
                            String finalName = "";
//                            Log.i(TAG, "finalName - " + finalName);
//                            if (!finalName.equals("")) {
                                sayHi(pID, finalName, _count);
//                            }

                        } else if (status.equals(resources.getString(R.string.Warning))) {
                            firebaseAnalyticsManager.putErrorMessage(kind, jsonObject.getString(resources.getString(R.string.FaceAPIMessage)));
//                            System.out.println("system ~~~ 沒有臉 ~~~");
                        } else if (status.equals(resources.getString(R.string.Error))) {
                            String message = jsonObject.getString(resources.getString(R.string.FaceAPIMessage));
                            if (message.equals(resources.getString(R.string.Stranger))) {
//                                System.out.println("system ~~~ 陌生人 ~~~");
                                sayHi(null, "", _count);
                            }
                            firebaseAnalyticsManager.putErrorMessage(kind, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "JSONException - " + e.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String message = "";
                    if (_res[1].equals(resources.getString(R.string.Error))) {
                        message = _res[0];
                    } else {
                        message = _res[1];
                    }
                    firebaseAnalyticsManager.putErrorMessage(kind, message);
                }
            }
        }

        Log.i(TAG, "FunctestTime " + _count + " - onApiAsyncTaskFinished() over");
    }

/*
    private String parseNameFromJson(JSONArray jsonArray, int _count) {
        Log.i(TAG, "FunctestTime " + _count + " - parseNameFromJson() start");

        //測試用人臉資料
        //老大 http://www.designboom.com/wp-content/uploads/2016/01/gesichtermix-celebrity-photoshop-mashup-designboom-012.jpg
        //老二 https://static1.squarespace.com/static/5296565fe4b0aa5ff545349d/54efb393e4b0ad929f997bff/54efb3bfe4b0da6eadba4f4f/1425038362082/Portrait+Faces+and+Photography+141228.JPG
        //老三 https://static1.squarespace.com/static/5296565fe4b0aa5ff545349d/54efb393e4b0ad929f997bff/54efb3c2e4b0da6eadba4f74/1425038395265/Portrait+Faces+and+Photography+150124-2.JPG
        //老四 https://s-media-cache-ak0.pinimg.com/originals/2c/ff/ba/2cffba2ed5691025995918905f34a924.jpg
        //老五 https://metrouk2.files.wordpress.com/2014/12/ad_153629868.jpg
        //測試 http://www.short-haircut.com/wp-content/uploads/2013/04/Short-haircuts-for-oval-faces.jpg
        //real person x1 : kobe

        double DIST_THREASHOLD = 0.3;

        try {
            Map<String, Double> distMap = new HashMap<String, Double>(); //pid / dist
            Map<String, Integer> voteMap = new HashMap<String, Integer>(); //pid / dist
            String tmpPid;
            double distFromJson = 0.0;
            double distFromMap = 0.0;
            int tmpVote = 1;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                //TODO: check assert if json has no content with key "dist" and "pid"

                tmpVote = 1;
                tmpPid = json.getString("personid");
                distFromJson = json.getDouble("dist");

                if (distFromJson > DIST_THREASHOLD)
                    continue;

                if (distMap.containsKey(tmpPid)) {
                    distFromMap = distMap.get(tmpPid);
                }
                distMap.put(tmpPid, distFromMap + distFromJson);

                if (voteMap.containsKey(tmpPid)) {
                    tmpVote = voteMap.get(tmpPid);
                    tmpVote = tmpVote + 1;
                }
                voteMap.put(tmpPid, tmpVote);
            }

            //sort by map
            int maxVotes = -1;
            String maxPid = "";
            for (Map.Entry<String, Integer> entry : voteMap.entrySet()) {
                if (entry.getValue() > maxVotes) {
                    maxVotes = entry.getValue();
                    maxPid = entry.getKey();
                }
            }

            Log.i(TAG, "FunctestTime " + _count + " - parseNameFromJson() over");
            return maxVotes > 5 ? FirebaseDataManager.getInstance(this).getName(maxPid) : "";

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        Log.i(TAG, "FunctestTime " + _count + " - parseNameFromJson() over");
        return "";
    }
*/

    int speakCounter = 0;

    private void sayHi(String pID, String name, int _count) {
        Log.i(TAG, "FunctestTime " + _count + " - sayHi() start");

//        Log.d(TAG,String.valueOf(Integer.valueOf(mLastSpeakTime)));
        if (isSpeakFinish && (Integer.valueOf(getCurrentTimestamp()) - Integer.valueOf(mLastSpeakTime) > 10 || mLastSpeakName != name)) {

            mLastSpeakTime = getCurrentTimestamp();
            mLastSpeakName = name;

//            if (name.equals("")) {
//                System.out.println("name.equals(\"\")");
////                robotUtil.robotSpeak(resources.getString(R.string.WhoAreYou));
//                    mibo.speak(String.format(resources.getString(R.string.WhoAreYou)));
//
//                return;
//            }

            String speakStr = name;

//            int ranNum = ((int) (Math.random() * 99)) % 6; // 0 - 99
            /*
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String nowTime = simpleDateFormat.format(date);
            String[] _now = nowTime.split(":");
            int _total = Integer.parseInt(_now[0]) * 60 + Integer.parseInt(_now[1]);

            if (6 * 60 <= _total && _total < 10 * 60 - 1) {
                speakStr += mornings[getRandom(mornings.length)];
            } else if (10 * 60 <= _total && _total < 13 * 60 - 1) {
                speakStr += afternoons[getRandom(afternoons.length)];
            } else if (13 * 60 <= _total && _total < 15 * 60 - 1) {
                speakStr += noons[getRandom(noons.length)];
            } else {
                speakStr += nights[getRandom(nights.length)];
            }
            */

            String selectedLexiconName = spf.getString("selectedLexiconName", "");
            if (selectedLexiconName!=null){
                if (mySQLite.isTableExists(selectedLexiconName)) {
                    wordsList = mySQLite.getRows(selectedLexiconName);
                    Random random = new Random();
                    speakStr += wordsList.get(random.nextInt(wordsList.size())).getWord();
//                    speakStr += nights[getRandom(nights.length)];
                }
            }

            robotUtil.robotSpeak(speakStr);
            mibo.speak(speakStr);

            return;
        }

        Log.i(TAG, "FunctestTime " + _count + " - sayHi() stop");
    }

    private int getRandom(int size) {
        return ((int) (Math.random() * 999)) % size;
    }

    @Override
    public void RobotonStateChange(String state) {
        Log.i(TAG, "RobotonStateChange - " + state);
        if (state.startsWith("3"))
            isSpeakFinish = false;
        else if (state.startsWith("5"))
            isSpeakFinish = true;
    }

    @Override
    public void RobotonEventUserUtterance(int position) {

    }


}
