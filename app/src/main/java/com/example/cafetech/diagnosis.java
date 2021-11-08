package com.example.cafetech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class diagnosis extends AppCompatActivity {
    public FloatingActionButton backhome, inputbutt;
    public Button exitbutt, helpbutt;
    public TextView outdiag, basinf_view, cm_view;
    public ImageView inpcamdisp;
    public static String inpIMG_str;
    public static Uri inpIMG_uri;
    public String labelout, requestCode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UI Related
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_diagnosis);
        outdiag = findViewById(R.id.diagout);
        basinf_view = findViewById(R.id.basinfdesc);
        inpcamdisp = findViewById(R.id.inpcamdisplay);

        //get data from Main
        Intent fromMain = getIntent();
        labelout = fromMain.getStringExtra("labeldiag");
        requestCode = fromMain.getStringExtra("inputMeth");

        if (requestCode.equals("0")) {
            inpIMG_str = fromMain.getStringExtra("inputcamera");
            inpIMG_uri = Uri.parse(inpIMG_str);
            try {
                InputStream inpIMG_path = getContentResolver().openInputStream(inpIMG_uri);
                Bitmap inpIMG_bmp = BitmapFactory.decodeStream(inpIMG_path);
                int cropSize = Math.min(inpIMG_bmp.getWidth(), inpIMG_bmp.getHeight());
                Bitmap croppedBmp = Bitmap.createBitmap(inpIMG_bmp, 0, 0, cropSize,cropSize);
                inpcamdisp.setImageBitmap(croppedBmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode.equals("1")) {
            Bundle extras = getIntent().getExtras();
            byte[] byteArray = extras.getByteArray("picture");
            Bitmap inpIMG_bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //include cropping

            inpcamdisp.setImageBitmap(inpIMG_bmp);
        }

        //dependent texts
        String[] listLabels = {"healthy", "miner", "rust", "cercospora", "phoma"};

        int getLabel;
        for(getLabel = 0; getLabel < listLabels.length; getLabel++){
            if(labelout.contains(listLabels[getLabel])) break;}

        switch(getLabel){
            case 0:
                outdiag.setText(labelout);
                basinf_view.setText(R.string.basinf_healthy);
                break;
            case 1:
                outdiag.setText(labelout);
                basinf_view.setText(R.string.basinf_miner);
                basinf_view.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 2:
                outdiag.setText(labelout);
                basinf_view.setText(R.string.basinf_rust);
                basinf_view.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 3:
                outdiag.setText(labelout);
                basinf_view.setText(R.string.basinf_cercospora);
                basinf_view.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 4:
                outdiag.setText(labelout);
                basinf_view.setText(R.string.basinf_phoma);
                basinf_view.setMovementMethod(new ScrollingMovementMethod());
                break;
            default:
                outdiag.setText(labelout);
                basinf_view.setText("Error");
                cm_view.setText("Error");
                break;
        }

        // Going backhome
        backhome = findViewById(R.id.backhome);

        backhome.setOnClickListener(view -> {
            Intent intent= new Intent(diagnosis.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Exit button
        exitbutt = findViewById(R.id.exitApp);
        exitbutt.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        });

        // Input button
        inputbutt = findViewById(R.id.proceedbutt);
        inputbutt.setOnClickListener(view -> {
            Intent nextScreen = new Intent(diagnosis.this, reminders.class);
            startActivity(nextScreen);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(diagnosis.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}