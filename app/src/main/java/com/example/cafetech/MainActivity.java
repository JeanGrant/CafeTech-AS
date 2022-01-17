package com.example.cafetech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public FloatingActionButton inpBTN;
    public TextView permissionPrompt, appTitle, appSubtitle, appDescription;
    public ImageView appLogo;
    public Button exitBTN, helpBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //region Exit App
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            System.exit(0);
        }
        //endregion

        //region UI related {hide ActionBar, disable NightMode, setContentView, identify each layout elements, set responsive layout size
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        int textMultiplier = Math.round(getResources().getDisplayMetrics().density / getResources().getConfiguration().fontScale);
        int scaleMultiplier = Math.round(getResources().getDisplayMetrics().density);

        appLogo = findViewById(R.id.AppLogo);
        appLogo.requestLayout();
        appLogo.getLayoutParams().height = 135 * scaleMultiplier;
        appLogo.getLayoutParams().width = 135 * scaleMultiplier;

        appTitle = findViewById(R.id.AppTitle);
        appTitle.setTextSize(20 * textMultiplier);
        appSubtitle = findViewById(R.id.AppSubtitle);
        appSubtitle.setTextSize(7 * textMultiplier);
        appDescription = findViewById(R.id.AppDescription);
        appDescription.setTextSize(6 * textMultiplier);

        inpBTN = findViewById(R.id.camBTN);
        inpBTN.getLayoutParams().height = 60 * scaleMultiplier;
        inpBTN.getLayoutParams().width = 60 * scaleMultiplier;
        inpBTN.setCustomSize(60 * scaleMultiplier);
        exitBTN = findViewById(R.id.exitBTN);
        exitBTN.getLayoutParams().height = 30 * scaleMultiplier;
        helpBTN = findViewById(R.id.helpBTN);
        helpBTN.getLayoutParams().height = 30 * scaleMultiplier;
        //endregion

        //region Request Permission {set text visibility, check permission, redirect to settings / next screen}
        permissionPrompt = findViewById(R.id.permission_error);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionPrompt.setVisibility(View.VISIBLE);
        }
        else {
            permissionPrompt.setVisibility(View.GONE);
        }

        this.inpBTN.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
            else {
                Intent nextScreen = new Intent(MainActivity.this, reminders.class);
                startActivity(nextScreen);
            }
        });
        //endregion

        //region Other Button Actions {exitBTN = Terminate app, helpBTN = redirect to helpdesk}
        exitBTN.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });

        helpBTN.setOnClickListener(view -> {
            Intent nextScreen = new Intent(MainActivity.this, help.class);
            startActivity(nextScreen);
        });
        //endregion
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionPrompt.setVisibility(View.VISIBLE);
        }
        else {
            permissionPrompt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}