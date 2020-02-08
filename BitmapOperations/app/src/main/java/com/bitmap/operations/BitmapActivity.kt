package com.bitmap.operations

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.DiscretePathEffect
import android.os.Bundle
import android.view.View
import com.bitmap.operations.libs.BitmapUtility
import kotlinx.android.synthetic.main.layout_bitmap.*

class BitmapActivity : Activity() {
    private lateinit var customBitmap: Bitmap

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bitmap)
        customBitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
    }

    fun onButtonClick(v: View) {
        var applyBitmap: Bitmap? = null
        when (v.id) {
            R.id.rotateCCW -> applyBitmap = BitmapUtility.getRotatedBitmap(customBitmap, -45f)
            R.id.rotateCW -> applyBitmap = BitmapUtility.getRotatedBitmap(customBitmap, +45f)
            R.id.hFlip -> applyBitmap = BitmapUtility.applyFlipOnSelectedComicBitmap(
                customBitmap, false
            )
            R.id.vFlip -> applyBitmap = BitmapUtility.applyFlipOnSelectedComicBitmap(
                customBitmap, true
            )
            R.id.enlarge -> applyBitmap = BitmapUtility
                .getRescaledBitmap(
                    customBitmap,
                    customBitmap.width + customBitmap.width / 2,
                    customBitmap.height + customBitmap.height / 2, true
                )
            R.id.compress -> applyBitmap = BitmapUtility
                .getRescaledBitmap(
                    customBitmap,
                    customBitmap.width - customBitmap.width / 2,
                    customBitmap.height - customBitmap.height / 2, false
                )
            R.id.withBorder -> applyBitmap = BitmapUtility.getRectangleBorderBitmap(
                customBitmap, 10f, Color.RED, null
            )
            R.id.innerBorder -> applyBitmap = BitmapUtility.getRectangleInnerBorderBitmap(
                customBitmap, 15, Color.RED, null
            )
            R.id.withRoundedCorner -> applyBitmap = BitmapUtility.getRoundedCornerBitmap(
                customBitmap, 15
            )
            R.id.contrast -> applyBitmap = BitmapUtility.getBitmapWithContrast(customBitmap, 45)

            R.id.applyColor -> applyBitmap =
                BitmapUtility.getImageBitmapWithColor(customBitmap, Color.RED, 255)

            R.id.mergeBitmap -> applyBitmap = BitmapUtility.mergeTwoBitmaps(
                customBitmap,
                BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            )
            R.id.roundRectBorder ->
                applyBitmap = BitmapUtility.getRoundRectangleBorderBitmap(
                    customBitmap, 3f, 15.0f, 15.0f, Color.RED, null
                )

            R.id.borderWithPathEffect -> {
                val dpe = DiscretePathEffect(2.0f, 2.0f)
                applyBitmap =
                    BitmapUtility.getRectangleBorderBitmap(customBitmap, 7f, Color.RED, dpe)
            }
            R.id.reflection -> {
                val reflectionGap = 4
                applyBitmap = BitmapUtility.createReflectedImage(customBitmap, reflectionGap, 100)
            }
            R.id.reset -> imageView.setImageBitmap(customBitmap)
        }
        applyBitmap?.let {
            imageView.setImageBitmap(it)
        }
    }
}
