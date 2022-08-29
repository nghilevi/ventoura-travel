package com.Mindelo.Ventoura.Entity;

import com.Mindelo.Ventoura.UI.View.RoundedImageView;

import android.graphics.Bitmap;

public class SmallHeadImage {

	private Bitmap headImage;
	private boolean isAdd;
	private RoundedImageView view;

	public SmallHeadImage() {
		super();
	}

	public SmallHeadImage(Bitmap headImage, boolean isAdd, RoundedImageView view) {
		super();
		this.headImage = headImage;
		this.isAdd = isAdd;
		this.view = view;
	}

	public Bitmap getHeadImage() {
		return headImage;
	}

	public void setHeadImage(Bitmap headImage) {
		this.headImage = headImage;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public RoundedImageView getView() {
		return view;
	}

	public void setView(RoundedImageView view) {
		this.view = view;
	}

}
