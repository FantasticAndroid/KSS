package com.android.internetradio.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.android.internetradio.R;

public final class TransAviMsgLoader extends Dialog {

    /**
     * cancelable is false by default
     * @param context
     */
    public TransAviMsgLoader(@NonNull Activity context) {
        super(context, R.style.TransLoader);
        setCancelable(false);
    }

    /**
     * @param context
     * @param isCancelable default is false
     */
    public TransAviMsgLoader(@NonNull Activity context, boolean isCancelable) {
        super(context, R.style.TransLoader);
        setCancelable(isCancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loader);

    }
}