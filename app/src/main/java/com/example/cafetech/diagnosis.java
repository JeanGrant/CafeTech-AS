package com.example.cafetech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class diagnosis extends AppCompatActivity {
    public FloatingActionButton backhome;
    public TextView outdiag, basinf_view, cm_view, basinf_prompt, cm_prompt;
    public ImageView inpcamdisp;
    public static String inpIMG_str;
    public static Uri inpIMG_uri;
    public String labelout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UI Related
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_diagnosis);
        outdiag = findViewById(R.id.diagout);
        basinf_prompt = findViewById(R.id.basinfprompt);
        basinf_view = findViewById(R.id.basinfdesc);
        cm_prompt = findViewById(R.id.cmprompt);
        cm_view = findViewById(R.id.cmdesc);
        inpcamdisp = findViewById(R.id.inpcamdisplay);

        //prepare texts


        //get data from Main
        Intent fromMain = getIntent();
        labelout = fromMain.getStringExtra("labeldiag");
        inpIMG_str = fromMain.getStringExtra("inputcamera");
        inpIMG_uri = Uri.parse(inpIMG_str);

        //set UI elements based on input
        inpcamdisp.setImageURI(inpIMG_uri);
        //dependent texts
        switch(labelout){
            case "healthy":
                outdiag.setText("Healthy");
                basinf_prompt.setText(R.string.basic_info_prompt_alt);
                basinf_view.setText(R.string.basinf_healthy);
                cm_prompt.setText(R.string.control_prompt_alt);
                cm_view.setText(R.string.cm_healthy);
                break;
            case "miner":
                outdiag.setText("Leaf Miners");
                basinf_view.setText(R.string.basinf_miner);
                cm_view.setText(R.string.cm_miner);
                break;
            case "rust":
                outdiag.setText("Leaf Rust");
                basinf_view.setText(R.string.basinf_rust);
                cm_view.setText(R.string.cm_rust);
                break;
            case "cercospora":
                outdiag.setText("Cercospora Leaf Rust");
                basinf_view.setText(R.string.basinf_cercospora);
                cm_view.setText(R.string.cm_cercospora);
                break;
            case "phoma":
                outdiag.setText("Phoma Leaf Rust");
                basinf_view.setText(R.string.basinf_phoma);
                cm_view.setText(R.string.cm_phoma);
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
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(diagnosis.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}