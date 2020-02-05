package com.android.mvvmjava.view.ui;

import android.os.Bundle;

import com.android.mvvmjava.R;
import com.android.mvvmjava.view.ui.base.CoreActivity;

public class MainActivity extends CoreActivity {

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
