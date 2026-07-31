package com.watudrc.app.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.watudrc.app.R;

public class FirmaTextView extends AppCompatTextView {

	public FirmaTextView(Context context) {
		super(context);
		if (isInEditMode()) return;
		parseAttributes(null);
	}

	public FirmaTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public FirmaTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	private void parseAttributes(AttributeSet attrs) {
		int typeface;
		if (attrs == null) { //Not created from xml
			typeface = Firma.FIRMA_LIGHT;
		} else {
			TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.FirmaTextView);
			typeface = values.getInt(R.styleable.FirmaTextView_firmatypeface, Firma.FIRMA_LIGHT);
			values.recycle();
		}
		setTypeface(getFirma(typeface));
	}

	public void setFirmaTypeface(int typeface) {
		setTypeface(getFirma(typeface));
	}

	private Typeface getFirma(int typeface) {
		return getFirma(getContext(), typeface);
	}

	public static Typeface getFirma(Context context, int typeface) {
		switch (typeface) {
			case Firma.FIRMA_LIGHT:
				if (Firma.sFirmaLight == null) {
					Firma.sFirmaLight = Typeface.createFromAsset(context.getAssets(), "fonts/poppins-light.ttf");
				}
				return Firma.sFirmaLight;

			case Firma.FIRMA_BOLD:
				if (Firma.sFirmaBold == null) {
					Firma.sFirmaBold = Typeface.createFromAsset(context.getAssets(), "fonts/poppins-bold.ttf");
				}
				return Firma.sFirmaBold;

			case Firma.FIRMA_MEDIUM:
				if (Firma.sFirmaMedium == null) {
					Firma.sFirmaMedium = Typeface.createFromAsset(context.getAssets(), "fonts/poppins-medium.ttf");
				}
				return Firma.sFirmaMedium;


			default:
				Firma.sFirmaLight = Typeface.createFromAsset(context.getAssets(), "fonts/poppins-light.ttf");

				return Firma.sFirmaLight;
		}
	}

	public static class Firma {

		public static final int FIRMA_LIGHT = 0;
		public static final int FIRMA_BOLD = 1;
		public static final int FIRMA_MEDIUM = 2;




		private static Typeface sFirmaLight;
		private static Typeface sFirmaBold;
		private static Typeface sFirmaMedium;

	}
}
