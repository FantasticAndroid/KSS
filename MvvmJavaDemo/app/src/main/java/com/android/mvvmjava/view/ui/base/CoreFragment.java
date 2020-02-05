package com.android.mvvmjava.view.ui.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.mvvmjava.MvvmApp;

abstract public class CoreFragment extends Fragment {

    protected MvvmApp mvvmApp;
    protected CoreActivity coreActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mvvmApp = (MvvmApp) context.getApplicationContext();
        coreActivity = (CoreActivity) context;
    }
}
