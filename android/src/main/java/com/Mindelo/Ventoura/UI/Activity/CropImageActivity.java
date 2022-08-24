package com.Mindelo.Ventoura.UI.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.UI.View.CropImageHighlightView;
import com.Mindelo.Ventoura.UI.View.CropImageView;
import com.Mindelo.Ventoura.Util.BackgroundUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CropImageActivity extends CropImageMonitoredActivity implements
		OnClickListener {

	private static final String TAG = "CropImageActivity";

	private ProgressDialog progressDialog;

	public boolean mSaving; // Whether the "save" button is already clicked.

	private Button btnDone, btnCancel, btnRotate;
	private TextView tvTitle;
	private CropImageHighlightView mCrop;
	private CropImageView mImageView;
	private Bitmap mBitmap;
	private final Handler mHandler = new Handler();
	private boolean mCircleCrop = false;

	private Runnable job = null;

	private static final int IMAGEROATAEDEGREE = 90;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		mImageView = (CropImageView) findViewById(R.id.image);

		// get the data form the calling activity
		Intent intent = this.getIntent();
		Uri selectedImage = intent.getData();
		String imgPath = ImageUtil.getImageRealPathFromURI(selectedImage,
				getContentResolver());
		int degree = ImageUtil.readImageRotateDegree(imgPath);

		// get the image to be cropped
		mBitmap = ImageUtil.decodeUriWithRotateDegree(selectedImage,
				getContentResolver(), degree,
				ConfigurationConstant.LARGE_USER_PORTAL_IMAGE_WIDTH,
				ConfigurationConstant.LARGE_USER_PORTAL_IMAGE_HEIGHT);

		mImageView.setImageBitmap(mBitmap);
		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		tvTitle = (TextView) this.findViewById(R.id.title_text);
		tvTitle.setText("Crop Image");
		btnRotate = (Button) this.findViewById(R.id.btn_action);
		btnRotate.setText("Rotate");

		btnCancel = (Button) findViewById(R.id.crop_image_cancel);
		btnDone = (Button) this.findViewById(R.id.crop_image_done);

		btnDone.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnRotate.setOnClickListener(this);

		startFaceDetection();
	}

	Thread CropImageTask = new Thread(new Runnable() {

		@Override
		public void run() {
			onSaveClicked();
		}
	});

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		super.onDestroy();
	}

	private void startFaceDetection() {
		mImageView.setImageBitmapResetBase(mBitmap, true);
		if (job != null) {
			job = null;
		}
		job = new Runnable() {
			public void run() {
				final CountDownLatch latch = new CountDownLatch(1);
				final Bitmap b = mBitmap;
				mHandler.post(new Runnable() {
					public void run() {
						if (b != mBitmap && b != null) {
							mImageView.setImageBitmapResetBase(b, true);
							mBitmap.recycle();
							mBitmap = b;
						}
						if (mImageView.getScale() == 1.0f) {
							mImageView.center(true, true);
						}
						latch.countDown();
					}
				});
				try {
					latch.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				mRunFaceDetection.run();
			}
		};
		BackgroundUtil.startBackgroundJob(this, null, "loading image", job,
				mHandler);
	}

	private void onSaveClicked() {
		// CR: !
		// this code needs to change to use the decode/crop/encode single
		// step api so that we don't require that the whole (possibly large)
		// bitmap doesn't have to be read into memory
		if (mSaving)
			return;

		if (mCrop == null) {
			return;
		}
		mSaving = true;
		Rect r = mCrop.getCropRect();

		int width = r.width(); // CR: final == happy panda!
		int height = r.height();

		// If we are circle cropping, we want alpha channel, which is the
		// third param here.
		Bitmap croppedImage = Bitmap.createBitmap(width, height,
				mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(croppedImage);
		Rect dstRect = new Rect(0, 0, width, height);
		canvas.drawBitmap(mBitmap, r, dstRect, null);

		byte[] bytes = ImageUtil.BitmapToBytes(croppedImage);
		byte[] resizeImageBytes = ImageUtil.resizeImage(bytes);
		Bitmap resizeCroppedImage = ImageUtil.BytesToBitmap(resizeImageBytes);

		if (mCircleCrop) {
			// OK, so what's all this about?
			// Bitmaps are inherently rectangular but we want to return
			// something that's basically a circle. So we fill in the
			// area around the circle with alpha. Note the all important
			// PortDuff.Mode.CLEARes.
			Canvas c = new Canvas(croppedImage);
			Path p = new Path();
			p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
			c.clipPath(p, Region.Op.DIFFERENCE);
			c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
		}

		// Return the cropped image directly or save it to the specified URI.
		Bundle myExtras = getIntent().getExtras();

		if (myExtras.getBoolean("return-data") || myExtras != null
				&& (myExtras.getParcelable("data") != null)) {
			Bundle extras = new Bundle();
			extras.putParcelable("data", resizeCroppedImage);
			setResult(RESULT_OK, (new Intent()).putExtras(extras));
			finish();
		} else {
			final Bitmap b = croppedImage;
			final Runnable save = new Runnable() {
				public void run() {
					saveOutput(b);
				}
			};
			BackgroundUtil.startBackgroundJob(this, null, "onSaveClicked",
					save, mHandler);
		}
	}

	private void saveOutput(Bitmap croppedImage) {
		// dir
		String dir_path = getSDPath() + File.separator + "CropImages";
		File dir = new File(dir_path);
		File file = new File(dir_path + File.separator
				+ System.currentTimeMillis() + ".jpg");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		OutputStream outStream;
		try {
			outStream = new FileOutputStream(file);
			croppedImage.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			Log.i("CropImage", "bitmap saved tosd,path:" + file.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mSaving = false;
	}

	public String getSDPath() {
		File sdDir = null;
		@SuppressWarnings("unused")
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		sdDir = Environment.getExternalStorageDirectory();
		return sdDir.toString();

	}

	Runnable mRunFaceDetection = new Runnable() {
		Matrix mImageMatrix;

		// Create a default HightlightView if we found no face in the picture.
		private void makeDefault() {
			CropImageHighlightView hv = new CropImageHighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			Rect imageRect = new Rect(0, 0, width, height);

			// CR: sentences!
			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;
			int cropHeight = cropWidth;
			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop, true);
			mImageView.clearAll();
			mImageView.add(hv);
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();
			mHandler.post(new Runnable() {
				public void run() {
					makeDefault();
					mImageView.invalidate();
					if (mImageView.getmHighlightViews().size() == 1) {
						mCrop = mImageView.getmHighlightViews().get(0);
						mCrop.setFocus(true);
					}
				}
			});
		}
	};

	// TODO
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// the next both item are all the same cancel function
		case R.id.crop_image_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_title_bar_edit:
			mBitmap = ImageUtil.rotateImage(mBitmap, IMAGEROATAEDEGREE);
			startFaceDetection();
			break;
		case R.id.crop_image_done:
			progressDialog = new ProgressDialog(CropImageActivity.this);
			progressDialog.setMessage("Cropping image");
			progressDialog.show();
			CropImageTask.start();
			break;
		}
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

}
