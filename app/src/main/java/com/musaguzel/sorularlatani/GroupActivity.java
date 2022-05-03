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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class GroupActivity extends AppCompatActivity {
    private AdView mAdView;
    SharedPreferences sharedPreferencesgrup;
    TextView txtTalimatBaslik,txtTalimatBirText,txtTalimatIkiText,txtTalimatUcText,txtTalimatDortText;
    EditText groupName;
    String groupname;
    boolean grupmu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        //Tanımlamalar
        txtTalimatBaslik = findViewById(R.id.txtTalimatBaslik);
        txtTalimatBirText = findViewById(R.id.txtTalimatBirText);
        txtTalimatIkiText = findViewById(R.id.txtTalimatIkiText);
        txtTalimatUcText = findViewById(R.id.txtTalimatUcText);
        txtTalimatDortText = findViewById(R.id.txtTalimatDortText);
        groupName = findViewById(R.id.groupNameEditText);
        sharedPreferencesgrup = this.getSharedPreferences("com.musaguzel.sorularlatani", Context.MODE_PRIVATE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewgroup);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Intent ile grupmu degerini alma
        Intent intent = getIntent();
        grupmu = intent.getBooleanExtra("grupmu",false);
    }

    public void basla(View view){

        if (!haveNetwork()){
            internetuyari();
        }else if (!groupName.getText().toString().matches("")){
            elmasazaltma();
            groupname = groupName.getText().toString();
            Intent intent = new Intent(GroupActivity.this,questionDisplayActivity.class);
            intent.putExtra("grupmu",grupmu);
            intent.putExtra("groupname",groupname);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            groupName.setHintTextColor(ContextCompat.getColor(this,R.color.brown));
            Toast.makeText(GroupActivity.this,"Lütfen bir grup adı belirleyin",Toast.LENGTH_SHORT).show();
        }
    }

    public void elmasazaltma(){
        int elmas = sharedPreferencesgrup.getInt("degerliPuan",1000);
        elmas = elmas - 2000;
        sharedPreferencesgrup.edit().putInt("degerliPuan",elmas).apply();
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