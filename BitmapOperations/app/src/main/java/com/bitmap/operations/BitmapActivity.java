package com.bitmap.operations;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.bitmap.operations.libs.BitmapUtility;

public class BitmapActivity extends Activity {
    private Bitmap customBitmap;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bitmap);
        imageView = (ImageView) findViewById(R.id.imageView);
        customBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.test);
        ((Button) findViewById(R.id.rotateCCW))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.rotateCW))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.hFlip)).setOnClickListener(clickListener);
        ((Button) findViewById(R.id.vFlip)).setOnClickListener(clickListener);
        ((Button) findViewById(R.id.enlarge)).setOnClickListener(clickListener);
        ((Button) findViewById(R.id.compress))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.withBorder))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.withRoundedCorner))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.contrast))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.Color)).setOnClickListener(clickListener);
        ((Button) findViewById(R.id.reset)).setOnClickListener(clickListener);
        ((Button) findViewById(R.id.innerBorder))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.mergeBitmap))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.roundRectBorder))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.borderWithPathEffect))
                .setOnClickListener(clickListener);
        ((Button) findViewById(R.id.reflection))
                .setOnClickListener(clickListener);
    }

    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            Bitmap applyBitmap = null;
            switch (v.getId()) {
                case R.id.rotateCCW:
                    applyBitmap = BitmapUtility.getRotatedBitmap(customBitmap, -45);
                    break;
                case R.id.rotateCW:
                    applyBitmap = BitmapUtility.getRotatedBitmap(customBitmap, +45);
                    break;
                case R.id.hFlip:
                    applyBitmap = BitmapUtility.applyFlipOnSelectedComicBitmap(
                            customBitmap, false);
                    break;
                case R.id.vFlip:
                    applyBitmap = BitmapUtility.applyFlipOnSelectedComicBitmap(
                            customBitmap, true);
                    break;
                case R.id.enlarge:
                    applyBitmap = BitmapUtility
                            .getRescaledBitmap(
                                    customBitmap,
                                    customBitmap.getWidth()
                                            + customBitmap.getWidth() / 2,
                                    customBitmap.getHeight()
                                            + customBitmap.getHeight() / 2, true);
                    break;
                case R.id.compress:
                    applyBitmap = BitmapUtility
                            .getRescaledBitmap(
                                    customBitmap,
                                    customBitmap.getWidth()
                                            - customBitmap.getWidth() / 2,
                                    customBitmap.getHeight()
                                            - customBitmap.getHeight() / 2, false);
                    break;
                case R.id.withBorder:
                    applyBitmap = BitmapUtility.getRectangleBorderBitmap(
                            customBitmap, 10, Color.RED, null);
                    break;
                case R.id.innerBorder:
                    applyBitmap = BitmapUtility.getRectangleInnerBorderBitmap(
                            customBitmap, 15, Color.RED, null);
                    break;
                case R.id.withRoundedCorner:
                    applyBitmap = BitmapUtility.getRoundedCornerBitmap(
                            customBitmap, 15);
                    break;
                case R.id.contrast:
                    applyBitmap = BitmapUtility.getBitmapWithContrast(customBitmap,
                            45);
                    break;
                case R.id.Color:
                    applyBitmap = BitmapUtility.getImageBitmapWithColor(
                            customBitmap, Color.RED, 255);
                    break;
                case R.id.mergeBitmap:
                    applyBitmap = BitmapUtility.mergeTwoBitmaps(customBitmap,
                            BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.ic_launcher));
                    break;
                case R.id.roundRectBorder:

                    applyBitmap = BitmapUtility.getRoundRectangleBorderBitmap(
                            customBitmap, 3, 15.0f, 15.0f, Color.RED, null);
                    break;

                case R.id.borderWithPathEffect:
                    DiscretePathEffect dpe = new DiscretePathEffect(2.0f, 2.0f);
                    applyBitmap = BitmapUtility.getRectangleBorderBitmap(
                            customBitmap, 7, Color.RED, dpe);
                    break;
                case R.id.reflection:
                    final int reflectionGap = 4;
                    applyBitmap = BitmapUtility.createReflectedImage(customBitmap,
                            reflectionGap, 100);
                    break;
                case R.id.reset:
                    imageView.setImageBitmap(customBitmap);
                    break;
            }
            if (applyBitmap != null)
                imageView.setImageBitmap(applyBitmap);
        }
    };
}
