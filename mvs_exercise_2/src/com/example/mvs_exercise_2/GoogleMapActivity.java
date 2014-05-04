package com.example.mvs_exercise_2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class GoogleMapActivity extends FragmentActivity {
	
	private static final String TAG = GoogleMapActivity.class.getSimpleName();
	
	private static final float CAMERA_ZOOM_MAX = 17f;
	
	private GoogleMap mMap;
	
	private LocationManager mLocationManager;
	
	private Marker mCurrentLocationMarker;

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
		setContentView(R.layout.activity_google_map);
		
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);

		Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if(lastKnownLocation != null){
			updateMapLocation(lastKnownLocation);
		}
	}
	
	private void updateMapLocation(Location location) {
		LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
		
		if(mCurrentLocationMarker == null){
			MarkerOptions mOptions = new MarkerOptions()
				.position(newLocation)
				.title(getString(R.string.map_your_position));
			mCurrentLocationMarker = mMap.addMarker(mOptions);
		} else {
			mCurrentLocationMarker.setPosition(newLocation);
		}
		
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, CAMERA_ZOOM_MAX));
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
		getMenuInflater().inflate(R.menu.google_map, menu);
		return true;
	}

}
