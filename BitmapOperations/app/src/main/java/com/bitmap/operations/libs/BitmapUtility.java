package com.bitmap.operations.libs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class BitmapUtility {
	/***
	 * To Rotate a bitmap to given rotationAngle,
	 * 
	 * @param bitmap
	 * @param rotationAngle
	 *            , positive for CW, negative for CCW
	 * @return Bitmap with rotationAngle Rotate
	 */
	public static Bitmap getRotatedBitmap(Bitmap bitmap, float rotationAngle) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		// to rotate bitmap with its center
		mtx.postRotate(rotationAngle, w / 2, h / 2);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		return bitmap;
	}

	/***
	 * Filp bitmap to the horizontal OR vertical direction
	 * 
	 * @param bitmap
	 * @param isVertical
	 * @return
	 */
	public static Bitmap applyFlipOnSelectedComicBitmap(Bitmap bitmap,
			boolean isVertical) {
		Matrix matrix = new Matrix();
		if (isVertical)
			matrix.preScale(1.0f, -1.0f);
		else
			matrix.preScale(-1.0f, 1.0f);

		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * To Apply border to the bitmap with given width and color with optional
	 * pathEffect
	 * 
	 * @param bitmap
	 * @param strokeWidth
	 * @param colorCode
	 * @param pe
	 *            (optional)
	 * @return
	 */
	public static Bitmap getRectangleBorderBitmap(Bitmap bitmap,
			final float strokeWidth, final int colorCode, PathEffect pe) {
		// Crate a mutable bitmap
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		// remove zigzag from line
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(colorCode);

		paint.setStyle(Style.STROKE);
		// paint.setDither(true);
		paint.setStrokeWidth(strokeWidth);
		// To apply zigzag black border
		// DiscretePathEffect dpe = new DiscretePathEffect(1.0f,1.0f);

		paint.setPathEffect(pe); // pe can be null also
		canvas.drawBitmap(bitmap, rect, rect, null);
		canvas.drawRect(rect, paint);
		return output;
	}

	/**
	 * To Apply border to the bitmap with given width and color with optional
	 * pathEffect
	 * 
	 * @param bitmap
	 * @param strokeWidth
	 * @param colorCode
	 * @param pe
	 *            (optional)
	 * @return
	 */
	public static Bitmap getRoundRectangleBorderBitmap(Bitmap bitmap,
			final float strokeWidth, final float xRadious,
			final float yRadious, final int colorCode, PathEffect pe) {
		bitmap = BitmapUtility.getRoundedCornerBitmap(bitmap,
				(int) (xRadious + yRadious) / 2);
		// Crate a mutable bitmap
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(0, 0, bitmap.getWidth(),
				bitmap.getHeight());

		// remove zigzag from line
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(colorCode);

		paint.setStyle(Style.STROKE);
		// paint.setDither(true);
		paint.setStrokeWidth(strokeWidth);
		// To apply zigzag black border
		// DiscretePathEffect dpe = new DiscretePathEffect(1.0f,1.0f);

		paint.setPathEffect(pe); // pe can be null also
		canvas.drawBitmap(bitmap, rect, rectF, null);
		// canvas.drawRect(rect,paint);
		canvas.drawRoundRect(rectF, xRadious, yRadious, paint);
		return output;
	}

	/***
	 * 
	 * @param bitmap
	 * @param intensity
	 *            (0 to 180) 0 mean full contrast
	 * @return Bitmap
	 * @throws NullPointerException
	 */
	public static Bitmap getBitmapWithContrast(Bitmap bitmap, int intensity)
			throws NullPointerException {
		// Angle range should be 0 to 180
		intensity = 100 - intensity;
		int mAngle = (int) ((float) intensity * 1.80f);
		// mAngle += 1;
		if (mAngle > 180) {
			mAngle = 0;
		}

		// convert our animated angle [-180...180] to a contrast value of
		// [-1..1]
		float contrast = (float) mAngle / 180.f;
		float scale;

		if (contrast == 1.0f)
			scale = contrast + 5.f;
		else if (contrast >= 0.95f && contrast < 1.0f)
			scale = contrast + 3.5f;
		else if (contrast >= 0.90f && contrast < 0.95f)
			scale = contrast + 2.0f;
		else
			scale = contrast + 1.f;

		/*
		 * 5x4 matrix for transforming the color+alpha components of a Bitmap.
		 * The matrix is stored in a single array, and its treated as follows: [
		 * a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t ] When
		 * applied to a color [r, g, b, a], the resulting color is computed as
		 * (after clamping) R' = a*R + b*G + c*B + d*A + e; G' = f*R + g*G + h*B
		 * + i*A + j; B' = k*R + l*G + m*B + n*A + o; A' = p*R + q*G + r*B + s*A
		 * + t;
		 */
		ColorMatrix colorMatrix = new ColorMatrix();

		colorMatrix.set(new float[] { scale, 0, 0, 0, 0, 0, scale, 0, 0, 0, 0,
				0, scale, 0, 0, 0, 0, 0, 1, 0 });

		ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(
				colorMatrix);

		Paint paint = new Paint();
		// remove any old color filter
		paint.setColorFilter(null);
		paint.setColorFilter(colorFilter);

		Bitmap mutableBitmap = bitmap.copy(Config.ARGB_8888, true);

		new Canvas(mutableBitmap).drawBitmap(mutableBitmap, 0, 0, paint);
		Runtime.getRuntime().gc();
		System.gc();
		return mutableBitmap;
	}

	/***
	 * @param bitmap
	 *            (an initially white color bitmap)
	 * @param color
	 * @param opacity
	 *            in percentage
	 * @return Bitmap
	 * @throws NullPointerException
	 */
	public static Bitmap getImageBitmapWithColor(Bitmap bitmap, int color,
			int opacity) throws NullPointerException {
		float Red = Color.red(color);
		float G = Color.green(color);
		float B = Color.blue(color);

		float[] colorTransform = { Red / 255f, 0, 0, 0, 0, // R color
				0, G / 255f, 0, 0, 0 // G color
				, 0, 0, B / 255f, 0, 0 // B color
				, 0, 0, 0, 1f, 0f };

		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0f); // Remove Colour

		colorMatrix.set(colorTransform);
		ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(
				colorMatrix);

		Paint paint = new Paint();
		paint.setColorFilter(colorFilter);
		paint.setAlpha((int) (opacity * 2.55f));
		Bitmap mutableBitmap = null;
		mutableBitmap = bitmap.copy(Config.ARGB_8888, true);

		new Canvas(mutableBitmap).drawBitmap(mutableBitmap, 0, 0, paint);
		Runtime.getRuntime().gc();
		System.gc();
		return mutableBitmap;
	}

	/****
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, final int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		// final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/****
	 * @param bitmap
	 * @param strokeWidth
	 * @param colorCode
	 * @return
	 */
	public static Bitmap getBitmapViaMode(Bitmap bitmap,
			final float strokeWidth, final int colorCode, Mode modeType) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(colorCode);
		paint.setStrokeWidth(strokeWidth);
		canvas.drawRoundRect(rectF, strokeWidth, strokeWidth, paint);

		paint.setXfermode(new PorterDuffXfermode(modeType));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/*****
	 * @param orgBitmap
	 * @param newWidth
	 * @param newHeight
	 * @param newBitmapIsLarge
	 * @return
	 */
	public static Bitmap getRescaledBitmap(Bitmap orgBitmap, int newWidth,
			int newHeight, boolean newBitmapIsLarge) {
		return Bitmap.createScaledBitmap(orgBitmap, newWidth, newHeight,
				newBitmapIsLarge);
	}

	/****
	 * @param bitmap
	 * @param strokeWidth
	 * @param colorCode
	 * @param pe
	 * @return
	 */
	public static Bitmap getRectangleInnerBorderBitmap(Bitmap bitmap,
			final int strokeWidth, final int colorCode, PathEffect pe) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setStyle(Style.STROKE);
		paint.setDither(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setPathEffect(pe);

		canvas.drawBitmap(bitmap, rect, rect, null);
		paint.setColor(Color.parseColor("#ffffff"));
		canvas.drawRect(rect, paint);
		final Rect rectInner = new Rect(strokeWidth / 2, strokeWidth / 2,
				bitmap.getWidth() - strokeWidth / 2, bitmap.getHeight()
						- strokeWidth / 2);

		paint.setStrokeWidth(5);
		paint.setColor(colorCode);
		canvas.drawRect(rectInner, paint);

		return output;
	}

	public static Bitmap mergeTwoBitmaps(Bitmap base, Bitmap overlay) {
		int w = base.getWidth();
		int h = base.getHeight();

		int adWDelta = (int) (w - overlay.getWidth()) / 2;
		int adHDelta = (int) (h - overlay.getHeight()) / 2;

		Bitmap mBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		canvas.drawBitmap(base, 0, 0, null);
		canvas.drawBitmap(overlay, adWDelta, adHDelta, null);
		return mBitmap;
	}

	/**
	 * @param selectedColorCode
	 * @param rect
	 * @param paint
	 */
	public static void applyRadialGradientToRect(int selectedColorCode,
			Rect rect, Paint paint) {
		int r = Color.red(selectedColorCode);
		int g = Color.green(selectedColorCode);
		int b = Color.blue(selectedColorCode);

		int selectedColorCode_withoutAlpha = Color.argb(0, r, g, b);

		int[] colorarray = { selectedColorCode_withoutAlpha, selectedColorCode };
		float[] positions = { 0.f, 1.f };

		RadialGradient luarM = new RadialGradient(rect.centerX(),
				rect.centerY(), rect.width() / 2, colorarray, positions,
				TileMode.CLAMP);
		paint.setShader(luarM);

	}

	/**
	 * @param tempStorageForBitmapByteArrayHolder
	 *            (Temp storage to use for decoding. Suggest 16K or so.)(default
	 *            you can provide (16*1024)
	 * @return
	 */
	public static BitmapFactory.Options getOptionsForBitmap(
			int tempStorageForBitmapByteArrayHolder) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = 1;
		opt.inTempStorage = new byte[tempStorageForBitmapByteArrayHolder];
		return opt;
	}

	/*****
	 * @param context
	 * @param Path
	 *            (Assest Path)
	 * @param fileName
	 *            (ex. abc.png) * @param tempStorageForBitmapByteArrayHolder
	 *            (Temp storage to use for decoding. Suggest 16K or so.)(default
	 *            you can provide (16*1024)
	 * @return Bitmap
	 */
	public static Bitmap getBitmapImageFromAssetPath(Context context,
			String Path, String fileName,
			int tempStorageForBitmapByteArrayHolder) {
		InputStream istr = null;
		try {
			istr = context.getAssets().open(Path + "/" + fileName);
		} catch (IOException e) {
			Log.v("Exception in ", "getBitmapImageFromAssetPath");
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(istr, null, BitmapUtility
				.getOptionsForBitmap(tempStorageForBitmapByteArrayHolder));
	}

	/**
	 * @param jsonURL
	 * @param connectionTimeOutTIME
	 *            (in miliSeconds)
	 * @param tempStorageForBitmapByteArrayHolder
	 *            default you can provide (16*1024)
	 * @param readTimeOutTIME
	 *            (in miliSeconds)
	 * @return
	 */
	public static Bitmap getImageBitmapFromJSONUrl(String jsonURL,
			int connectionTimeOutTIME, int readTimeOutTIME,
			int tempStorageForBitmapByteArrayHolder) {
		URLConnection conn = null;
		InputStream input = null;
		try {
			URL updateURL = new URL(jsonURL);
			conn = updateURL.openConnection();

			conn.setRequestProperty("Content-Language", "en-US");
			conn.setConnectTimeout(connectionTimeOutTIME);
			conn.setReadTimeout(readTimeOutTIME);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			input = conn.getInputStream();

			BufferedInputStream bis = new BufferedInputStream(input);
			Bitmap bm = BitmapFactory.decodeStream(bis, null, BitmapUtility
					.getOptionsForBitmap(tempStorageForBitmapByteArrayHolder));
			bis.close();
			return bm;
		} catch (Exception e) {
			System.out.println("error=" + e.getMessage());
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception e) {
				System.out.println("error=" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * @param originalImage
	 *            The original Bitmap image used to create the reflection
	 * @param reflectionGap
	 *            The gap we want between the reflection and the original image
	 * @param reflectedBitmapHeightInPercentage
	 *            (0 to 100) height of new reflected bitmap should be % of
	 *            original bitmap
	 * 
	 * @return the bitmap with a reflection
	 */
	public static Bitmap createReflectedImage(Bitmap originalImage,
			final int reflectionGap, int reflectedBitmapHeightInPercentage) {

		try {
			if (reflectedBitmapHeightInPercentage > 100)
				reflectedBitmapHeightInPercentage = 100;

			float heightF = (float) reflectedBitmapHeightInPercentage / 100.f;

			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			int reflectedBitmapHeight = (int) (height * heightF);
			// Create a Bitmap with the flip matrix applied to it.
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0,
					width, height, matrix, true);
			// reflectionImage = Bitmap.createScaledBitmap(reflectionImage,
			// width, reflectedBitmapHeight, true);

			// create a new bitmap with specified height
			reflectionImage = Bitmap.createBitmap(reflectionImage, 0, 0, width,
					reflectedBitmapHeight);

			// Create a new bitmap with same width but taller to fit reflection
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + reflectedBitmapHeight), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(originalImage, 0, 0, null);
			// Draw in the gap
			canvas.drawRect(0, height, width, height + reflectionGap,
					new Paint());
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			// Create a shader that is a linear gradient that covers the
			// reflection
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);

			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			return bitmapWithReflection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// see http://androidsnippets.com/create-image-with-reflection

}
