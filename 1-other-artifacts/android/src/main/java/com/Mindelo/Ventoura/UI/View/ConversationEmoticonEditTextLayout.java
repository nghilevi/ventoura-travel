package com.Mindelo.Ventoura.UI.View;

import java.util.ArrayList;
import java.util.List;
import com.Mindelo.Ventoura.Entity.ConversationEmoticon;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Adapter.ConversationEmoticonViewAdapter;
import com.Mindelo.Ventoura.UI.Adapter.ConversationEmoticonViewPagerAdapter;
import com.Mindelo.Ventoura.Util.ConversationEmoticonUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ConversationEmoticonEditTextLayout extends RelativeLayout implements
		OnClickListener, OnItemClickListener,OnTouchListener {

	private static final String TAG = "com.Mindelo.Ventoura.UI.Layout.ConversationEmoticonEditTextLayout";

	private Context context;

	// monitor the face page
	private OnCorpusSelectedListener onCorpusSelectedListener;

	// the chat face region
	private ViewPager vpConversationEmoticon;

	// contain all the page of chat face
	private List<View> pageViews;

	// the selection of chat face region
	private LinearLayout chatFacePageSelection;

	// point assemble of the page
	private List<ImageView> pointViews;

	// assembel of the chat face
	public List<List<ConversationEmoticon>> chatFacesPageList;

	// all view of the face region
	private View view;

	// the edit region
	private EditText etMessage;

	// send messager button
	private Button sendButton;

	// chat face imageButton
	private ImageButton chatFaceButton;

	// current page of the chat face
	private int currentPage = 0;

	// the content of edittext
	private SpannableString spannableString;

	//the height of chat face in the edittext
	private int chatFaceHeight;

	//whether has chat face
	private boolean chatFaceStatue=false;
	
	//whether has key board
	private boolean mHasKeyboard = false;


	// adapter of each chat page
	private List<ConversationEmoticonViewAdapter> chatFaceAdapters;

	public ConversationEmoticonEditTextLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public ConversationEmoticonEditTextLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ConversationEmoticonEditTextLayout(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		chatFacesPageList = ConversationEmoticonUtil.getInstance().chatFacesPageList;
		onCreate();
	}

	private void onCreate() {
		Log.i(TAG, "onCreate");
		initView();
		initVeiwPager();
		initPoints();
		initData();
	}

	private void initData() {
		vpConversationEmoticon.setAdapter(new ConversationEmoticonViewPagerAdapter(pageViews));
		vpConversationEmoticon.setCurrentItem(1);
		currentPage = 0;
		vpConversationEmoticon.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0 - 1;
				drawPoint(arg0);
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vpConversationEmoticon.setCurrentItem(arg0 + 1);
						pointViews.get(1).setBackgroundResource(R.drawable.conversation_emoticon_page_selected);
					} else {
						vpConversationEmoticon.setCurrentItem(arg0 - 1);
						pointViews.get(arg0 - 1).setBackgroundResource(R.drawable.conversation_emoticon_page_selected);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initPoints() {
		pointViews = new ArrayList<ImageView>();
		ImageView point;
		for (int i = 0; i < pageViews.size(); i++) {
			point = new ImageView(context);
			point.setBackgroundResource(R.drawable.conversation_emoticon_page_not_selected);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			chatFacePageSelection.addView(point, layoutParams);
			if (0 == i || pageViews.size() - 1 == i) {
				point.setVisibility(View.GONE);
			}
			if (i == 1) {
				point.setBackgroundResource(R.drawable.conversation_emoticon_page_selected);
			}
			pointViews.add(point);
		}
	}

	private void initVeiwPager() {
		pageViews = new ArrayList<View>();

		// add a blank view to the left
		View nullLeftView = new View(context);

		nullLeftView.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullLeftView);

		// the chat page added as the middle
		chatFaceAdapters = new ArrayList<ConversationEmoticonViewAdapter>();
		for (int i = 0; i < chatFacesPageList.size(); i++) {
			GridView chatFacePage = new GridView(context);
			ConversationEmoticonViewAdapter adapter = new ConversationEmoticonViewAdapter(context,chatFacesPageList.get(i));
			chatFacePage.setAdapter(adapter);
			chatFaceAdapters.add(adapter);
			chatFacePage.setOnItemClickListener(this);
			chatFacePage.setNumColumns(7);
			chatFacePage.setBackgroundColor(Color.TRANSPARENT);
			chatFacePage.setHorizontalSpacing(1);
			chatFacePage.setVerticalSpacing(1);
			chatFacePage.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			chatFacePage.setCacheColorHint(0);
			chatFacePage.setPadding(5, 0, 5, 0);
			chatFacePage.setSelector(new ColorDrawable(Color.TRANSPARENT));
			chatFacePage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			chatFacePage.setGravity(Gravity.CENTER_VERTICAL);
			pageViews.add(chatFacePage);
		}

		// add a blank view to the left
		View nullRightView = new View(context);

		nullRightView.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullRightView);

	}

	private void initView() {
		etMessage = (EditText) this.findViewById(R.id.conversation_edittext_message_content_field);
		etMessage.addTextChangedListener(textWatcher);
		sendButton = (Button) this.findViewById(R.id.button_conversatioon_send_message);	
		chatFaceButton = (ImageButton) this.findViewById(R.id.chat_face_select_ib);
		vpConversationEmoticon = (ViewPager) this.findViewById(R.id.vp_chatface_choose);
		chatFacePageSelection = (LinearLayout) this.findViewById(R.id.layout_chatface_page_selection);
		view = this.findViewById(R.id.layout_chatface_choose);
		etMessage.setOnTouchListener(this);
		sendButton.setOnClickListener(this);
		chatFaceButton.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		ConversationEmoticon chatFace = (ConversationEmoticon) chatFaceAdapters.get(currentPage).getItem(position);
		if (chatFace.getFaceImageResId() == R.drawable.conversation_emoticon_delete_icon) {
			int selection = etMessage.getSelectionStart();
			String text = etMessage.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					etMessage.getText().delete(start, end);
					return;
				}
				etMessage.getText().delete(selection - 1, selection);
			}
		}
		if (!TextUtils.isEmpty(chatFace.getFaceImageDescription())) {
			if (onCorpusSelectedListener != null) {
				onCorpusSelectedListener.onCorpusSelected(chatFace);
			}
			chatFaceHeight = (int) (etMessage.getTextSize() * 5 / 4);
			int s = etMessage.getSelectionStart();
			Editable editAble = etMessage.getText();
			if (s == editAble.toString().length()) {
				spannableString = ConversationEmoticonUtil.getInstance().addFace(etMessage.getContext(), chatFace.getFaceImageResId(),chatFace.getFaceImageDescription(), chatFaceHeight);
				etMessage.append(spannableString);
			} else {
				int startSelection=etMessage.getSelectionStart();
				startSelection+=chatFace.getFaceImageDescription().toString().length();
				editAble.insert(s, chatFace.getFaceImageDescription());
				String str = etMessage.getText().toString();
				SpannableString sss = ConversationEmoticonUtil.getInstance().getExpressionString(context, str, chatFaceHeight);
				etMessage.setText(sss);
				etMessage.setSelection(startSelection);
			}
		}

	}

	public interface OnCorpusSelectedListener {

		void onCorpusSelected(ConversationEmoticon chatFace);

		void onCorpusDeleted();
	}

	public void drawPoint(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.conversation_emoticon_page_selected);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.conversation_emoticon_page_not_selected);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_conversatioon_send_message:
			Toast.makeText(context, etMessage.getText().toString(),Toast.LENGTH_SHORT).show();
			break;
		case R.id.chat_face_select_ib:
			dealConversationEmoticonStatus();
			break;
		}
	}

	/**
	 * 隐藏表情选择框
	 */
	public boolean hideFaceView() {
		// 隐藏表情选择框
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			//Log.i(TAG, "beforeTextChanged s="+s);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			// Log.i(TAG, "onTextChanged s="+s);
		}

		@Override
		public void afterTextChanged(Editable s) {
			//Log.i(TAG, "afterTextChanged s="+s);
		}
	};

	private void dealConversationEmoticonStatus() {
		// already open the default input method ,first close it
		if (mHasKeyboard == true) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
			mHasKeyboard = false;
			if (chatFaceStatue == false) {
				view.setVisibility(View.VISIBLE);
				chatFaceStatue = true;
			}
		} else {
			if (chatFaceStatue == false) {
				view.setVisibility(View.VISIBLE);
				chatFaceStatue = true;
			} else {
				view.setVisibility(View.GONE);
				chatFaceStatue = false;
			}
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (R.id.conversation_edittext_message_content_field == v.getId()) {
			mHasKeyboard=true;
		}
		return false;
	}
}
