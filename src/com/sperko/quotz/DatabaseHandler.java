package com.sperko.quotz;

import java.util.ArrayList;
import java.util.List;

import com.sperko.quotz.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "quotez";

	private static final String TABLE_QUOTES = "quotes";
	
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_QUOTE = "quote";
	private static final String COLUMN_SOURCE = "source";
	private Context context;
	
	private static final Quote NOOP = new Quote(-1, "No Quotes Found", "");
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createQuotesTable(db);
    	String[] quotes = context.getResources().getStringArray(R.array.quotes);
    	for (String quote : quotes) {
			performAdd(db, quote, "");
		}
	}

	private void createQuotesTable(SQLiteDatabase db) {
		String createSql = "CREATE TABLE " + 
				TABLE_QUOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," 
				+ COLUMN_QUOTE + " TEXT,"
                + COLUMN_SOURCE + " TEXT" + 
				")";
        db.execSQL(createSql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1) {
			migrateSlogansToQuotes(db);
		}
	}
	
	public void addQuote(String quote, String source) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    performAdd(db, quote, source);
	    
	    db.close(); // Closing database connection
	}

	private void performAdd(SQLiteDatabase db, String quote, String source) {
		ContentValues values = new ContentValues();
	    values.put(COLUMN_QUOTE, quote);
	    values.put(COLUMN_SOURCE, source);
	 
	    // Inserting Row
	    db.insert(TABLE_QUOTES, null, values);
	}
	
	public void updateQuote(Quote quote) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_QUOTE, quote.getText());
	 
	    // Inserting Row
	    db.update(TABLE_QUOTES, values, COLUMN_ID + "= ?", new String[] { String.valueOf(quote.getId()) });
	    db.close(); // Closing database connection
	}
	
	public void deleteQuote(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_QUOTES, COLUMN_ID + "= ?", new String[] { String.valueOf(id) });
	    db.close(); // Closing database connection
	}
	
	public List<Quote> getQuotes() {
		List<Quote> quotes = new ArrayList<Quote>();
	    // Select All Query
	    String selectQuery = "SELECT " + 
	    		COLUMN_ID + "," +
	    		COLUMN_QUOTE + "," +
	    		COLUMN_SOURCE +
	    		" FROM " + TABLE_QUOTES +
	    		" ORDER BY " + COLUMN_QUOTE;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	quotes.add(new Quote(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
	        } while (cursor.moveToNext());
	    }
	 
	    db.close();
	    
	    // return contact list
	    return quotes;
    }
	
	public Quote getRandomQuote() {
		Quote quote = NOOP;
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.query(TABLE_QUOTES, new String[]{COLUMN_ID, COLUMN_QUOTE, COLUMN_SOURCE}, null, null, null, null, "RANDOM()", "1");
	    if(cursor.moveToFirst()) {
	    	quote = new Quote(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
	    }
	    db.close();
	    return quote;
	}
	
	private void migrateSlogansToQuotes(SQLiteDatabase db) {
		createQuotesTable(db);
		
		db.execSQL("INSERT INTO " + TABLE_QUOTES + "(" + COLUMN_ID + ", " + COLUMN_QUOTE + ") SELECT id, slogan from slogans");
		db.execSQL("DROP TABLE slogans");
	}
}
