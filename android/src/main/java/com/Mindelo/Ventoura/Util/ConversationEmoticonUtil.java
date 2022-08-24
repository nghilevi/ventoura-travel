package com.Mindelo.Ventoura.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.Mindelo.Ventoura.Entity.ConversationEmoticon;
import com.Mindelo.Ventoura.UI.Activity.R;


public class ConversationEmoticonUtil {

	// the nums of each page
	private int pageSize = 20;

	// the singleton pattern of ConversationEmoticonUitl
	private static ConversationEmoticonUtil conversationEmoticonUtil;

	// to save the info of chat face into memory
	private Map<String, String> chatFaceMap = new HashMap<String, String>();

	// to save the chat face into memory
	private List<ConversationEmoticon> chatFaces = new ArrayList<ConversationEmoticon>();
	
	// to save the pages of chat face
	public List<List<ConversationEmoticon>> chatFacesPageList=new ArrayList<List<ConversationEmoticon>>();

	private ConversationEmoticonUtil() {

	}

	public static ConversationEmoticonUtil getInstance() {
		if (conversationEmoticonUtil == null) {
			conversationEmoticonUtil = new ConversationEmoticonUtil();
		}
		return conversationEmoticonUtil;
	}

	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(Context context, String str,int height) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String matchPattern = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(matchPattern,Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0,height);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId, String spannableString,int height) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapUtil.ReadBitmapById(context, imgId, height, height);
		ImageSpan imageSpan = new ImageSpan(context, bitmap, ImageSpan.ALIGN_BOTTOM);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start,int height) throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = chatFaceMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable",context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapUtil.ReadBitmapById(context, resId,height, height);
				ImageSpan imageSpan = new ImageSpan(context, bitmap,ImageSpan.ALIGN_BOTTOM);
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end,height);
				}
				break;
			}
		}
	}

	public void getFileText(Context context) {
		ParseData(getEmojiFile(context), context);
	}
	
	/**
	 * 读取表情配置文件
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getEmojiFile(Context context) {
		List<String> list=null;
		try {
			list = new ArrayList<String>();
			InputStream in = context.getResources().getAssets().open("chatface");
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void ParseData(List<String> data, Context context) {
		if (data == null) {
			return;
		}
		ConversationEmoticon chatFace;
		try {
			for (String str : data) {
				String[] text = str.split(",");
				String fileName = text[0].substring(0, text[0].lastIndexOf("."));
				chatFaceMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName,"drawable", context.getPackageName());

				if (resID != 0) {
					chatFace = new ConversationEmoticon();
					chatFace.setFaceImageResId(resID);
					chatFace.setFaceImageDescription(text[1]);
					chatFace.setFaceIamgeFileName(fileName);
					chatFaces.add(chatFace);  
				}
			}
			int pageCount = (int) Math.ceil(chatFaces.size() / 20 + 0.1);

			for (int i = 0; i < pageCount; i++) {
				chatFacesPageList.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<ConversationEmoticon> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;

		if (endIndex > chatFaces.size()) {
			endIndex = chatFaces.size();
		}
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		List<ConversationEmoticon> list = new ArrayList<ConversationEmoticon>();
		list.addAll(chatFaces.subList(startIndex, endIndex));
		if (list.size() < pageSize) {
			for (int i = list.size(); i < pageSize; i++) {
				ConversationEmoticon cf = new ConversationEmoticon();
				list.add(cf);
			}
		}
		if (list.size() == pageSize) {
			ConversationEmoticon cf = new ConversationEmoticon();
			cf.setFaceImageResId(R.drawable.conversation_emoticon_delete_icon);
			list.add(cf);
		}
		return list;
	}
}
