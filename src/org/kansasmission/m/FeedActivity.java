package org.kansasmission.m;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.kansasmission.m.R;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FeedActivity extends ListActivity {
	@SuppressWarnings("unused")
	private String httpID = "HTTP Auth UserID";
	@SuppressWarnings("unused")
	private String httpPW = "HTTP Auth Password";
    private ArrayList<TableItem> list;
    private TableAdapter adapter;
	private String[] pubDate, title, link, content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		Intent intent = getIntent();   // 값을 받기 위한 Intent 생성

		if (intent == null){
			Toast.makeText(FeedActivity.this, "요청이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
		}
		else{
			String mCallURL = intent.getStringExtra("CallURL"); 
			String mTitleName = intent.getStringExtra("TitleName");
			String mDocType = intent.getStringExtra("DocType");
	        list = new ArrayList<TableItem>();
	        adapter = new TableAdapter(this, R.layout.row, list);
	        setListAdapter(adapter);

			String returnData;
			//TODO User HTTP Configuration Routine Start//
			setTitle(mTitleName);
			if(mDocType.equals("Wednesday")){
				String[] bookList = getResources().getStringArray(R.array.bible_book);
				String[] bookListUrl = getResources().getStringArray(R.array.bible_book_url);
				if(bookList.length == bookListUrl.length){
					for (int i = 0 ; i < bookList.length ; i++){
						list.add(new TableItem(null, bookList[i], bookListUrl[i], null));
					}
					if(appInstalledOrNot("com.google.android.apps.docs")){

					}
					else{
		                Builder dlg= new AlertDialog.Builder(FeedActivity.this);
		                dlg.setTitle("Google Drive 설치")
		                .setMessage(R.string.dial_driveapp)
		                .setIcon(R.drawable.googledrivehdpi)
		                .setPositiveButton(R.string.dial_driveappOk, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
				        		Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_gdrive))); 
				        		startActivity(i);        		
							}
						})
		                .setNegativeButton(R.string.dial_driveappCancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
		                        Toast.makeText(FeedActivity.this,
		                        		getString(R.string.dial_gdrivealert), Toast.LENGTH_SHORT).show();
							}
		                })
		                .show();						
					}
				}
			}
			else if(mDocType.equals("WednesdayList")){
				String bookUrl = intent.getStringExtra("BookUrl");
				String[] chapterList = null;
				String[] chapterListUrl = null;
				if(bookUrl.equals("bible_list1")){
					chapterList = getResources().getStringArray(R.array.bible_list1);
					chapterListUrl = getResources().getStringArray(R.array.bible_list1_url);
				}
				else if(bookUrl.equals("bible_list2")){
					chapterList = getResources().getStringArray(R.array.bible_list2);
					chapterListUrl = getResources().getStringArray(R.array.bible_list2_url);
				}
				else if(bookUrl.equals("bible_list3")){
					chapterList = getResources().getStringArray(R.array.bible_list3);
					chapterListUrl = getResources().getStringArray(R.array.bible_list3_url);
				}
				if(chapterList != null){
					if(chapterList.length == chapterListUrl.length){
						for (int i = 0 ; i < chapterList.length ; i++){
							list.add(new TableItem(null, chapterList[i], chapterListUrl[i], null));
						}
					}
				}
			}
			else{
				try {
					returnData = new AsyncURLTask().execute(mCallURL).get();
					if(returnData != null){
						list.clear();
						//TODO User XML Parsing Configuration Routine Start//
						AdvDomParser xmlDomParse = new AdvDomParser(returnData);
						if (xmlDomParse.getDocData().getStrictErrorChecking()){
							if (mDocType.equals("GoogleDoc")){
								pubDate = xmlDomParse.getStringByTagName("item", "atom:updated");
								AdvDomParser.convertTimeFormats("yyyy-MM-dd'T'HH:mm:ss", pubDate, "GMT-5");
								title = xmlDomParse.getStringByTagName("item", "title");
								for (int i = 1 ; i < title.length ; i++){
									list.add(new TableItem(pubDate[i], title[i], null, null));
								}
							}
							else if (mDocType.equals("GoogleSite")){
							pubDate = xmlDomParse.getStringByTagName("entry", "published");
							AdvDomParser.convertTimeFormats("yyyy-MM-dd'T'HH:mm:ss", pubDate, "GMT-5");
							title = xmlDomParse.getStringByTagName("entry", "title");
//							link = xmlDomParse.getStringByTagName("entry", "link");
							link = xmlDomParse.getAttributesByTagName("entry", "link", 1, "href");
//							content = xmlDomParse.getStringByTagName("entry", "content", true);
							for (int i = 0 ; i < title.length ; i++){
								list.add(new TableItem(pubDate[i], title[i], link[i], null));
							}
						}
							else if (mDocType.equals("Youtube")){
								pubDate = xmlDomParse.getStringByTagName("item", "pubDate");
								AdvDomParser.convertTimeFormats("EEE, d MMM yyyy HH:mm:ss", pubDate, "GMT-5");
								title = xmlDomParse.getStringByTagName("item", "title");
								link = xmlDomParse.getStringByTagName("item", "link");
								for (int i = 0 ; i < title.length ; i++){
									list.add(new TableItem(pubDate[i], title[i].substring(11), link[i], null));
								}
							}
							else{
								pubDate = xmlDomParse.getStringByTagName("item", "pubDate");
								AdvDomParser.convertTimeFormats("EEE, d MMM yyyy HH:mm:ss", pubDate, "GMT-5");
								title = xmlDomParse.getStringByTagName("item", "title");
								link = xmlDomParse.getStringByTagName("item", "link");
								for (int i = 0 ; i < title.length ; i++){
									list.add(new TableItem(pubDate[i], title[i], link[i], null));
								}
							}
						}
						else{
							Toast.makeText(FeedActivity.this, "Invalid XML data", Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(FeedActivity.this, "Nullpointer Exception", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Prints with other exceptions
					e.printStackTrace();
//					list.add(new TableItem("Error Message", e.getLocalizedMessage().toString()));
				}
			}
				
			}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed, menu);
		return true;
	}
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		/*if(getIntent().getStringExtra("DocType").equals("Wednesday") && list.get(position).getFieldTitle() != null){
			Intent intent = new Intent(this, FeedActivity.class); // 평범한 Intent 생성
    		intent.putExtra("TitleName", list.get(position).getFieldContents());
    		intent.putExtra("BookUrl", list.get(position).getFieldLink());
    		intent.putExtra("DocType", "WednesdayList");
    		startActivity(intent);                                    // Activity 실행
		}*/
		if(list.get(position).getFieldLink() != null){
			Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getFieldLink())); 
			startActivity(i);
		}
		else if(list.get(position).getFieldContents() != null){
    		Intent intent = new Intent(this, ViewActivity.class); // 평범한 Intent 생성
    		intent.putExtra("Contents", list.get(position).getFieldContents());        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
    		intent.putExtra("Title", list.get(position).getFieldTitle());        // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
    		startActivity(intent);                                    // Activity 실행
		}
	}
	private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try
        {
               pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
               app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
               app_installed = false;
        }
        return app_installed ;
    }
}

class AsyncURLTask extends AsyncTask<String, Integer, String>{
	private static final int    TIMEOUT    = 10000;

	@Override
	protected String doInBackground(String... pURL) {
		// TODO Auto-generated method stub
		AdvObjConnector xmlConn = new AdvObjConnector(pURL[0], TIMEOUT);
//		xmlConn.setHttpAuth(httpID, httpPW);
//		xmlConn.appendParm("&erdat=", editCreatedOn.getText().toString());
		//ODOT User HTTP Configuration Routine End//
		return xmlConn.retrievePage();
	}
	
}