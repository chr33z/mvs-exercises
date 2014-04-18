package com.example.mvs_excercise_1_2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DetailActivity extends Activity {
	
	public static final String FILE_NAME = "filename";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		Intent intent = getIntent();
		if(intent != null && intent.getStringExtra(FILE_NAME) != null){
			loadImage(intent.getStringExtra(FILE_NAME));
		}
		
		findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void loadImage(String fileName) {
		FileInputStream fis;
		try {
			fis = openFileInput(fileName);
			Drawable image = Drawable.createFromStream(fis, fileName);
			((ImageView) findViewById(R.id.detail_image)).setImageDrawable(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
