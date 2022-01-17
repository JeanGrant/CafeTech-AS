package com.example.cafetech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class reminders extends AppCompatActivity {

    public Button backBTN, camBTN, uploadBTN;
    public TextView titleTxtView, g1TxtView, g2TxtView, g1s1TxtView, g1s2TxtView, g2s1TxtView, g2s2TxtView;
    public ImageView g1s1ImgView, g1s2ImgView, g2s1ImgView, g2s2ImgView;
    private ImageClassifier imgClassifier;
    private Bitmap imgBmp;
    public byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //region UI related {hide ActionBar, disable NightMode, setContentView, identify each layout elements, set responsive layout size}
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_reminders);

        int textMultiplier = Math.round(getResources().getDisplayMetrics().density / getResources().getConfiguration().fontScale);
        int scaleMultiplier = Math.round(getResources().getDisplayMetrics().density);

        backBTN = findViewById(R.id.backBTN);
        backBTN.getLayoutParams().height = 30 * scaleMultiplier;

        titleTxtView = findViewById(R.id.guide_title);
        titleTxtView.setTextSize(7 *  textMultiplier);

        g1TxtView = findViewById(R.id.guide_1);
        g1TxtView.setTextSize(6 * textMultiplier);
        g1s1ImgView = findViewById(R.id.cor_focus_img);
        g1s1ImgView.requestLayout();
        g1s1ImgView.getLayoutParams().height = 120 * scaleMultiplier;
        g1s1ImgView.getLayoutParams().width = 120 * scaleMultiplier;
        g1s1TxtView = findViewById(R.id.cor_focus_prompt);
        g1s1TxtView.setTextSize(5 * textMultiplier);
        g1s2ImgView = findViewById(R.id.wro_focus_img);
        g1s2ImgView.requestLayout();
        g1s2ImgView.getLayoutParams().height = 120 * scaleMultiplier;
        g1s2ImgView.getLayoutParams().width = 120 * scaleMultiplier;
        g1s2TxtView = findViewById(R.id.g1s2_TxtView);
        g1s2TxtView.setTextSize(5 * textMultiplier);

        g2TxtView = findViewById(R.id.guide_2);
        g2TxtView.setTextSize(6 * textMultiplier);
        g2s1ImgView = findViewById(R.id.cor_bg_img);
        g2s1ImgView.requestLayout();
        g2s1ImgView.getLayoutParams().height = 120 * scaleMultiplier;
        g2s1ImgView.getLayoutParams().width = 120 * scaleMultiplier;
        g2s1TxtView = findViewById(R.id.cor_bg_prompt);
        g2s1TxtView.setTextSize(5 * textMultiplier);
        g2s2ImgView = findViewById(R.id.wro_bg_img);
        g2s2ImgView.requestLayout();
        g2s2ImgView.getLayoutParams().height = 120 * scaleMultiplier;
        g2s2ImgView.getLayoutParams().width = 120 * scaleMultiplier;
        g2s2TxtView = findViewById(R.id.g2s2_TxtView);
        g2s2TxtView.setTextSize(5 * textMultiplier);

        camBTN = findViewById(R.id.bttn_cam);
        camBTN.getLayoutParams().height = 50 * scaleMultiplier;
        camBTN.setTextSize(5 * textMultiplier);
        uploadBTN = findViewById(R.id.bttn_upload);
        uploadBTN.getLayoutParams().height = 50 * scaleMultiplier;
        uploadBTN.setTextSize(5 * textMultiplier);
        //endregion

        //region Initialize Classifier
        try {
            imgClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("IMAGE CLASSIFIER ERROR", "ERROR:" + e);
        }
        //endregion

        //region Button Actions
        backBTN.setOnClickListener(view -> {
            Intent backToHome = new Intent(reminders.this, MainActivity.class);
            backToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backToHome);
            finish();
        });

        camBTN.setOnClickListener(view -> { //Open Camera
            Intent opencam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(opencam, 100);
        });

        uploadBTN.setOnClickListener(view -> { //Select Image
            Intent selimg = new Intent();
            selimg.setType("image/*");
            selimg.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selimg,"Select Picture"), 200);
        });
        //endregion
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent transferdiagnosis = new Intent(reminders.this, diagnosis.class);

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            Uri imgUri = data.getData();
            InputStream imgPath;
            try {
                imgPath = getContentResolver().openInputStream(imgUri);
                imgBmp = BitmapFactory.decodeStream(imgPath);
                byteArray = toBytArr(imgBmp);

                transferdiagnosis.putExtra("inputcamera", imgUri.toString());
                transferdiagnosis.putExtra("inputMeth", "0");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imgBmp = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
            byteArray = toBytArr(imgBmp);
            transferdiagnosis.putExtra("picture", byteArray);
            transferdiagnosis.putExtra("inputMeth", "1");
        }

        long sTime = System.nanoTime();
        List<ImageClassifier.Recognition> predictions = imgClassifier.recognizeImage(byteArray, 0);
        long fTime = System.nanoTime();
        long elapsedTime = fTime - sTime;
        long conversion = 1000000L;
        elapsedTime = elapsedTime / conversion;
        String timeresponse = Long.toString(elapsedTime);
        transferdiagnosis.putExtra("time", timeresponse);
        transferdiagnosis.putExtra("labeldiag", predictions.get(0).toString());

        startActivity(transferdiagnosis);
        finish();
    }

    private byte[] toBytArr(Bitmap convBMP) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        convBMP.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] convArr = stream.toByteArray();
        return convArr;
    }

}