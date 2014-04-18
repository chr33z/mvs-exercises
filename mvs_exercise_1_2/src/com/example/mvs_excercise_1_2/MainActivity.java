package com.example.mvs_excercise_1_2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private ImageView mImage1;
	private ImageView mImage2;
	private ImageView mImage3;
	private ImageView mImage4;
	private ImageView mImage5;
	
	private ProgressBar mProgress1;
	private ProgressBar mProgress2;
	private ProgressBar mProgress3;
	private ProgressBar mProgress4;
	private ProgressBar mProgress5;
	
	private boolean serviceStartet = false;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
		@Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null && intent.getStringExtra(DownloadService.FILE_NAME) != null){
            	String fileName = intent.getStringExtra(DownloadService.FILE_NAME);
            	int imageViewId = intent.getIntExtra(DownloadService.IMAGE_ID, 0);
            	int progressBarId = intent.getIntExtra(DownloadService.PROGRESSBAR_ID, 0);
            	
            	if(imageViewId != 0 && progressBarId != 0){
            		try {
            			FileInputStream fis = openFileInput(fileName);
            			Drawable image = Drawable.createFromStream(fis, fileName);
            			
            			ImageView imgView = (ImageView) findViewById(imageViewId);
            			ProgressBar progressBar = (ProgressBar) findViewById(progressBarId);
            			
            			imgView.setVisibility(View.VISIBLE);
            			imgView.setImageDrawable(image);
            			addImageClickListener(imgView, fileName);
            			progressBar.setVisibility(View.GONE);
            			
            		} catch (FileNotFoundException e) {
            			e.printStackTrace();
            		} catch (NullPointerException e){
            			e.printStackTrace();
            		}
            	}
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mImage1 = (ImageView) findViewById(R.id.dl_image_1);
		mImage2 = (ImageView) findViewById(R.id.dl_image_2);
		mImage3 = (ImageView) findViewById(R.id.dl_image_3);
		mImage4 = (ImageView) findViewById(R.id.dl_image_4);
		mImage5 = (ImageView) findViewById(R.id.dl_image_5);
		
		mProgress1 = (ProgressBar) findViewById(R.id.dl_progressbar_1);
		mProgress2 = (ProgressBar) findViewById(R.id.dl_progressbar_2);
		mProgress3 = (ProgressBar) findViewById(R.id.dl_progressbar_3);
		mProgress4 = (ProgressBar) findViewById(R.id.dl_progressbar_4);
		mProgress5 = (ProgressBar) findViewById(R.id.dl_progressbar_5);
		
		findViewById(R.id.button_start_download).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!serviceStartet){
					startService();
					serviceStartet = true;
				} else {
					reset();
					serviceStartet = false;
				}
			}
		});
		
		findViewById(R.id.button_kill_app).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        registerReceiver(mReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    
    private void addImageClickListener(ImageView imageView, final String fileName){
    	imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
				detailIntent.putExtra(DetailActivity.FILE_NAME, fileName);
				startActivity(detailIntent);
			}
		});
    }

	private void startService(){
		mProgress1.setIndeterminate(true);
		mProgress2.setIndeterminate(true);
		mProgress3.setIndeterminate(true);
		mProgress4.setIndeterminate(true);
		mProgress5.setIndeterminate(true);
		
		Intent intent = new Intent(this, DownloadService.class);
		startService(intent); 
	}
	
	private void reset(){
		mProgress1.setIndeterminate(false);
		mProgress2.setIndeterminate(false);
		mProgress3.setIndeterminate(false);
		mProgress4.setIndeterminate(false);
		mProgress5.setIndeterminate(false);
		mProgress1.setVisibility(View.VISIBLE);
		mProgress2.setVisibility(View.VISIBLE);
		mProgress3.setVisibility(View.VISIBLE);
		mProgress4.setVisibility(View.VISIBLE);
		mProgress5.setVisibility(View.VISIBLE);
		
		mImage1.setVisibility(View.GONE);
		mImage2.setVisibility(View.GONE);
		mImage3.setVisibility(View.GONE);
		mImage4.setVisibility(View.GONE);
		mImage5.setVisibility(View.GONE);
	}
}
