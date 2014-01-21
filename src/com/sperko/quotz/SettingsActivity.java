package com.sperko.quotz;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.sperko.quotz.R;
import com.sperko.quotz.prefs.DeleteQuotePreference;

public class SettingsActivity extends PreferenceActivity {
	public static final String DELETE_QUOTE = "pref_delete_quote";
	public static final String ADD_QUOTE = "pref_add_quote";
	public static final String NIGHT_THEME = "pref_night_theme";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		DatabaseHandler db = new DatabaseHandler(this);
		PreferenceScreen preferencesScreen = getPreferenceScreen();
		DeleteQuotePreference deleteQuotePref = (DeleteQuotePreference) preferencesScreen.findPreference(DELETE_QUOTE);
		deleteQuotePref.setQuote(db.getQuotes());
	}
}
