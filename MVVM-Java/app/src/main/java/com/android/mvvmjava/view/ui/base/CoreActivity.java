package com.android.mvvmjava.view.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mvvmjava.MvvmApp;

abstract public class CoreActivity extends AppCompatActivity {

    protected MvvmApp mvvmApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvvmApp = (MvvmApp) getApplication();
    }
}
