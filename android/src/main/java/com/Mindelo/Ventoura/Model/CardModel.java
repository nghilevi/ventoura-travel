/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 *
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 *
 * AndTinder is compatible with API Level 13 and upwards
 *
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package com.Mindelo.Ventoura.Model;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;

import lombok.Data;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

@Data
public class CardModel {

	private JSONVentoura ventoura;
	private Drawable cardImageDrawable;
	private Drawable cardLikeImageDrawable;
	private Drawable cardDislikeImageDrawable;

    private OnCardDimissedListener mOnCardDimissedListener = null;

    private OnClickListener mOnClickListener = null;
    {
    }

    public interface OnCardDimissedListener {
        void onLike();
        void onDislike();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

	public CardModel(JSONVentoura ventoura) {
		this(ventoura, (Drawable)null);
	}
	
	public CardModel(JSONVentoura ventoura, Drawable cardImage) {
		this.ventoura = ventoura;
		this.cardImageDrawable = cardImage;
	}
	
	public CardModel(JSONVentoura ventoura,Bitmap cardImage) {
		this.ventoura = ventoura;
		this.cardImageDrawable = new BitmapDrawable(null, cardImage);
	}



    public void setOnCardDimissedListener( OnCardDimissedListener listener ) {
        this.mOnCardDimissedListener = listener;
    }

    public OnCardDimissedListener getOnCardDimissedListener() {
       return this.mOnCardDimissedListener;
    }


    public void setOnClickListener( OnClickListener listener ) {
        this.mOnClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }
}