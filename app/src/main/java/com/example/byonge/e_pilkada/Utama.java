package com.example.byonge.e_pilkada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
    }
    public void sukakarya(View v ){
        Intent hasilIntent = new Intent(Utama.this,Dua.class);
        String emp = "SUKAKARYA";
        hasilIntent.putExtra("kecamatan",emp);
        startActivity(hasilIntent);
        finish();
    }
    public void sukajaya(View v ){
        Intent hasilIntent = new Intent(Utama.this,Dua.class);
        String emp = "SUKAJAYA";
        hasilIntent.putExtra("kecamatan",emp);
        startActivity(hasilIntent);
        finish();
    }
}
