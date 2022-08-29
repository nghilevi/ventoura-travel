package com.Mindelo.Ventoura.UI.View;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Random;

import lombok.Data;

import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.Model.CardModel;
import com.Mindelo.Ventoura.UI.Activity.R;

@Data
public class CardContainerView extends AdapterView<ListAdapter> {

	private ICountryService countryService;
	private ICityService cityService;

	public static final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	private static final double DISORDERED_MAX_ROTATION_RADIANS = Math.PI / 64;
	private int mNumberOfCards = -1;
	private final DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();
			clearStack();
			ensureFull();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			clearStack();
		}
	};
	private final Random mRandom = new Random();
	private final Rect boundsRect = new Rect();
	private final Rect childRect = new Rect();
	private final Rect clickRect = new Rect();
	private final Matrix mMatrix = new Matrix();

	// TODO: determine max dynamically based on device speed
	private int mMaxVisible = 10;
	private GestureDetector mGestureDetector;
	private int mFlingSlop;
	private ListAdapter mListAdapter;
	private float mLastTouchX;
	private float mLastTouchY;
	private View mTopCard, tmpTopCard;
	private CardModel mCardModel;
	private int mTouchSlop;
	private int mGravity;
	private int mNextAdapterPosition;
	private boolean mDragging;
	private boolean mFling;

	// animation
	private Animation rotateAnimYes;
	private Animation rotateAnimNo;

	private int clickViewId;
	private boolean mgetClickedRect = false;

	/*
	 * views
	 */
	private TextView ventouraNameTextView;
	private TextView ventouraAgeTextView;
	private TextView ventouraLocationTextView;

	public CardContainerView(Context context) {
		super(context);
		setGravity(Gravity.CENTER);
		init();
		
	}

	public CardContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CardContainerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(getContext());
		mFling = false;
		mFlingSlop = viewConfiguration.getScaledMinimumFlingVelocity();
		mTouchSlop = viewConfiguration.getScaledTouchSlop();
		mGestureDetector = new GestureDetector(getContext(),
				new GestureListener());
		initYesAnimation();
		initNoAnimation();
	}

	private void initYesAnimation() {

		rotateAnimYes = AnimationUtils.loadAnimation(this.getContext(),
				R.anim.ventouring_rotate_yes);
		if (rotateAnimYes == null) {
			rotateAnimYes = new RotateAnimation(0f, 120f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
		}
		// LinearInterpolator lin = new LinearInterpolator();
		AccelerateInterpolator lin = new AccelerateInterpolator();
		rotateAnimYes.setInterpolator(lin);
		rotateAnimYes.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				/*
				 * reset the mDragging
				 */
				mDragging = false;
				mFling = true;
				mTopCard = getChildAt(getChildCount() - 2);
				if (mTopCard != null) {
					mCardModel = (CardModel) mListAdapter.getItem(mListAdapter
							.getCount() - getChildCount() + 1);
					JSONVentoura tempVentoura = mCardModel.getVentoura();
					updateVentouraTextInfo(tempVentoura);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				onAnimationEnd(animation);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				removeViewInLayout(tmpTopCard);
				ensureFull();
				mFling = false;
			}
		});
	}

	private void initNoAnimation() {
		rotateAnimNo = AnimationUtils.loadAnimation(this.getContext(),
				R.anim.ventouring_rotate_no);
		if (rotateAnimNo == null) {
			rotateAnimNo = new RotateAnimation(0f, 120f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
		}
		AccelerateInterpolator lin = new AccelerateInterpolator();
		// LinearInterpolator lin = new LinearInterpolator();
		rotateAnimNo.setInterpolator(lin);
		rotateAnimNo.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mDragging = false;
				mFling = true;
				mTopCard = getChildAt(getChildCount() - 2);
				if (mTopCard != null) {
					mCardModel = (CardModel) mListAdapter.getItem(mListAdapter
							.getCount() - getChildCount() + 1);
					JSONVentoura tempVentoura = mCardModel.getVentoura();
					updateVentouraTextInfo(tempVentoura);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				onAnimationEnd(animation);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				removeViewInLayout(tmpTopCard);
				ensureFull();
				mFling = false;
			}
		});
	}

	@Override
	public ListAdapter getAdapter() {
		return mListAdapter;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (mListAdapter != null)
			mListAdapter.unregisterDataSetObserver(mDataSetObserver);

		clearStack();
		mTopCard = null;
		mCardModel = null;
		mListAdapter = adapter;
		mNextAdapterPosition = 0;
		adapter.registerDataSetObserver(mDataSetObserver);

		ensureFull();

		if (getChildCount() != 0) {
			mTopCard = getChildAt(getChildCount() - 1);
			if (mTopCard != null) {
				mCardModel = (CardModel) mListAdapter.getItem(mListAdapter
						.getCount() - getChildCount());
				JSONVentoura tempVentoura = mCardModel.getVentoura();
				updateVentouraTextInfo(tempVentoura);
			}
			// mTopCard.setLayerType(LAYER_TYPE_HARDWARE, null);
		}
		mNumberOfCards = getAdapter().getCount();
		requestLayout();
	}

	private void ensureFull() {
		while (mNextAdapterPosition < mListAdapter.getCount()
				&& getChildCount() < mMaxVisible) {
			View view = mListAdapter.getView(mNextAdapterPosition, null, this);
			// view.setLayerType(LAYER_TYPE_SOFTWARE, null);
			// if (mOrientation == Orientation.Disordered) {
			// view.setRotation(getDisorderedRotation());
			// }
			addViewInLayout(view, 0, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					mListAdapter.getItemViewType(mNextAdapterPosition)), false);

			requestLayout();

			mNextAdapterPosition += 1;
		}
	}

	private void clearStack() {
		removeAllViewsInLayout();
		mNextAdapterPosition = 0;
		mTopCard = null;
		mCardModel = null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int requestedWidth = getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight();
		int requestedHeight = getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom();
		int childWidth, childHeight;

		childWidth = requestedWidth;
		childHeight = requestedHeight;

		int childWidthMeasureSpec, childHeightMeasureSpec;
		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth,
				MeasureSpec.AT_MOST);
		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight,
				MeasureSpec.AT_MOST);

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			assert child != null;
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		for (int i = 0; i < getChildCount(); i++) {
			boundsRect.set(0, 0, getWidth(), getHeight());

			View view = getChildAt(i);
			int w, h;
			w = view.getMeasuredWidth();
			h = view.getMeasuredHeight();

			Gravity.apply(mGravity, w, h, boundsRect, childRect);
			view.layout(childRect.left, childRect.top, childRect.right,
					childRect.bottom);

			/*
			 * Get the clickRect in the first layout
			 */
			if (mgetClickedRect == false) {
				view.findViewById(clickViewId).getHitRect(clickRect);
				setClickRect(clickRect);
				Log.i("@@@@Click Rect***", clickRect.left + "," + clickRect.top
						+ "," + clickRect.right + "," + clickRect.bottom);
				mgetClickedRect = true;
			}
		}
	}

	/*
	 * Get the Center of Click View's range
	 */
	void setClickRect(Rect rect) {
		int l, t, r, b;
		l = rect.left;
		r = rect.right;
		t = rect.top;
		b = rect.bottom;
		int cw = r - l;
		int ch = b - t;
		int border = 100;
		/*
		 * get a square, but fill with border below
		 */
		if (ch < cw) {
			int cl = (cw - ch) / 2;
			int cr = (cw + ch) / 2;
			clickRect.set(cl, t, cr, b + border);
			Log.i("Click Rect", "Adapted");
		} else {
			int ct = (ch - cw) / 2;
			int cb = (ch + ch) / 2;
			clickRect.set(l, ct, r, cb + border);
		}
	}

	/*
	 * update the user info once a swipe is done
	 */
	private void updateVentouraTextInfo(JSONVentoura ventoura) {

		
		if (ventoura == null || ventouraNameTextView == null || ventouraAgeTextView == null
				|| ventouraLocationTextView == null)
			return;
		ventouraNameTextView.setText(ventoura.getFirstname());
		ventouraAgeTextView.setText(ventoura.getAge() + "");

		String location = "";
		Country country = countryService.getCountryById(ventoura.getCountry());
		City city = cityService.getCityById(ventoura.getCity());
		if (ventoura.getUserRole() == UserRole.GUIDE) {
			if(country != null){
				location = country.getCountryName();
				
				if(city != null){
					location = city.getCityName() + ", " + location;
				}
			}
					
		} else {
			if(country != null){
				location = country.getCountryName();
			}else{
				location = "Alien"; //TODO
			}
		}
		ventouraLocationTextView.setText(location); 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mFling) {
			return false;
		}
		if (mTopCard == null) {
			return false;
		}
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		// Log.d("Touch Event",
		// MotionEvent.actionToString(event.getActionMasked()) + " ");

		View noTextView = mTopCard.findViewById(R.id.ventouring_no_tv);
		View yesTextView = mTopCard.findViewById(R.id.ventouring_yes_tv);
		final int pointerIndex;
		final float x, y;
		final float dx, dy;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			// mTopCard.getHitRect(childRect);

			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);

			if (!clickRect.contains((int) x, (int) y)) {
				return false;
			}
			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = event.getPointerId(pointerIndex);

			float[] points = new float[] { x - mTopCard.getLeft(),
					y - mTopCard.getTop() };
			mTopCard.getMatrix().invert(mMatrix);
			mMatrix.mapPoints(points);
			// mTopCard.setPivotX(points[0]);
			// mTopCard.setPivotY(points[1]);

			mTopCard.setPivotX((mTopCard.getRight() - mTopCard.getLeft()) / 2);
			mTopCard.setPivotY(mTopCard.getBottom());

			break;
		case MotionEvent.ACTION_MOVE:

			pointerIndex = event.findPointerIndex(mActivePointerId);
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);

			dx = x - mLastTouchX;
			dy = y - mLastTouchY;

			if (Math.abs(dx) > mTouchSlop || Math.abs(dy) > mTouchSlop) {
				mDragging = true;
			}

			if (!mDragging) {
				return true;
			}

			// Log.i("Move x y rotation:",dx + "," + getWidth() +"," +
			// mTopCard.getWidth() +"," + 40 * (dx)
			// / (getWidth() / 2.f));

			mTopCard.setRotation(mTopCard.getRotation() + 40 * (dx)
					/ (getWidth() / 2.f));

			if (mTopCard.getRotation() < -15) {
				noTextView.setVisibility(View.VISIBLE);
			} else {
				noTextView.setVisibility(View.INVISIBLE);
			}
			if (mTopCard.getRotation() > 15) {
				yesTextView.setVisibility(View.VISIBLE);
			} else {
				yesTextView.setVisibility(View.INVISIBLE);
			}

			mLastTouchX = x;
			mLastTouchY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			noTextView.setVisibility(View.INVISIBLE);
			yesTextView.setVisibility(View.INVISIBLE);
			Log.i("UP", "Dragging=" + mDragging);
			if (!mDragging) {
				/*
				 * if not dragging, that was a click event
				 */
				if (mCardModel != null
						&& mCardModel.getOnClickListener() != null) {
					mCardModel.getOnClickListener().OnClickListener();
				}
				return true;
			}
			mDragging = false;
			mActivePointerId = INVALID_POINTER_ID;
			ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
					mTopCard, PropertyValuesHolder.ofFloat("translationX", 0),
					PropertyValuesHolder.ofFloat("translationY", 0),
					PropertyValuesHolder.ofFloat("rotation", 0)).setDuration(
					250);
			animator.setInterpolator(new AccelerateInterpolator());
			animator.start();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			pointerIndex = event.getActionIndex();
			final int pointerId = event.getPointerId(pointerIndex);

			if (pointerId == mActivePointerId) {
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = event.getX(newPointerIndex);
				mLastTouchY = event.getY(newPointerIndex);

				mActivePointerId = event.getPointerId(newPointerIndex);
			}
			break;
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mFling) {
			return false;
		}
		if (mTopCard == null) {
			return false;
		}
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		// Log.d("InterceptTouch Event",
		// MotionEvent.actionToString(event.getActionMasked()) + " ");
		final int pointerIndex;
		final float x, y;
		final float dx, dy;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			// mTopCard.getHitRect(childRect);

			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);

			if (!clickRect.contains((int) x, (int) y)) {
				return false;
			}

			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = event.getPointerId(pointerIndex);
			break;
		case MotionEvent.ACTION_MOVE:
			pointerIndex = event.findPointerIndex(mActivePointerId);
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			if (Math.abs(x - mLastTouchX) > mTouchSlop
					|| Math.abs(y - mLastTouchY) > mTouchSlop) {
				float[] points = new float[] { x - mTopCard.getLeft(),
						y - mTopCard.getTop() };
				mTopCard.getMatrix().invert(mMatrix);
				mMatrix.mapPoints(points);

				return true;
			}
		}

		return false;
	}

	@Override
	public View getSelectedView() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSelection(int position) {
		throw new UnsupportedOperationException();
	}

	public int getGravity() {
		return mGravity;
	}

	public void setGravity(int gravity) {
		mGravity = gravity;
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		int viewType;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(int w, int h, int viewType) {
			super(w, h);
			this.viewType = viewType;
		}
	}

	public void dislikeFromOutPut() {
		if (mFling) {
			return;
		}
		if (mTopCard == null)
			return;

		View noTextView = mTopCard.findViewById(R.id.ventouring_no_tv);
		noTextView.setVisibility(View.VISIBLE);
		if (mCardModel != null
				&& mCardModel.getOnCardDimissedListener() != null)
			mCardModel.getOnCardDimissedListener().onDislike();

		tmpTopCard = mTopCard;

		tmpTopCard.startAnimation(rotateAnimNo);
	}

	public void likeFromOutPut() {
		if (mFling) {
			return;
		}
		if (mTopCard == null)
			return;

		View yesTextView = mTopCard.findViewById(R.id.ventouring_yes_tv);
		yesTextView.setVisibility(View.VISIBLE);
		if (mCardModel != null
				&& mCardModel.getOnCardDimissedListener() != null)
			mCardModel.getOnCardDimissedListener().onLike();

		tmpTopCard = mTopCard;
		tmpTopCard.startAnimation(rotateAnimYes);
	}

	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (mFling) {
				return false;
			}
			tmpTopCard = mTopCard;
			float dx = e2.getX() - e1.getX();
			if (Math.abs(dx) > mTouchSlop
					&& 2 * Math.abs(velocityX) > Math.abs(velocityY)
					&& Math.abs(velocityX) > mFlingSlop * 4) {

				View noTextView = mTopCard.findViewById(R.id.ventouring_no_tv);
				View yesTextView = mTopCard
						.findViewById(R.id.ventouring_yes_tv);

				if (mCardModel != null
						&& mCardModel.getOnCardDimissedListener() != null) {
					if (velocityX < 0) {
						noTextView.setVisibility(View.VISIBLE);
						mCardModel.getOnCardDimissedListener().onDislike();
						tmpTopCard.startAnimation(rotateAnimNo);
					} else {
						yesTextView.setVisibility(View.VISIBLE);
						mCardModel.getOnCardDimissedListener().onLike();
						tmpTopCard.startAnimation(rotateAnimYes);
					}
				}

				return true;
			} else
				return false;
		}
	}
}