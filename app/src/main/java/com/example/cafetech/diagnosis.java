package com.example.cafetech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class diagnosis extends AppCompatActivity {
    public FloatingActionButton inpBTN;
    public Button backBTN, exitBTN, helpBTN;
    public TextView titleTxtView, lblTxtView, infoTxtView, prompt1TxtView;
    public ImageView inpImgView;
    public String lblTxt;
    public Bitmap imgBmp;
    public static String inpIMG_str;
    public static Uri inpIMG_uri;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //region UI Related {hide ActionBar, disable NightMode, setContentView, identify each layout elements, set responsive layout size}
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_diagnosis);

        int textMultiplier = Math.round(getResources().getDisplayMetrics().density / getResources().getConfiguration().fontScale);
        int scaleMultiplier = Math.round(getResources().getDisplayMetrics().density);

        backBTN = findViewById(R.id.backBTN);
        backBTN.getLayoutParams().height = 30 * scaleMultiplier;

        titleTxtView = findViewById(R.id.title_TxtView);
        titleTxtView.setTextSize(7 *  textMultiplier);

        inpImgView = findViewById(R.id.inp_ImgView);
        inpImgView.requestLayout();
        inpImgView.getLayoutParams().height = 150 * scaleMultiplier;
        inpImgView.getLayoutParams().width = 150 * scaleMultiplier;

        prompt1TxtView = findViewById(R.id.prompt1_TxtView);
        prompt1TxtView.setTextSize(7 * textMultiplier);
        lblTxtView = findViewById(R.id.label_TxtView);
        lblTxtView.setTextSize(6 * textMultiplier);
        infoTxtView = findViewById(R.id.info_TxtView);
        infoTxtView.setTextSize(6 * textMultiplier);


        exitBTN = findViewById(R.id.exitBTN);
        exitBTN.getLayoutParams().height = 30 * scaleMultiplier;
        helpBTN = findViewById(R.id.helpBTN);
        helpBTN.getLayoutParams().height = 30 * scaleMultiplier;
        inpBTN = findViewById(R.id.camBTN);
        inpBTN.getLayoutParams().height = 60 * scaleMultiplier;
        inpBTN.getLayoutParams().width = 60 * scaleMultiplier;
        inpBTN.setCustomSize(60 * scaleMultiplier);
        //endregion

        //region Get data from Main
        Intent fromMain = getIntent();
        lblTxt = fromMain.getStringExtra("labeldiag");
        String timeresponse = fromMain.getStringExtra("time");

        String requestCode = fromMain.getStringExtra("inputMeth");

        if (requestCode.equals("0")) {
            inpIMG_str = fromMain.getStringExtra("inputcamera");
            inpIMG_uri = Uri.parse(inpIMG_str);
            try {
                InputStream imgPath = getContentResolver().openInputStream(inpIMG_uri);
                imgBmp = BitmapFactory.decodeStream(imgPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode.equals("1")) {
            Bundle extras = getIntent().getExtras();
            byte[] byteArray = extras.getByteArray("picture");
            imgBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        int cropSize = Math.min(imgBmp.getWidth(), imgBmp.getHeight());
        Bitmap croppedBmp = Bitmap.createBitmap(imgBmp, 0, 0, cropSize,cropSize);
        inpImgView.setImageBitmap(croppedBmp);
        //endregion

        //region Dependent Texts
        switch(lblTxt){
            case "healthy":
                lblTxtView.setText("Healthy ");
                infoTxtView.setText(R.string.basinf_healthy);
                break;
            case "miner":
                lblTxtView.setText("Leaf Miner ");
                infoTxtView.setText(R.string.basinf_miner);
                infoTxtView.setMovementMethod(new ScrollingMovementMethod());
                break;
            case "rust":
                lblTxtView.setText("Leaf Rust ");
                infoTxtView.setText(R.string.basinf_rust);
                infoTxtView.setMovementMethod(new ScrollingMovementMethod());
                break;
            case "cercospora":
                lblTxtView.setText("Cercospora Leaf Spot ");
                infoTxtView.setText(R.string.basinf_cercospora);
                infoTxtView.setMovementMethod(new ScrollingMovementMethod());
                break;
            case "phoma":
                lblTxtView.setText("Phoma Leaf Spot ");
                infoTxtView.setText(R.string.basinf_phoma);
                infoTxtView.setMovementMethod(new ScrollingMovementMethod());
                break;
            default:
                lblTxtView.setText(lblTxt);
                infoTxtView.setText("Error");
                break;
        }
        //endregion

        //region Button Actions
        backBTN.setOnClickListener(view -> {
            Intent intent= new Intent(diagnosis.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        exitBTN.setOnClickListener(view -> {
            Intent intent = new Intent(diagnosis.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
        });

        inpBTN.setOnClickListener(view -> {
            Intent remindersScreen = new Intent(diagnosis.this, reminders.class);
            startActivity(remindersScreen);
            finish();
        });

        helpBTN.setOnClickListener(view -> {
            Intent helpScreen = new Intent(diagnosis.this, help.class);
            startActivity(helpScreen);
        });
        //endregion
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(diagnosis.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}