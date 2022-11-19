package com.uniminuto.chiptransport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ManualUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_user);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}