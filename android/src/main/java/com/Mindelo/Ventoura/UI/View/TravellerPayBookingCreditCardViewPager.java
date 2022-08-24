package com.Mindelo.Ventoura.UI.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TravellerPayBookingCreditCardViewPager extends ViewPager {

	private boolean scrollable = false;

	public TravellerPayBookingCreditCardViewPager(Context context) {
		super(context);
	}

	public TravellerPayBookingCreditCardViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (scrollable)
			return super.onInterceptTouchEvent(event);
		else
			return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (scrollable)
			return super.onTouchEvent(arg0);
		else
			return false;
	}

}
