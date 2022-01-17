package com.example.cafetech;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

public class help extends AppCompatActivity {

    Button arrow1, arrow2, arrow3;
    ConstraintLayout hiddenView1, hiddenView2, hiddenView3;
    CardView cardView1, cardView2, cardView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UI Related
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_help);

        cardView1 = findViewById(R.id.base_cardview_1);
        arrow1 = findViewById(R.id.arrow_button_1);
        hiddenView1 = findViewById(R.id.hidden_view_1);

        cardView1.setOnClickListener(view -> {
            if (hiddenView1.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                TransitionManager.beginDelayedTransition(cardView3,
                        new AutoTransition());
                hiddenView1.setVisibility(View.GONE);
                arrow1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_more_24,0,0,0);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView1,
                        new AutoTransition());
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                TransitionManager.beginDelayedTransition(cardView3,
                        new AutoTransition());
                hiddenView1.setVisibility(View.VISIBLE);
                arrow1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_less_24,0,0,0);
            }
        });

        cardView2 = findViewById(R.id.base_cardview_2);
        arrow2 = findViewById(R.id.arrow_button_2);
        hiddenView2 = findViewById(R.id.hidden_view_2);

        cardView2.setOnClickListener(view -> {
            if (hiddenView2.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView3,
                        new AutoTransition());
                hiddenView2.setVisibility(View.GONE);
                arrow2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_more_24,0,0,0);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                TransitionManager.beginDelayedTransition(cardView3,
                        new AutoTransition());
                hiddenView2.setVisibility(View.VISIBLE);
                arrow2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_less_24,0,0,0);
            }
        });

        cardView3 = findViewById(R.id.base_cardview_3);
        arrow3 = findViewById(R.id.arrow_button_3);
        hiddenView3 = findViewById(R.id.hidden_view_3);

        cardView3.setOnClickListener(view -> {
            if (hiddenView3.getVisibility() == View.VISIBLE) {
                hiddenView3.setVisibility(View.GONE);
                arrow3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_more_24,0,0,0);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView3,
                        new AutoTransition());
                hiddenView3.setVisibility(View.VISIBLE);
                arrow3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_expand_less_24,0,0,0);
            }
        });

        // Going backBTN
        Button backhome = findViewById(R.id.backBTN);

        backhome.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}