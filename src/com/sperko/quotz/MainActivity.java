package com.sperko.quotz;

import com.sperko.quotz.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DatabaseHandler db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        applyTheme();
        
        final GestureDetector gestureDetector = new GestureDetector(new FlickListener());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		getWindow().getDecorView().setOnTouchListener(gestureListener);
        
        db = new DatabaseHandler(this);
        
        nextQuote();
    }

	private void applyTheme() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightTheme = prefs.getBoolean(SettingsActivity.NIGHT_THEME, false);
        if(nightTheme) {
        	getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        	TextView quoteView = (TextView) findViewById(R.id.quote);
        	quoteView.setBackgroundColor(Color.BLACK);
        	quoteView.setTextColor(Color.WHITE);

        	Button nextButton = (Button) findViewById(R.id.next);
        	nextButton.setTextColor(Color.WHITE);
        }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_settings:
    		showSettings(item);
    		return true;
    	}
		return super.onOptionsItemSelected(item);
	}

	private void showSettings(MenuItem item) {
		Intent settingsActivity = new Intent(getBaseContext(),
                SettingsActivity.class);
		startActivity(settingsActivity);
	}

	public void showNext(View v) {
    	nextQuote();
    }
    
    private void nextQuote() {
    	Quote quote = db.getRandomQuote();

    	TextView quoteView = (TextView) findViewById(R.id.quote);
    	
    	quoteView.setText(quote.getText());
    }

    class FlickListener extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    	    
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	nextQuote();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	nextQuote();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

    }
}
