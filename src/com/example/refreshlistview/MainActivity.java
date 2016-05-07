package com.example.refreshlistview;

import java.util.ArrayList;

import com.example.refreshlistview.RefreshListView.RefreshListViewListener;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ActionBarActivity implements RefreshListViewListener {
	private RefreshListView mListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    public void init(){
    	geneItems();
		mListView = (RefreshListView) findViewById(R.id.pullrefreshlist);
		mListView.setEnableLoad(true);
		mListView.setEnableFresh(true);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setRefreshListViewListener(this);
	
		mHandler = new Handler();
    }
    private void geneItems() {
		for (int i = 0; i != 5; ++i) {
			items.add("refresh cnt " + (++start));
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onLo() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshtime("¸Õ¸Õ");
	}
	public void onload() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				mAdapter.notifyDataSetChanged();
				onLo();
			}
		}, 2000);
	}
	@Override
	public void refresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				items.clear();
				geneItems();
				// mAdapter.notifyDataSetChanged();
				mAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, items);
				mListView.setAdapter(mAdapter);
				onLo();
			}
		}, 2000);
	}
}
