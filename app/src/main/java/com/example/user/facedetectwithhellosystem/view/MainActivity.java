package com.example.user.facedetectwithhellosystem.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.tools.SquareImageView;

public class MainActivity extends AppCompatActivity {

    SquareImageView faceDetectImg, lexiconImg;

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
            }
        });

        lexiconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReplaceFragment(MainActivity.this).replace(getSupportFragmentManager(), R.id.mainFrame, new ManageLexiconFragment());
            }
        });
    }

    private void findViewByIds() {
        faceDetectImg = findViewById(R.id.faceDetectAndSayHelloImg);
        lexiconImg = findViewById(R.id.lexiconManagementImg);

    }
}
