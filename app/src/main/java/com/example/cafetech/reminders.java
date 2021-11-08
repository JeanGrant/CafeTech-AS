package com.example.cafetech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class reminders extends AppCompatActivity {

    public FloatingActionButton backhome;
    public Button takephotobtn, uploadimgbtn;
    private ImageClassifier imageClassifier;
    private Bitmap inpIMG_bmp;
    private Uri inpIMG_uri;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        //UI Related
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        backhome = findViewById(R.id.backhome);
        takephotobtn = findViewById(R.id.bttn_cam);
        uploadimgbtn = findViewById(R.id.bttn_upload);

        //initialize classifier
        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("IMAGE CLASSIFIER ERROR", "ERROR:" + e);
        }

        backhome.setOnClickListener(view -> {
            Intent backToHome = new Intent(reminders.this, MainActivity.class);
            backToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backToHome);
        });

        takephotobtn.setOnClickListener(view -> { //Open Camera
            Intent opencam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(opencam, 100);
        });

        uploadimgbtn.setOnClickListener(view -> { //Select Image
            Intent selimg = new Intent();
            selimg.setType("image/*");
            selimg.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selimg,"Select Picture"), 200);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent transferdiagnosis = new Intent(reminders.this, diagnosis.class);

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            inpIMG_uri = data.getData();
            InputStream inpIMG_path;
            try {
                inpIMG_path = getContentResolver().openInputStream(inpIMG_uri);
                Bitmap inpIMG_bmp = BitmapFactory.decodeStream(inpIMG_path);
                byteArray = toBytArr(inpIMG_bmp);

                transferdiagnosis.putExtra("inputcamera", inpIMG_uri.toString());
                transferdiagnosis.putExtra("inputMeth", "0");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            inpIMG_bmp = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
            byteArray = toBytArr(inpIMG_bmp);
            transferdiagnosis.putExtra("picture", byteArray);
            transferdiagnosis.putExtra("inputMeth", "1");
        }

        List<ImageClassifier.Recognition> predictions = imageClassifier.recognizeImage(byteArray, 0);
        transferdiagnosis.putExtra("labeldiag", predictions.get(0).toString());

        startActivity(transferdiagnosis);
    }

    private byte[] toBytArr(Bitmap convBMP) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        convBMP.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] convArr = stream.toByteArray();
        return convArr;
    }
}