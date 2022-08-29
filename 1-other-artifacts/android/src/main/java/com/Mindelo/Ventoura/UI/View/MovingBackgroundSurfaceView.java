package com.Mindelo.Ventoura.UI.View;

import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.Util.BitmapUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MovingBackgroundSurfaceView extends SurfaceView implements
		Callback, Runnable {

	private final String TAG = "MovingBackgroundSurfaceView";

	private Context mContext;

	private SurfaceHolder surfaceHolder;

	private boolean flag = false;

	private Bitmap bitmap_bg;

	private float mSurfaceWidth, mSurfaceHeight;

	private int mBitposX; 

	private Canvas mCanvas;

	private Thread thread;

	private enum State {
		LEFT, RIGHT;
	}

	private State state = State.LEFT;

	private final int BITMAP_STEP = 1;

	public MovingBackgroundSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		flag = true;
		this.mContext = context;
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	protected void onDraw() {
		drawBG();
		updateBG();
	}

	private void updateBG() {
		switch (state) {
		case LEFT:
			mBitposX -= BITMAP_STEP;
			break;
		case RIGHT:
			mBitposX += BITMAP_STEP;
			break;
		default:
			throw new IllegalArgumentException("wrong moving state");
		}
		if (mBitposX <= -mSurfaceWidth / 2) {
			state = State.RIGHT;
		}
		if (mBitposX >= 0) {
			state = State.LEFT;
		}
	}

	private void drawBG() {
		if (mCanvas != null) {
			mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			mCanvas.drawBitmap(bitmap_bg, mBitposX, 0, null);
		}else{
			Log.i(TAG, " mCanvas is null ");
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, " surfaceCreated ");
		this.mSurfaceWidth = getWidth();
		this.mSurfaceHeight = this.getHeight();
		int mWidth = (int) (mSurfaceWidth * 3 / 2);
		int mHeight = (int) (mSurfaceHeight * 2 / 2);
		this.bitmap_bg = BitmapUtil.ReadBitmapById(mContext,R.drawable.bg_login_paris_moving_picture, (int) mWidth,(int) mHeight);
		thread = new Thread(this);
		//this will make the moving background exist when return to the login activity again
		flag=true;
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG, " surfaceChanged ");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, " surfaceDestroyed ");
		flag = false;
		bitmap_bg=null;
		mCanvas=null;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		while (flag) {
			synchronized (surfaceHolder) {
				mCanvas = surfaceHolder.lockCanvas();
				if(mCanvas!=null){
					onDraw();
					surfaceHolder.unlockCanvasAndPost(mCanvas);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
