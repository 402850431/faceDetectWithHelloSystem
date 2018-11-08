package com.example.user.facedetectwithhellosystem.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.tools.SquareImageView;

public class MainActivity extends AppCompatActivity {

    SquareImageView faceDetectImg, lexiconImg;
    SharedPreferences spf;
    private MySQLite mySQLite;
    private static final int PERMISSION_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        setUpOnClicks();
    }

    private void setUpOnClicks() {
        faceDetectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySQLite.isHasTable()) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    } else {
                        startActivity(new Intent(MainActivity.this, FaceDetectNoAnalysisActivity.class));
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.tableIsEmpty, Toast.LENGTH_SHORT).show();
                }
            }
        });

        lexiconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>onclick", "onclick");
                new ReplaceFragment(MainActivity.this).replace(getSupportFragmentManager(), R.id.mainFrame, new ManageLexiconFragment());
            }
        });
    }

    private void findViewByIds() {
        mySQLite = new MySQLite(this);
        faceDetectImg = findViewById(R.id.faceDetectAndSayHelloImg);
        lexiconImg = findViewById(R.id.lexiconManagementImg);
        spf = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
