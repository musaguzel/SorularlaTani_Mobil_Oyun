package com.musaguzel.sorularlatani;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class questionDisplayActivity extends AppCompatActivity {
    private AdView mAdView;
    private ArrayList<String> yorumlar;
    private boolean buttontiklanmaKontrolu;
    GifImageView heartgif;
    TextView scoreText,sorusayimText,soruText;
    Button bildiButton,bilmediButton,unutmusButton,devametButton;
    private FirebaseFirestore firebaseFirestore;
    String soru,collectionName,soruyusoran,soruyucevaplayan,grupName;
    boolean sevgilimi,grupmu;
    Random random = new Random();
    int numberForDocument,numberForQuestions,numberForNiceWord;
    int soruNo = 0;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
        //Tanımlamalar
        heartgif = findViewById(R.id.heartgif);
        scoreText = findViewById(R.id.scoreText);
        sorusayimText = findViewById(R.id.soruSayimText);
        soruText = findViewById(R.id.soruText);
        bildiButton = findViewById(R.id.bildiButton);
        bilmediButton = findViewById(R.id.bilmediButton);
        unutmusButton = findViewById(R.id.unutmusButton);
        devametButton = findViewById(R.id.devametButton);
        //scoreText.setText("Puan: " + score + "/100");
        sorusayimText.setText("Soru: " + soruNo + "/10");
        yorumlar = new ArrayList<>();
        sorubitimiyorumlar();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewdisplay);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Intent ile kullanıcı isimlerini ve sevgili ve grup için boolean veriyi alma
        Intent intent = getIntent();
        soruyusoran = intent.getStringExtra("sorusoran");
        soruyucevaplayan = intent.getStringExtra("sorucevaplayan");
        sevgilimi = intent.getBooleanExtra("sevgilimi",false);
        grupmu = intent.getBooleanExtra("grupmu",false);
        grupName = intent.getStringExtra("groupname");
        arkadasSevgiliGrupAyrimi();
        if (grupmu == false){
            scoreText.setText(soruyusoran + " - " + soruyucevaplayan);
        }else {
            scoreText.setText(grupName);
        }
        //İlk başta devam et Butonu devre dışı
        devametButton.setEnabled(false);
        gerisayimozelligi();
        //Inıtalitize etme
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFirestore();

    }

    //Grup Aktivitesinden gelirse eğer butonlar görünmeyecek
    public void buttongorunurlugu(){
        bildiButton.setVisibility(View.INVISIBLE);
        bilmediButton.setVisibility(View.INVISIBLE);
        unutmusButton.setVisibility(View.INVISIBLE);
    }
    //Random belgeden Soru almak için sayı türetme metodu
    public void numberRandom(){
        numberForDocument = random.nextInt(7 - 1) + 1;
        numberForQuestions = random.nextInt(11 - 1) + 1;
        System.out.println(numberForDocument);                          ///////////////////burası silinecek
        System.out.println(numberForQuestions);                         ///////////////////burası silinecek
    }
    //Arkadaş Sevgili grup ayrımı kontrol altına alındı metot ile
    public void arkadasSevgiliGrupAyrimi(){
        if (grupmu == true){
            collectionName = "GrupSorular";
            buttongorunurlugu();
        }else if (sevgilimi == true){
            collectionName = "SevgiliSorular";
            heartgif.setVisibility(View.VISIBLE);
        }else if (grupmu == false && sevgilimi == false){
            collectionName = "ArkadasSorular";
        }
    }

    //Soruları Veritabanından Çekme
    public void getDataFromFirestore(){
        numberRandom();
        DocumentReference documentReference = firebaseFirestore.collection(collectionName).document(String.valueOf(numberForDocument));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                            Map<String,Object> data = document.getData();
                            soru = data.get("Soru"+ numberForQuestions).toString();
                            if (grupmu == false){
                                soruText.setText(soruyucevaplayan + " , " + soruyusoran + " " + soru );
                            }else {
                                soruText.setText(soru);
                            }
                            if (soruNo > 10){
                                numberForNiceWord = random.nextInt(10 - 1) + 1;
                                soruText.setText(yorumlar.get(numberForNiceWord));
                                soruText.setBackground(ContextCompat.getDrawable(questionDisplayActivity.this,R.drawable.buttonwhite));
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) soruText.getLayoutParams();
                                layoutParams.setMargins(8,350,8,0);
                                soruText.setPadding(20,4,20,4);
                                soruText.setLayoutParams(layoutParams);


                        }
                    }
                }
            }
        });
    }

    public void bildiButtonSettings(){
        bildiButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonbildigreen));
        bilmediButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        unutmusButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        bildiButton.setEnabled(false);
        bilmediButton.setEnabled(true);
        unutmusButton.setEnabled(true);
    }

    //Bu metodun amacı devam et tıklandığında bildi butonunun işlevlerini devam ettirmesi
    public void listenerBildiSettings(){
            bildiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (score >= 0){
                        score = score + 10;
                        //scoreText.setText(score + "/100");
                        bildiButtonSettings();
                        listenerBildiSabit();
                        if (v.getId() == R.id.bildiButton){
                            buttontiklanmaKontrolu = true;
                        }
                    }
                }
            });
    }

    public void listenerBildiSabit(){                                        // 2.Defa bildi butonu tıklanınca puan artmasını önleme
        bildiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score > 0){
                    //scoreText.setText(score + "/100");
                    bildiButtonSettings();
                }
            }
        });
    }

    public void bildi(View view){
        if (view.getId() == R.id.bildiButton){                             //Herhangi bir şık işaretlendiyse referans almak için
            buttontiklanmaKontrolu = true;
        }
    bildiButtonSettings();
    //Puan artmasını sağlama
        if (score >= 0){
            score = score + 10;
            //scoreText.setText(score + "/100");
        }
        // 2.Defa bildi butonu tıklanınca puan artmasını önleme
        listenerBildiSabit();
    }
    public void bilmedi(View view){
        bildiButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        bilmediButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonbilmedired));
        unutmusButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        bilmediButton.setEnabled(false);
        bildiButton.setEnabled(true);
        unutmusButton.setEnabled(true);
        if (view.getId() == R.id.bilmediButton){                          //Herhangi bir şık işaretlendiyse referans almak için
            buttontiklanmaKontrolu = true;
        }
    }
    public void unutmus(View view){
        bildiButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        bilmediButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        unutmusButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonunutmusyellow));
        unutmusButton.setEnabled(false);
        bildiButton.setEnabled(true);
        bilmediButton.setEnabled(true);
        if (view.getId() == R.id.unutmusButton){                           //Herhangi bir şık işaretlendiyse referans almak için
            buttontiklanmaKontrolu = true;
        }
    }
    public void devamet(View view){
        if (buttontiklanmaKontrolu == true){
            soruNo++;
            if (soruNo < 11){
                getDataFromFirestore();                                  //Yeni soruyu getirme
                listenerBildiSettings();                                 //Bildi Butonunun işlevini devam ettirmesi için
                gerisayimozelligi();
                allMainSettings();                                       //Butonların ana ayarları
                sorusayimText.setText("Soru: " + soruNo + "/10");
                buttontiklanmaKontrolu = false;
            }
        }else {
            Toast.makeText(this,"Lütfen bir şık işaretleyin",Toast.LENGTH_SHORT).show();
        }

        if (soruNo > 10){                                        //Oyun sonu Kontrol altına alma, Sonuç ekranına yönlendirme işlemleri
            getDataFromFirestore();
            buttongorunurlugu();                                 //3 Butonu görünmez yapması için
            devametButton.setEnabled(true);
            sorusayimText.setText("Test Bitti");
            devametButton.setText("Sonucu görmek için tıklayın");

            devametButton.setOnClickListener(new View.OnClickListener() {               ////Sonuç Ekranına Gönderme
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(questionDisplayActivity.this,resultActivity.class);
                    intent.putExtra("sorusoran",soruyusoran);
                    intent.putExtra("sorucevaplayan",soruyucevaplayan);
                    intent.putExtra("scorevalue",score);
                    intent.putExtra("sevgilimi",sevgilimi);
                    intent.putExtra("grupmu",grupmu);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    //devamet tıklanınca butonların ana ayarlarına dönmesini sağlama
    public void allMainSettings(){
        bildiButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        bilmediButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        unutmusButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonwhite));
        devametButton.setBackground(ContextCompat.getDrawable(this,R.drawable.buttonred));
        devametButton.setTextColor(ContextCompat.getColor(this,R.color.white));
        devametButton.setEnabled(false);
        bildiButton.setEnabled(true);
        bilmediButton.setEnabled(true);
        unutmusButton.setEnabled(true);
    }

    public void gerisayimozelligi(){
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                devametButton.setText("Devam et " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                devametButton.setText("Devam et");
                devametButton.setTextColor(ContextCompat.getColor(questionDisplayActivity.this,R.color.redred));
                devametButton.setBackground(ContextCompat.getDrawable(questionDisplayActivity.this,R.drawable.buttonwhite));
                devametButton.setEnabled(true);
                if (grupmu == true){
                    buttontiklanmaKontrolu = true;
                }
            }
        }.start();
    }

    public void sorubitimiyorumlar(){
        yorumlar.add("İnsanlar böyledir; bir şey beklemedikleri kimseleri tanımazlar pek.   - Plautus"); //Plautus
        yorumlar.add("İnsanların birbirlerini tanımaları için en iyi zaman, ayrılmalarına yakın zamandır.   - Dostoyevski"); //Dostoyevski
        yorumlar.add("Erkek hoşlandıktan sonra tanır, kadın tanıdıkça hoşlanır.   - Milan Kundera"); //Milan Kundera
        yorumlar.add("Kendini tanımayanlar, başkalarını da tanıyamaz.   - Quintilian"); //Quintilian
        yorumlar.add("İnsan yalnızca, kendine acı çektirenleri tanır.   - Goethe"); //Goethe
        yorumlar.add("İnsanları iyi tanıyın, her insanı fena bilip kötülemeyin, her insanı da iyi bilip övmeyin!   - Mevlana"); //Mevlana
        yorumlar.add("Tanıdığın her insanda, tanımadığın bir insan gizlidir.   - İmam Râfii"); //İmam Râfii
        yorumlar.add("İnsanları tanımak için onları sınamaktan korkma; çünkü kaybedilmesi gerekenleri, en önce kaybetmelisin.   - Lucius Annaeus Seneca"); //Lucius Annaeus Seneca
        yorumlar.add("Başkalarını tanımak akıllılıktır; kendini tanımak bilgeliktir. Başkalarını yönetmek kuvvettir; kendini yönetmek iradedir.   - Lao Tsu"); //Lao Tsu
    }

}

//Biraz daha güzel söz eklenecek