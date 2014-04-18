package com.example.mvs_excercise_1_2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DownloadService extends Service {

	private static final String TAG = DownloadService.class.getSimpleName();

	public static final String BROADCAST_ACTION = "com.example.mvs_excercise_1_2.IMAGE_READY";
	
	public static final String FILE_NAME = "imageFileName";
	public static final String IMAGE_ID = "imageId";
	public static final String PROGRESSBAR_ID = "progressbarId";

	private URL imageUrl1;
	private URL imageUrl2;
	private URL imageUrl3;
	private URL imageUrl4;
	private URL imageUrl5;

	@Override
	public void onCreate(){
		try {
			imageUrl1 = new URL("http://imgs.xkcd.com/comics/orbital_mechanics.png");
			imageUrl2 = new URL("http://imgs.xkcd.com/comics/flowchart.png");
			imageUrl3 = new URL("http://imgs.xkcd.com/comics/identity.png");
			imageUrl4 = new URL("http://imgs.xkcd.com/comics/im_sorry.png");
			imageUrl5 = new URL("http://imgs.xkcd.com/comics/actually.png");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		downloadInBackground();
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void downloadInBackground(){
		downloadResourceInBackground(imageUrl1, R.id.dl_image_1, R.id.dl_progressbar_1);
		downloadResourceInBackground(imageUrl2, R.id.dl_image_2, R.id.dl_progressbar_2);
		downloadResourceInBackground(imageUrl3, R.id.dl_image_3, R.id.dl_progressbar_3);
		downloadResourceInBackground(imageUrl4, R.id.dl_image_4, R.id.dl_progressbar_4);
		downloadResourceInBackground(imageUrl5, R.id.dl_image_5, R.id.dl_progressbar_5);
	}

	private void downloadResourceInBackground(final URL url, final int imageViewId, final int progressBarId){
		Thread thread = new Thread()
		{
			@Override
			public void run() {
				try {
					String fileName = new File(url.getPath()).getName();

					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					writeInputStream(connection.getInputStream(), fileName);

					sendDownloadBroadcast(fileName, imageViewId, progressBarId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	private void writeInputStream(InputStream inputStream, String fileName){
		
		try {
			FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
			fos.flush();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendDownloadBroadcast(String fileName, int imageViewId, int progressBarId){
		Intent imageIntent = new Intent(DownloadService.BROADCAST_ACTION);
		imageIntent.putExtra(FILE_NAME, fileName);
		imageIntent.putExtra(IMAGE_ID, imageViewId);
		imageIntent.putExtra(PROGRESSBAR_ID, progressBarId);
		sendBroadcast(imageIntent);
		Log.d(TAG, "broadcast sent");
	}
}
