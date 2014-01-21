package com.sperko.quotz.prefs;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.sperko.quotz.R;
import com.sperko.quotz.DatabaseHandler;
import com.sperko.quotz.Quote;

public class DeleteQuotePreference extends ListPreference {
    
	public DeleteQuotePreference(Context context) {
		super(context);
	}

	public DeleteQuotePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setQuote(List<Quote> quotes) {
		int size = quotes != null ? quotes.size() : 0;
		
		CharSequence[] lEntries = new CharSequence[size];
	    CharSequence[] lEntryValues = new CharSequence[size];
	    
	    Quote quote = null;
	    for(int i = 0; i < size; i++) {
	    	quote = quotes.get(i);
	    	lEntries[i] = quote.getText();
	    	lEntryValues[i] = String.valueOf(quote.getId());
	    }
	    this.setEntries(lEntries);
	    this.setEntryValues(lEntryValues);
	}

	@Override
	protected boolean persistString(final String value) {
		if(value == null) {
			return false;
		}
		new AlertDialog.Builder(this.getContext())
		.setTitle("Title")
		.setMessage("Do you really want to delete this quote?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	DatabaseHandler db = new DatabaseHandler(DeleteQuotePreference.this.getContext());
		    	db.deleteQuote(Integer.parseInt(value));
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		return false;
	}
}
