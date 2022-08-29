package com.Mindelo.Ventoura.UI.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.RelativeLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

import java.util.List;
import java.util.ArrayList;

import com.Mindelo.Ventoura.UI.Activity.R;

/**
 * QuickAction dialog, shows action list as icon and text like the one in Gallery3D app. Currently supports vertical 
 * and horizontal layout.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * Contributors:
 * - Kevin Peck <kevinwpeck@gmail.com>
 */
public class BottomPopupMenuAction extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private ScrollView mScroller;
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;
	protected Activity mActivity;
	
	private List<PopupActionItem> actionItems = new ArrayList<PopupActionItem>();
	
	private boolean mDidAction;
	
	private int mChildPos;
    private int mInsertPos;
    
    
	
    /**
     * Constructor for default vertical layout
     * 
     * @param context  Context
     */
    public BottomPopupMenuAction(Activity activity) {
    	super(activity.getBaseContext());

    	mActivity = activity;
    	
    	Context context = activity.getBaseContext();
        
        mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setRootViewId(R.layout.popup_bottom_menu);
        mChildPos 	= 0;
    }

    /**
     * Get action item at an index
     * 
     * @param index  Index of item (position from callback)
     * 
     * @return  Action Item at the position
     */
    public PopupActionItem getActionItem(int index) {
        return actionItems.get(index);
    }
    
	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView	= (ViewGroup) mInflater.inflate(id, null);
		mTrack 		= (ViewGroup) mRootView.findViewById(R.id.tracks);

		Button cancelBtn = (Button) mRootView.findViewById(R.id.popup_bottom_menu_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		mScroller	= (ScrollView) mRootView.findViewById(R.id.scroller);
		
		//This was previously defined on show() method, moved here to prevent force close that occured
		//when tapping fastly on a view to show quickaction dialog.
		//Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(mRootView);
	}
	
	
	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener Listener
	 */
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mItemClickListener = listener;
	}
	
	/**
	 * Add action item
	 * 
	 * @param action  {@link PopupActionItem}
	 */
	public void addActionItem(PopupActionItem action) {
		actionItems.add(action);
		
		String title 	= action.getTitle();
		
		View container;
		
		container = mInflater.inflate(R.layout.adapter_popup_bottom_menu_action_item, null);
		
		TextView text 	= (TextView) container.findViewById(R.id.tv_title);
		
		
		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}
		
		final int pos 		=  mChildPos;
		final int actionId 	= action.getActionId();
		
		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(BottomPopupMenuAction.this, pos, actionId);
                }
				
                if (!getActionItem(pos).isSticky()) {  
                	mDidAction = true;
                	
                    dismiss();
                }
			}
		});
		
		container.setFocusable(true);
		container.setClickable(true);
		
		mTrack.addView(container, mInsertPos);
		
		mChildPos++;
		mInsertPos++;
	}
	
	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
	 * 
	 */
	public void show (View anchor) {
		preShow();
		
		mDidAction 			= false;
		
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();
		
		mWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		
		View lastView = mTrack.getChildAt(mChildPos - 1);
		lastView.findViewById(R.id.popup_bottom_menu_divider).setVisibility(View.GONE);
		
		mWindow.setAnimationStyle(R.style.anim_menu_bottombar);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, screenHeight);
		/*
		 * set the screen as gray
		 */
		WindowManager.LayoutParams lp=mActivity.getWindow().getAttributes();
        lp.alpha=0.5f;
        mActivity.getWindow().setAttributes(lp);
	}
	
	
	/**
	 * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
	 * by clicking outside the dialog or clicking on sticky item.
	 */
	public void setOnDismissListener(BottomPopupMenuAction.OnDismissListener listener) {
		setOnDismissListener(this);
		
		mDismissListener = listener;
	}
	
	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
		/*
		 * reset the window as transparent
		 */
		WindowManager.LayoutParams lp=mActivity.getWindow().getAttributes();
        lp.alpha=1.0f;
        mActivity.getWindow().setAttributes(lp);
	}
	
	/**
	 * Listener for item click
	 *
	 */
	public interface OnActionItemClickListener {
		public abstract void onItemClick(BottomPopupMenuAction source, int pos, int actionId);
	}
	
	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
}