package com.example.mvs_exercise_2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class OettingenView extends ImageView {

	public static final int MARKER_RADIUS = 8;
	//scale and shift depend on the image
	
	public static final double scaleY = -0.00000710642;
	public static final double scaleX = 0.00001071844;
	public static final double shiftY = 48.153159;
	public static final double shiftX = 11.591277;
	
//	public static final double scaleX = 0.000015278125;
//	public static final double scaleY = 0.000022375;
//	public static final double shiftX = 48.148283;
//	public static final double shiftY = 11.58501;
	
	private Paint markerPaint;
	private Location l;
	
	//Constructors
	public OettingenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public OettingenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public OettingenView(Context context) {
		super(context);
		init();
	}
	
	public void setLocation(Location _l){
		l = _l;
		
		/* I added an invalidate call here to request a redraw of the view */
		invalidate();
	}
	
	/**
	 * Initialize paint and image
	 */
	private void init(){
		markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markerPaint.setColor(Color.BLUE);
		Resources res = getResources();
		setImageDrawable(res.getDrawable(R.drawable.map_ifi_lmu));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//if we got a location
		if(l != null){
			double longitude = l.getLongitude();
			double latitude = l.getLatitude();
			Log.i(this.getClass().getName(), "coords: " + latitude + " " + longitude);
			
			//transform coordinates to pixels - switch lat/long
			float[] pixelCoords = convert(longitude, latitude);
			Log.i(this.getClass().getName(), "pixel coords: " + pixelCoords[0] + " " + pixelCoords[1]);
			//if the result is inside the bounds of the image
			if(pixelCoords[0]>=0 && pixelCoords[0]<=640 && pixelCoords[1]>=0 && pixelCoords[1]<=960){
				//draw position on image
				canvas.drawCircle(pixelCoords[0], pixelCoords[1], MARKER_RADIUS, markerPaint);
			} else {
				//log the problem
				Log.e(this.getClass().getName(), "can not display position on map");
			}
		}
	}
	
	/**
	 * 
	 * @param x The latitude from a WGS84 coordinate.
	 * @param y The longitude of a WGS84 coordinate.
	 * @return A pair {pixX, pixY} representing the corresponding pixel coordinates of the input.
	 */
	private float[] convert(double x, double y){
		//Point_WGS84_x = Shift_x + Point_pixel_x * Scale_x
		float pixX = (float) ((x-shiftX)/scaleX);
		//Point_WGS84_y = Shift_y + Point_pixel_y * Scale_y
		float pixY = (float) ((y-shiftY)/scaleY);
		float[] pixs = {pixX, pixY};
		return pixs;
	}
}
