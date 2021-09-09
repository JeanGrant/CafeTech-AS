package com.example.cafetech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public FloatingActionButton camerabutt;
    private int val;
    private ImageClassifier imageClassifier;
    public ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //Request for Camera Permission Upon Startup
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        //Set Variables
        TextView rndnum = findViewById(R.id.AppTitle);
        camerabutt = findViewById(R.id.camerabutt);
        ImageView caminp = findViewById(R.id.AppLogo);
        listview = findViewById(R.id.listtest);

        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("IMAGE CLASSIFIER ERROR", "ERROR:" + e);
        }

        //Actions when button is clicked
        camerabutt.setOnClickListener(view -> {
            //Open Camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);


            //Generate Random Number
            Random random = new Random();
            val = random.nextInt(5);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //Get Capture Image
            assert data != null;
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //Set Capture Image to ImageView
            //caminp.setImageBitmap(captureImage);

            //transfer
            Intent inpcam = new Intent(MainActivity.this, diagnosis.class);
            inpcam.putExtra(diagnosis.INPCAM,String.valueOf(val));
            inpcam.putExtra("inputcamera", captureImage);

            List<ImageClassifier.Recognition> predictions = imageClassifier.recognizeImage(captureImage, 0);
            float topres = 0;
            for (ImageClassifier.Recognition recog : predictions) {
                if(recog.getConfidence()>topres){
                    topres = recog.getConfidence();
                }
            }
            for (ImageClassifier.Recognition recog : predictions) {
                if(recog.getConfidence()==topres){
                    inpcam.putExtra("labeldiag", recog.getName());
                }
            }

            startActivity(inpcam);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}