package com.example.mvs_exercise_2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button buttonGoogleMaps;
	private Button buttonGPS;
	
	private OnClickListener buttonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button_google_map:
				startActivity(new Intent(getBaseContext(), GoogleMapActivity.class));
				break;
			case R.id.button_gps_ifi:
				startActivity(new Intent(getBaseContext(), GPSActivity.class));
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonGoogleMaps = (Button) findViewById(R.id.button_google_map);
		buttonGoogleMaps.setOnClickListener(buttonListener);
		buttonGPS = (Button) findViewById(R.id.button_gps_ifi);
		buttonGPS.setOnClickListener(buttonListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
