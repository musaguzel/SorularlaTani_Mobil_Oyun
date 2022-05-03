package com.musaguzel.sorularlatani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private RewardedAd mRewardedAd;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    int value;
    int a = 0 , g = 0 , s = 0;
    TextView textView,elmasSayisi;
    Button reklambuton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Tanımlamalar
        sharedPreferences = this.getSharedPreferences("com.musaguzel.sorularlatani", Context.MODE_PRIVATE);
        textView = findViewById(R.id.textView);
        elmasSayisi= findViewById(R.id.diamondNumberText);
        reklambuton = findViewById(R.id.reklamButton);
        value = sharedPreferences.getInt("degerliPuan",1000);
        elmasSayisi.setText(Integer.toString(value));

            //Reklam sdk initialize etme
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //Ödüllü reklam kodları
        loadads();
        reklambuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showads();

            }
        });
            //Banner reklam initialize etme
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Interstitial reklam initialize
        loadInterstitial();

            //İnternet kontrolü
        if (!haveNetwork()){
            Toast.makeText(this,"İnternet bağlantınızı kontrol edin",Toast.LENGTH_SHORT).show();
        }
    }
        //**************************Buttonlar**************************

    public void teksorular(View view){
        a++;
       if (!haveNetwork()){
           internetuyari();
        }else if (value < 1000){
           Toast.makeText(this,"Yetersiz Elmas",Toast.LENGTH_SHORT).show();
       }if (haveNetwork() && value >= 1000){
            Intent intent = new Intent(MainActivity.this,GetNameActivity.class);
            startActivity(intent);
        }
        if (a == 2 || a == 4 || a == 6 || a == 8 || a == 10 || a == 12 || a == 14){
            showAdsInter();
        }
    }
    public void grupsorular(View view){
        g++;
        if (!haveNetwork()){
            internetuyari();
        }else if(value < 2000) {
            Toast.makeText(this,"Yetersiz Elmas",Toast.LENGTH_SHORT).show();
        }if (haveNetwork() && value >= 2000){
            boolean grup = true;
            Intent intent = new Intent(MainActivity.this,GroupActivity.class);
            intent.putExtra("grupmu",grup);
            startActivity(intent);
        }
        if (g == 2 || g == 4 || g == 6 || g == 8 || g == 10 || g == 12 || g == 14){
            showAdsInter();
        }

    }
    public void sevgilisorular(View view){
        s++;
        if (!haveNetwork()){
            internetuyari();
        }else if (value < 3000){
            Toast.makeText(this,"Yetersiz Elmas",Toast.LENGTH_SHORT).show();
        }if (haveNetwork() && value >= 3000){
            boolean sevgili = true;
            Intent intent = new Intent(MainActivity.this,GetNameActivity.class);
            intent.putExtra("sevgilimi",sevgili);
            startActivity(intent);
        }
        if (s == 2 || s == 4 || s == 6 || s == 8 || s == 10 || s == 12 || s == 14){
            showAdsInter();
        }

    }
        //**************************INTERNET KONTROLU**************************

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
            //**************************INTERNET YOK ISE UYARI VERMESİNİ SAGLAMA**************************
    public void internetuyari(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Tekrar Dene");
        alert.setMessage("İnternet bağlantınızı kontrol edin");
        alert.setPositiveButton("Tekrar dene", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }


                //**************************GEÇİŞ REKLAMIN KODLARI**************************

        private void loadInterstitial(){

            AdRequest adRequestInter = new AdRequest.Builder().build();

            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequestInter, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    mInterstitialAd = null;
                }
            });

        }

        private void beforeShowCallBackAdsInter(){

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.

                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
                    loadInterstitial();
                }
            });

        }

        private void showAdsInter(){
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                beforeShowCallBackAdsInter();
            }
        }



                 //**************************Ödüllü REKLAMIN KODLARI**************************

    private void loadads(){

        AdRequest adRequestReward = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequestReward, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }


    private void beforeShowCallBackAds(){

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                mRewardedAd = null;
                loadads();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
            }
        });

    }

    private void showads(){

        if (mRewardedAd != null) {
            Activity activityContext = MainActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    value = value + 50000;
                    elmasSayisi.setText(Integer.toString(value));
                    sharedPreferences.edit().putInt("degerliPuan",value).apply();
                }
            });
            beforeShowCallBackAds();
        }

    }

}
