package com.bhaskar.darwin.ui.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bhaskar.darwin.ui.sqldemo.provider.OfferPointsUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        useSqlLite();
    }

    private void useSqlLite() {
        OfferPointsUtils.insertOrUpdatePoints((MainApp) getApplication(),
                1);
        OfferPointsUtils.getAllPointsAndSyncToServer((MainApp) getApplication());
    }
}
