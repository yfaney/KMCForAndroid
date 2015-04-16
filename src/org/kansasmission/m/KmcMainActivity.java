package org.kansasmission.m;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 
 * @version 1.0  KMC 1st release
 * @version 1.1  Adding Wednesday Seminar
 * @version 1.2  Better quality icon
 * @version 1.21 Correcting ISO time zone
 * @author Faney
 *
 */
public class KmcMainActivity extends Activity {
	private static final String gMapl = "https://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q=8841+Glenwood+St.,+Overland+Park,+KS+66212+&aq=&sll=37.6,-95.665&sspn=42.783003,80.595703&vpsrc=0&t=h&ie=UTF8&hq=&hnear=8841+Glenwood+St,+Overland+Park,+Johnson,+Kansas+66212&z=16&iwloc=A";
	private static final String gDivID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kmc_main);
		TextView textViewHomePage = (TextView)findViewById(R.id.textViewHomePage);
		textViewHomePage.setAutoLinkMask(Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
		textViewHomePage.setLinksClickable(true);
		
        ImageButton imageButtonNews = (ImageButton)findViewById(R.id.imageButtonNews);
        ImageButton imageButtonVideo = (ImageButton)findViewById(R.id.imageButtonVideo);
        ImageButton imageButtonPastor = (ImageButton)findViewById(R.id.imageButtonPastor);
        ImageButton imageButtonManna = (ImageButton)findViewById(R.id.imageButtonManna);
        ImageButton imageButtonCall = (ImageButton)findViewById(R.id.imageButtonCall);
        ImageButton imageButtonNavi = (ImageButton)findViewById(R.id.imageButtonNavi);
        ImageButton imageButtonWedn = (ImageButton)findViewById(R.id.imageButtonWedn);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		if (metrics.heightPixels < 720 && metrics.densityDpi <= 240){
//			resizeImgButton(imageButtonNews, R.drawable.buttonselector1, 0.75);
//			resizeImgButton(imageButtonVideo, R.drawable.buttonselector2, 0.75);
//			resizeImgButton(imageButtonPastor, R.drawable.buttonselector3, 0.75);
//			resizeImgButton(imageButtonManna, R.drawable.buttonselector4, 0.75);
//			resizeImgButton(imageButtonCall, R.drawable.buttonselector5, 0.75);
//			resizeImgButton(imageButtonNavi, R.drawable.buttonselector6, 0.75);
//			resizeImgButton(imageButtonWedn, R.drawable.buttonselector7, 0.75);
//		}
//		else if(metrics.heightPixels > 720 && metrics.densityDpi >= 320){
//			resizeImgButton(imageButtonNews, R.drawable.buttonselector1, 1.5);
//			resizeImgButton(imageButtonVideo, R.drawable.buttonselector2, 1.5);
//			resizeImgButton(imageButtonPastor, R.drawable.buttonselector3, 1.5);
//			resizeImgButton(imageButtonManna, R.drawable.buttonselector4, 1.5);
//			resizeImgButton(imageButtonCall, R.drawable.buttonselector5, 1.5);
//			resizeImgButton(imageButtonNavi, R.drawable.buttonselector6, 1.5);
//			resizeImgButton(imageButtonWedn, R.drawable.buttonselector7, 1.5);
//		}
        
        
        imageButtonNews.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(KmcMainActivity.this, FeedActivity.class); // 평범한 Intent 생성
        		intent.putExtra("CallURL", getString(R.string.url_news));        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
        		intent.putExtra("TitleName", getString(R.string.str_news));
        		intent.putExtra("DocType", "GoogleSite");
        		startActivity(intent);                                    // Activity 실행
        	}
        });
        imageButtonVideo.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(KmcMainActivity.this, FeedActivity.class); // 평범한 Intent 생성
        		intent.putExtra("CallURL", getString(R.string.url_video));        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
        		intent.putExtra("TitleName", getString(R.string.str_video));
        		intent.putExtra("DocType", "Youtube");
        		startActivity(intent);                                    // Activity 실행
        	}
        });
        imageButtonPastor.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(KmcMainActivity.this, FeedActivity.class); // 평범한 Intent 생성
        		intent.putExtra("CallURL", getString(R.string.url_pastor));        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
        		intent.putExtra("TitleName", getString(R.string.str_pastor));
        		intent.putExtra("DocType", "General");
        		startActivity(intent);                                    // Activity 실행
        	}
        });
        imageButtonManna.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(KmcMainActivity.this, FeedActivity.class); // 평범한 Intent 생성
        		intent.putExtra("CallURL", getString(R.string.url_manna));        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
        		intent.putExtra("TitleName", getString(R.string.str_manna));
        		intent.putExtra("DocType", "General");
        		startActivity(intent);                                    // Activity 실행
        	}
        });
        imageButtonCall.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_call))); 
        		startActivity(i);        		
        	}
        });
        imageButtonNavi.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(gMapl)); 
        		startActivity(i);
        	}
        });
        imageButtonWedn.setOnClickListener( new ImageButton.OnClickListener(){
        	@Override
			public void onClick(View v) {
//        		Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_wedns1))); 
//        		startActivity(i);        		
        		Intent intent = new Intent(KmcMainActivity.this, FeedActivity.class); // 평범한 Intent 생성
        		intent.putExtra("CallURL", getString(R.string.url_wedns));        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
        		intent.putExtra("TitleName", getString(R.string.str_wedns));
        		intent.putExtra("DocType", "Wednesday");
        		startActivity(intent);                                    // Activity 실행
        	}
        });
	}

	private void resizeImgButton(ImageButton imageButtonObj, int pId, double ratio) {
		// TODO Auto-generated method stub
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), pId); // 비트맵 이미지를 만든다.
		int width=(int)(imageButtonObj.getWidth() * ratio); // 가로 사이즈 지정
		int height=(int)(imageButtonObj.getHeight() * ratio); // 세로 사이즈 지정
		Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, width, height, true); // 이미지 사이즈 조정
		imageButtonObj.setImageBitmap(resizedbitmap); // 이미지뷰에 조정한 이미지 넣기

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kmc_main, menu);
		return true;
	}

}
