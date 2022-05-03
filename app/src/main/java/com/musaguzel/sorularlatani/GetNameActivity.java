package com.musaguzel.sorularlatani;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class GetNameActivity extends AppCompatActivity {
    private AdView mAdView;
    SharedPreferences sharedPreferencess;
    TextView textView,talimatBaslik,talimatBilgi;
    EditText soran,cevaplayan;
    boolean sevgilimi;
    String soruyusoranismi,soruyucevaplayanismi;
    Button baslaButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);
        //Tanımlamalar
        sharedPreferencess = this.getSharedPreferences("com.musaguzel.sorularlatani", Context.MODE_PRIVATE);
        textView = findViewById(R.id.textView2);
        talimatBaslik = findViewById(R.id.talimatBaslikText);
        talimatBilgi = findViewById(R.id.talimatBilgiText);
        soran = findViewById(R.id.soranEditText);
        cevaplayan = findViewById(R.id.cevaplayanEditText);
        baslaButton = findViewById(R.id.baslaButton);
        soruyusoranismi = "";
        soruyucevaplayanismi = "";
        //Reklam sdk initialize etme
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Intent ile sevgilimi değerini alma
        Intent intent = getIntent();
        sevgilimi = intent.getBooleanExtra("sevgilimi",false);
    }

    //Girilen isimler degiskene atanıp intent ile sorugöstrime aktarıldı, sevgili değeri gönderme kontrol altına alındı.
    public void basla(View view){
        soruyusoranismi = soran.getText().toString();
        soruyucevaplayanismi = cevaplayan.getText().toString();
        if (soruyusoranismi.isEmpty() || soruyucevaplayanismi.isEmpty()){
            soran.setHintTextColor(ContextCompat.getColor(this,R.color.redred));
            cevaplayan.setHintTextColor(ContextCompat.getColor(this,R.color.redred));
            Toast.makeText(GetNameActivity.this,"Lütfen İsimlerinizi Girin", Toast.LENGTH_SHORT).show();
        }else {

            if (!haveNetwork()){
                internetuyari();
            }else {
                elmasazaltma();
                Intent intent = new Intent(GetNameActivity.this,questionDisplayActivity.class);
                intent.putExtra("sorusoran",soruyusoranismi);
                intent.putExtra("sorucevaplayan",soruyucevaplayanismi);
                if (sevgilimi == true){
                    intent.putExtra("sevgilimi",sevgilimi);
            }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

        }
    }

public void elmasazaltma(){
    int elmas = sharedPreferencess.getInt("degerliPuan",1000);
    if (sevgilimi == true){
        elmas = elmas - 3000;
        sharedPreferencess.edit().putInt("degerliPuan",elmas).apply();
    }else {
        elmas = elmas - 1000;
        sharedPreferencess.edit().putInt("degerliPuan",elmas).apply();
    }

}


    private boolean haveNetwork(){
        boolean have_Wifi = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_Wifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;
        }
        return have_Wifi || have_MobileData;

    }

    public void internetuyari(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Tekrar Dene");
        alert.setMessage("İnternet bağlantınızı kontrol edin");
        alert.setPositiveButton("Tekrar dene", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              /*  if (!haveNetwork()){
                    internetuyari();
                }*/
            }
        });
        alert.show();
    }



}