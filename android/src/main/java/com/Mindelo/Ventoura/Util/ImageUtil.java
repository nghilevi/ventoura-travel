package com.Mindelo.Ventoura.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ImageUtil {
	
	private static final String TAG = "ImageUtil";

	/**
	 * in case decodeFile out of memory, we need to resize the image to reduce
	 * file size
	 */
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * decode the file to bitmap as its original size
	 */
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/*
	 * convert an Uri resource to a bitmap image
	 */
	public static Bitmap decodeUri(Uri selectedImage,
			ContentResolver contentResolver, int reqWidth, int reqHeight) {
		Bitmap bmp = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			String path = getImageRealPathFromURI(selectedImage,
					contentResolver);
			File f = new File(path);
			InputStream in = new FileInputStream(f);
			BitmapFactory.decodeStream(in, null, o);

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inPreferredConfig = Config.RGB_565;
			o2.inDither = true;
			o2.inSampleSize = calculateInSampleSize(o, reqWidth, reqHeight);
			bmp = BitmapFactory.decodeStream(
					contentResolver.openInputStream(selectedImage), null, o2);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return bmp;
	}

	public static Bitmap decodeUriWithRotateDegree(Uri selectedImage,
			ContentResolver contentResolver, int degree, int reqWidth,
			int reqHeight) {
		Bitmap bmp = decodeUri(selectedImage, contentResolver, reqWidth,
				reqHeight);
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
				matrix, true);
		return bmp;
	}

	/**
	 * copy and save the bitmap image to a desFile.
	 */
	public static void copyImageFile(Bitmap bmp, File desFile) {
		try {
			OutputStream out = new FileOutputStream(desFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public static Bitmap resizeCropImage() {
		return null;

	}

	public static byte[] BitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();

	}

	public static Bitmap BytesToBitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable bytesToDrawable(byte[] b) {
		Drawable image = null;
		image = new BitmapDrawable(
				BitmapFactory.decodeByteArray(b, 0, b.length));
		return image;
	}

	/**
	 * resize the image to 320*320
	 */
	public static byte[] resizeImage(byte[] input) {
		Bitmap original = BitmapFactory.decodeByteArray(input, 0, input.length);
		Bitmap resized = Bitmap.createScaledBitmap(original,
				ConfigurationConstant.SHRINK_USER_IMAGE_WIDTH,
				ConfigurationConstant.SHRINK_USER_IMAGE_HEIGHT, true);
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.PNG, 0, blob);
		return blob.toByteArray();
	}

	public static int readImageRotateDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static String getImageRealPathFromURI(Uri contentURI,
			ContentResolver contentResolver) {
		String result;
		Cursor cursor = contentResolver.query(contentURI, null, null, null,
				null);
		if (cursor == null) {
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	public static Bitmap rotateImage(Bitmap bmp, int d) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(d);
		// Rotating Bitmap
		Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
		return rotatedBMP;
	}

}
