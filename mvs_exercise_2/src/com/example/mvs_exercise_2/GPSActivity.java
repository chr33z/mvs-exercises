package com.example.mvs_exercise_2;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class GPSActivity extends Activity {
	
	private static final String TAG	= GPSActivity.class.getSimpleName();
	
	private OettingenView oettView;
	
	private LocationManager mLocationManager;

	private LocationListener mLocationListener = new LocationListener() {
	    
		public void onLocationChanged(Location location) {
			Log.i(TAG, "Found new location at: "+location.getLongitude() + " | " + location.getLatitude());
			updateMapLocation(location);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_map_layout);
		
		oettView = (OettingenView) findViewById(R.id.view_of_ifi);
		
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if(lastKnownLocation != null){
			updateMapLocation(lastKnownLocation);
		}
	}
	
	private void updateMapLocation(Location location){
		oettView.setLocation(location);
	}
	
	protected void onResume(){
		super.onResume();
		
		if(mLocationManager != null){
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 25, mLocationListener);
		}
	}
	
	protected void onStop(){
		super.onStop();
		
		if(mLocationManager != null){
			mLocationManager.removeUpdates(mLocationListener);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.g, menu);
		return true;
	}

}
