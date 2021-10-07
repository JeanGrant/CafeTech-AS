package com.example.cafetech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ImageClassifier imageClassifier;
    public FloatingActionButton camerabutt;
    public ImageView caminp; // to be removed; only for debugging
    public ListView listview; //to be removed; only for debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UI Related
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        camerabutt = findViewById(R.id.camerabutt);
        caminp = findViewById(R.id.AppLogo);
        listview = findViewById(R.id.probabilities);

        //Request for Camera Permission Upon Startup
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }*/

        //initialize classifier
        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("IMAGE CLASSIFIER ERROR", "ERROR:" + e);
        }

        //Actions when button is clicked
        camerabutt.setOnClickListener(view -> {
            //Open Camera
            //Intent opencam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(opencam, 100);

            //Select Image
            Intent selimg = new Intent();
            selimg.setType("image/*");
            selimg.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selimg,"Select Picture"), 200);

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            Uri inpIMG_uri = data.getData();
            try {
                InputStream inpIMG_path = getContentResolver().openInputStream(inpIMG_uri);
                Bitmap inpIMG_bmp = BitmapFactory.decodeStream(inpIMG_path);

                caminp.setImageBitmap(inpIMG_bmp);

                //Intent transferdiagnosis = new Intent(MainActivity.this, diagnosis.class);
                List<ImageClassifier.Recognition> predictions = imageClassifier.recognizeImage(inpIMG_bmp, 0);
                final List<String> predictionsList = new ArrayList<>();
                for(ImageClassifier.Recognition recog : predictions){
                    predictionsList.add("Label: "+recog.getName()+" Confidence: "+recog.getConfidence());
                }
                ArrayAdapter<String> predictionsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, predictionsList);
                listview.setAdapter(predictionsAdapter);
                //transferdiagnosis.putExtra("labeldiag", predictions.get(0).toString());
                //transferdiagnosis.putExtra("inputcamera", inpIMG_uri.toString());

                //startActivity(transferdiagnosis);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}