package com.sperko.quotz.prefs;

import com.sperko.quotz.DatabaseHandler;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class AddQuotePreference extends EditTextPreference {

	public AddQuotePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AddQuotePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AddQuotePreference(Context context) {
		super(context);
	}

	@Override
	protected boolean persistString(String value) {
    	DatabaseHandler db = new DatabaseHandler(AddQuotePreference.this.getContext());
    	db.addQuote(value, "");
		return false;
	}
}
