package org.kansasmission.m;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class ViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		Intent intent = getIntent();   // 값을 받기 위한 Intent 생성
		if (intent == null){
			Toast.makeText(ViewActivity.this, "요청이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
		}
		else{
			String mContents = intent.getStringExtra("Contents"); 
			if (mContents != null){
				WebView webContentsView = (WebView)findViewById(R.id.webView1);
				// 자바스크립트 허용
				webContentsView.getSettings().setJavaScriptEnabled(false);
				                    
				// 스크롤바 없애기
				webContentsView.setHorizontalScrollBarEnabled(false);
				webContentsView.setVerticalScrollBarEnabled(false);
				webContentsView.setBackgroundColor(0);
				WebSettings webSettings = webContentsView.getSettings();
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				webSettings.setDefaultFontSize(metrics.widthPixels / metrics.densityDpi * 10);
				webContentsView.loadData(mContents, "text/html; charset=UTF-8", null);
//				Toast.makeText(ViewActivity.this, Integer.toString(metrics.widthPixels / metrics.densityDpi * 10), Toast.LENGTH_SHORT).show();
				setTitle(intent.getStringExtra("Title"));
			}
			else{
				Toast.makeText(ViewActivity.this, "내용이 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

}
