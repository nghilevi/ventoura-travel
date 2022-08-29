package com.Mindelo.Ventoura.Util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

/*
 * this class contains some utility method related to BitMap things
 */
public class BitmapUtil {
	/**
	 * 
	 * @param context
	 * @param resource
	 *            Id
	 * @return BitMap, get a bitmap from resource (drawable) id
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int drawableId,
			int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}

	@SuppressWarnings("deprecation")
	public static Drawable getBitmapAdjustScreen(Context context,
			DisplayMetrics dm, int drawableId) {
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		// get the adjusting the screen picture.
		Bitmap bgBitmap = BitmapUtil.ReadBitmapById(context, drawableId,
				screenWidth, screenHeight);
		return new BitmapDrawable(bgBitmap);
	}

	/***
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
			int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;

		matrix.postScale(scale, scale2);

		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return b;
	}

	public static byte[] bitMapToByteArray(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static Bitmap byteArrayToBitMap(byte[] images, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(images, 0, images.length, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

}