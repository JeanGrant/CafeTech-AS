package com.example.cafetech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class diagnosis extends AppCompatActivity {
    public FloatingActionButton backhome;
    public TextView outdiag, descdiag;
    public ImageView inpcamdisp;
    public static final String INPCAM = "0";
    public static Bitmap inputcamera;
    public String inputcam, labelout;
    public String basinf_0 = "Mabuhay, ang iyong tanim na Arabica ay walang ipinapakitang implikasyon ng pagkakaroon ng sakit!";
    public String basinf_1 = "Ang iyong tanim na Arabica ay nagpapakita ng mga sintomas ng pagkakaroon ng 'Coffee Leaf Miners'. Ang 'Coffee Leaf Miner' ay isang cosmopolitan na peste. Ito ay maaaring makita sa itaas na bahagi ng apektadong dahon. Ang dahong apektado ay nagpapakita ng mga irregular na mga kayumangging dungis. Kapag hinawakan ang mga dungis, maaaring makita ang mga maliliit na puting uod na nakapaloob sa bahaging itaas ng balat ng dahon. Ang buhay ng mga peste ay tumatagal depende sa temperatura, halumigmig, at ulan.";
    public String basinf_2 = "Ang iyong tanim na Arabica ay nagpapakita ng mga sintomas ng pagkakaroon ng 'Coffee Leaf Rusts'. Ang amag na 'Hemileia vastatrix' ang sanhi ng sakit na Coffee Leaf Rust. Ang amag na ito ay kadalasang lumalabas sa basang dahon tuwing tagulan. Maaari itong makilala sa bahagin itaas ng dahon sa pamamagitan ng mapuputing dilaw na mga dungis. Maari din itong mapansin sa ibabang bahagi ng dahon sa pamamagitan ng mapulbos na kahel na mga sugat.";
    public String basinf_3 = "Ang iyong tanim na Arabica ay nagpapakita ng mga sintomas ng pagkakaroon ng 'Cercospora Leaf Rust'. Ang amag na 'Cercospora coffeicola' ang sanhi ng sakit na Cercospora Leaf Rust. Ang kadalasang sintomas nang sakit na ito ay ang hugis bilog na mga dungis. Ang gitna ng mga dungis ay mayroong kulay-balat, kulay-abo o kulay-puti. Ang mga sugat sa dahon ay kadalasang napapalibutan ng kulay-dilaw.";
    public String basinf_4 = "Ang iyong tanim na Arabica ay nagpapakita ng mga sintomas ng pagkakaroon ng 'Phoma Leaf Rust'. Ang buhay ng mga pathogen na sanhi ng sakit na Phoma Leaf Rust ay nakasalalay sa klima at altitude. Pinapaboran nito ang mga temperatura mula sa 16 degree Celsius at 20 degree Celsius. Kumakalat ito sa pamamagitan ng tilamsik ng tubig. Maaaring makilala ang sakit na ito sa pamamagitan ng maiitim na dungis na kadalasang nagsisimula sa gilid ng dahon.";
    public String cm_0 = "Upang mapanatili ang kalusugan ng iyong tanim na Arabica";
    public String cm_1 = "Upang ma-kontrol ang higit na pagkalat ng sakit na Coffee Leaf Miners";
    public String cm_2 = "Upang ma-kontrol ang higit na pagkalat ng sakit na Coffee Leaf Rust";
    public String cm_3 = "Upang ma-kontrol ang higit na pagkalat ng sakit na Cercospora Leaf Rust";
    public String cm_4 = "Upang ma-kontrol ang higit na pagkalat ng sakit na Phoma Leaf Rust";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_diagnosis);

        //get random number from MainActivity
        Intent inpcam = getIntent();
        inputcam = inpcam.getStringExtra(INPCAM);
        labelout = inpcam.getStringExtra("labeldiag");
        inputcamera = inpcam.getParcelableExtra("inputcamera");

        //set texts dependent on input
        outdiag = findViewById(R.id.diagout);
        descdiag = findViewById(R.id.diagdescription);
        inpcamdisp = findViewById(R.id.inpcamdisplay);

        inpcamdisp.setImageBitmap(inputcamera);
        outdiag.setText(labelout);
        //dependent texts
        if (Integer.parseInt(inputcam)==0){
            descdiag.setText("Basic Information\n" + basinf_0 + "\n\nControl and Management\n" + cm_0);
        }else if (Integer.parseInt(inputcam)==1){
            descdiag.setText("Basic Information\n" + basinf_1 + "\n\nControl and Management\n" + cm_1);
        }else if (Integer.parseInt(inputcam)==2){
            descdiag.setText("Basic Information\n" + basinf_2 + "\n\nControl and Management\n" + cm_2);
        }else if (Integer.parseInt(inputcam)==3){
            descdiag.setText("Basic Information\n" + basinf_3 + "\n\nControl and Management\n" + cm_3);
        }else if (Integer.parseInt(inputcam)==4){
            descdiag.setText("Basic Information\n" + basinf_4 + "\n\nControl and Management\n" + cm_4);
        }else {
            outdiag.setText("Error");
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