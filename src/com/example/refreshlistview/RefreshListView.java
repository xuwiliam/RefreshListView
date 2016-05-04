package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
public class RefreshListView extends ListView implements OnScrollListener{
  public RefreshListView(Context context){
	  super(context);
  }
  public RefreshListView(Context context, AttributeSet attr){
	  super(context, attr);  
  }
  public RefreshListView(Context context, AttributeSet attr, int defstyle){
	  super(context,attr,defstyle);
  }
@Override
public void onScrollStateChanged(AbsListView view, int scrollState) {
	// TODO Auto-generated method stub
	
}
@Override
public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	// TODO Auto-generated method stub
	
 }
}
