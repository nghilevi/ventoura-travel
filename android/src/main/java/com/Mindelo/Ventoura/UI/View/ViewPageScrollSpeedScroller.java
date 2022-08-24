package com.Mindelo.Ventoura.UI.View;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPageScrollSpeedScroller extends Scroller {

	private int mDuration = 0;

	public ViewPageScrollSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	public int getmDuration() {
		return mDuration;
	}

	public void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}

}
