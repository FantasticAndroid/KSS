package com.android.mvvmjava.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mvvmjava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            ProjectListFragment fragment = new ProjectListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragment, ProjectListFragment.TAG).commit();
        }
    }
}
