package com.Mindelo.Ventoura.UI.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Entity.GuideProfileTagsEntity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.ViewHolder.OneTagViewHolder;
import com.Mindelo.Ventoura.UI.ViewHolder.TwoTagViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GuideProfileAttractionsTagAdapter extends BaseAdapter {

	private final static String TAG = "GuideProfileAttractionsTagAdapter";
	
	private final int maxLengthOfTag=10;

	private Context context;
	private LayoutInflater inflater;
	private List<String> tags;
	private List<GuideProfileTagsEntity> guideProfileTagsEntities=new ArrayList<>();
	

	private final int TWOTAGLEFT=0x1003;
	private final int TWOTAGRIGHT=0x1004;

	@Override
	public void notifyDataSetChanged() {
		updataVeiw();
		super.notifyDataSetChanged();
	}

	public GuideProfileAttractionsTagAdapter(Context context, List<String> tags) {
		super();
		this.context = context;
		this.tags = tags;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		updataVeiw();
	}

	@Override
	public int getCount() {
		return guideProfileTagsEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return guideProfileTagsEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view=null;
		GuideProfileTagsEntity e = guideProfileTagsEntities.get(position);
		List<String> tagsOfEachRow = e.getTagsOfEachRow();
		if (tagsOfEachRow.size() == 1) {
			String tag = tagsOfEachRow.get(0);
			if (tag.length() < maxLengthOfTag) {
				view = inflater.inflate(R.layout.adapter_guide_profile_attractions_tags_selection_one_short_item,null);
				OneTagViewHolder vh = new OneTagViewHolder(view);
				vh.tvTag.setText(tag);
				vh.ivCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deteleOneTag(position);
					}
				});
			}else{
				view = inflater.inflate(R.layout.adapter_guide_profile_attractions_tags_selection_one_long_item,null);
				OneTagViewHolder vh = new OneTagViewHolder(view);
				vh.tvTag.setText(tag);
				vh.ivCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deteleOneTag(position);
					}
				});
			}

		} else{
			view=inflater.inflate(R.layout.adapter_guide_profile_attractions_tags_selection_two_item, null);
			TwoTagViewHolder vh=new TwoTagViewHolder(view);
			vh.tvTagLeft.setText(tagsOfEachRow.get(0));
			vh.tvTagRight.setText(tagsOfEachRow.get(1));
			vh.ivCancelLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deteleTwoTag(position,TWOTAGLEFT);
					Log.i(TAG, "2");
					
				}
			});
			vh.ivCancelRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deteleTwoTag(position,TWOTAGRIGHT);
					Log.i(TAG, "3");
					
				}
			});
		}
		return view;
	}

	private void setGuideProfileTagsEntity() {
		guideProfileTagsEntities.clear();
		int tagsLength = tags.size();
		if (tagsLength != 0) {
			int index = 0;
			Log.i(TAG, "tagsLength = " + tagsLength);
			// the index of the string when the length of the stirng is litter
			// than maxLenghtOfTag
			for (int i = 0; i < tagsLength; i++) {
				if (tags.get(i).length() >= maxLengthOfTag) {
					continue;
				} else {
					index = i;
					break;
				}
			}
			if (index == 0) {// all the string neighor is longer than maxLenghtOfTag or the othersize
				if (tags.get(0).length() >= maxLengthOfTag) {
					for (int i = 0; i < tagsLength; i++) {
						GuideProfileTagsEntity e = new GuideProfileTagsEntity();
						e.addTags(tags.get(i));
						guideProfileTagsEntities.add(e);
					}
				} else {
					for (int i = index; i < tagsLength;) {
						int leftNum = tagsLength - i;
						if (leftNum == 1) {
							GuideProfileTagsEntity e = new GuideProfileTagsEntity();
							e.addTags(tags.get(i));
							guideProfileTagsEntities.add(e);
							break;
						} else {
							GuideProfileTagsEntity e = new GuideProfileTagsEntity();
							e.addTags(tags.get(i));
							e.addTags(tags.get(i + 1));
							i += 2;
							guideProfileTagsEntities.add(e);
						}
					}
				}

			} else {// has the string where the length of string is smaller than
					// maxLenghtOfTag
				for (int i = 0; i < index; i++) {
					GuideProfileTagsEntity e = new GuideProfileTagsEntity();
					e.addTags(tags.get(i));
					guideProfileTagsEntities.add(e);
				}
				for (int i = index; i < tagsLength;) {
					int leftNum = tagsLength - i;
					if (leftNum == 1) {
						GuideProfileTagsEntity e = new GuideProfileTagsEntity();
						e.addTags(tags.get(i));
						guideProfileTagsEntities.add(e);
						break;
					} else {
						GuideProfileTagsEntity e = new GuideProfileTagsEntity();
						e.addTags(tags.get(i));
						e.addTags(tags.get(i + 1));
						i += 2;
						guideProfileTagsEntities.add(e);
					}
				}
			}
		}
	}
	
	private void updataVeiw(){
		java.util.Collections.sort(tags, new StringLengthComparator());
		setGuideProfileTagsEntity();
	}
	private class StringLengthComparator implements java.util.Comparator<String> {

		public StringLengthComparator() {

		}

		@Override
		public int compare(String lhs, String rhs) {
			int dist1 = Math.abs(lhs.length());
			int dist2 = Math.abs(rhs.length());
			return dist2 - dist1;
		}

	}

	
	private void deteleTwoTag(int rowNum,int leftOfRight) {
		int index=0;
		for(int i=0;i<rowNum;i++){
			GuideProfileTagsEntity e = guideProfileTagsEntities.get(i);
			List<String> s = e.getTagsOfEachRow();
			index+=s.size();
		}
		if(leftOfRight==TWOTAGRIGHT){
			index++;
		}
		deteleOneTag(index);
	
	}
	
	private void deteleOneTag(int position){
		tags.remove(position);
		notifyDataSetChanged();
	}
	
}
