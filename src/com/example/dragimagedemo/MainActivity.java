package com.example.dragimagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView imageView;
	private DisplayMetrics dm;
	private Bitmap bitmap;

	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("圖片預覽程式");
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
				
		imageView = (ImageView)findViewById(R.id.image_view);
		zoomInButton = (ImageButton)findViewById(R.id.zoomInButton);
		zoomOutButton = (ImageButton)findViewById(R.id.zoomOutButton);
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
		imageView.setImageBitmap(bitmap);
		
		new ImageViewHelper(this,dm,imageView,bitmap,zoomInButton,zoomOutButton);     
	}
}
  