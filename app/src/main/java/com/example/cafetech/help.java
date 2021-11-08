package com.example.cafetech;

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

        arrow1.setOnClickListener(view -> {
            if (hiddenView1.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                hiddenView1.setVisibility(View.GONE);
//                    arrow(R.drawable.ic_baseline_expand_more_24);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView1,
                        new AutoTransition());
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                hiddenView1.setVisibility(View.VISIBLE);
//                    arrow.setCompoundDrawables(R.drawable.ic_baseline_expand_less_24);
            }
        });

        cardView2 = findViewById(R.id.base_cardview_2);
        arrow2 = findViewById(R.id.arrow_button_2);
        hiddenView2 = findViewById(R.id.hidden_view_2);

        arrow2.setOnClickListener(view -> {
            if (hiddenView2.getVisibility() == View.VISIBLE) {
                hiddenView2.setVisibility(View.GONE);
//                    arrow(R.drawable.ic_baseline_expand_more_24);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView2,
                        new AutoTransition());
                hiddenView2.setVisibility(View.VISIBLE);
//                    arrow.setCompoundDrawables(R.drawable.ic_baseline_expand_less_24);
            }
        });
    }
}