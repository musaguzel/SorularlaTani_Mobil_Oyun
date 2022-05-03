package com.musaguzel.sorularlatani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class resultActivity extends AppCompatActivity {
    TextView yorumText,animationText;
    String soranismi,cevaplayanismi,yorum,documentName;
    GifImageView konfettigif;
    private FirebaseFirestore firebaseFirestore;
    int score,numberForYorum;
    boolean sevgilimi,grupmu;
    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //Tanımlamalar
        konfettigif = findViewById(R.id.konfetti);
        yorumText = findViewById(R.id.yorumText);
        animationText = findViewById(R.id.animationText);
        //Intent ile isimler ve score değeri alma
        Intent intent = getIntent();
        soranismi = intent.getStringExtra("sorusoran");
        cevaplayanismi = intent.getStringExtra("sorucevaplayan");
        score = intent.getIntExtra("scorevalue",0);
        sevgilimi = intent.getBooleanExtra("sevgilimi",false);
        grupmu = intent.getBooleanExtra("grupmu",false);
        //Initialize etme
        firebaseFirestore = FirebaseFirestore.getInstance();
        getCommentFromFirestore();
    }

    public void getCommentFromFirestore(){
        numberForYorum = random.nextInt(5 - 1) + 1;
        realdocumentname();
        DocumentReference documentReference = firebaseFirestore.collection("Yorumlar").document(documentName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()){
                        Map<String,Object> data = document.getData();
                        yorum = data.get("Yorum" + numberForYorum).toString();
                        yorumText.setText(yorum);
                    }
                }
            }
        });
    }

    public void puanagorebelgeismiarkadas(){

        if (score < 20){
            documentName = "020puanyorumları";
        } if (score > 20 && score <= 40){
            documentName = "2040puanyorumları";
        } if (score > 40 && score <= 60){
            documentName = "4060puanyorumları";
        } if (score > 60 && score <= 80){
            documentName = "6080puanyorumları";
        } if (score > 80 && score <= 100){
            documentName = "80100puanyorumları";
        }
    }

    public void puanagorebelgeismisevgili(){
        if (score < 20){
            documentName = "020puansevgiliyorumları";
        }if (score > 20 && score <= 40){
            documentName = "2040puansevgiliyorumları";
        }if (score > 40 && score <= 60){
            documentName = "4060puansevgiliyorumları";
        }if (score > 60 && score <= 80){
            documentName = "6080puansevgiliyorumları";
        }if (score > 80 && score <= 100){
            documentName = "80100puansevgiliyorumları";
        }
    }
    public void belgeismigrup(){
        if (grupmu == true){
            documentName = "grupyorumları";
        }
    }
        //Üstteki üç metodun birleşimi
    public void realdocumentname(){
        if (sevgilimi == false && grupmu == false){
            puanagorebelgeismiarkadas();
        }if (sevgilimi == true){
            puanagorebelgeismisevgili();
        }if (grupmu == true){
            belgeismigrup();
        }
    }

    public void anamenudon(View view){
        Intent intent = new Intent(resultActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

//yorumlar ve sorular tasarlanmalı
//gerçek id ler yerlerine konulacak

//Banner Ad id main activity
//ca-app-pub-2353477900915398/8209119746

//Banner Ad id get name activity
//ca-app-pub-2353477900915398/9093608383

//Banner Ad id group activity
//ca-app-pub-2353477900915398/7012394041

//Banner Ad id display activity
//ca-app-pub-2353477900915398/6810048219

//Banner test id
//ca-app-pub-3940256099942544/6300978111

//Intersitial ad id
//ca-app-pub-2353477900915398/2597059786

//App id
//ca-app-pub-2353477900915398~2666813758

