package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView.HitTestResult;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
public class RefreshListView extends ListView implements OnScrollListener{
  private float lastY=-1;
  private RelativeLayout mHeaderviewcontent;
  private TextView hintview;
  private int allitems;
  private boolean isrefreshing=false;
  private boolean enablefresh=true;
  private boolean enableload=true;
  private boolean isloading=false;
  private boolean isloadready=false;
  private float ratio=1.8f;
  private Scroller onScroller;
  private OnScrollListener scrollListener;
  private RefreshListViewListener listener;
  private int scrolldelta=400;
  private int loadmoredelta=50;
  private RefreshListViewHeader header;
  private RefreshListViewFooter footer;
  private int headerheight;
  private int scrollbackheader=0;
  private int scrollbackfooter=1;
  private int scrollback;
  public RefreshListView(Context context){
	  super(context);
	  init(context);
  }
  public RefreshListView(Context context, AttributeSet attr){
	  super(context, attr);
	  init(context);
  }
  public RefreshListView(Context context, AttributeSet attr, int defstyle){
	  super(context,attr,defstyle);
	  init(context);
  }
  public void init(Context context){
	onScroller=new Scroller(context,  new DecelerateInterpolator());
	super.setOnScrollListener(this);
	header = new RefreshListViewHeader(context);
	mHeaderviewcontent = (RelativeLayout)header.findViewById(R.id.xlistview_header_content);
	hintview=(TextView)header.findViewById(R.id.xlistview_header_time);
	addHeaderView(header);
    footer = new RefreshListViewFooter(context);
    //addView(footer);
    header.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		
		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			headerheight = mHeaderviewcontent.getHeight();
			getViewTreeObserver()
			.removeGlobalOnLayoutListener(this);
		}
	});
    
  }
  public void setEnableFresh(boolean enable){
	  enablefresh=enable;
	  if(enablefresh){
		mHeaderviewcontent.setVisibility(View.VISIBLE);
		
	  }
	  else
		mHeaderviewcontent.setVisibility(View.INVISIBLE);
  }
@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
	    if(isloadready==false){
	    	isloadready=true;
	    	addFooterView(footer);
	    }
		super.setAdapter(adapter);
	}
    public void setEnableLoad(boolean enable){
    	enableload=enable;
    	if(enableload==false){
    		footer.hide();
    		footer.setOnClickListener(null);
    	}
    	else{
    		isloading=false;
    		footer.show();
    		footer.setState(RefreshListViewFooter.STATE_NORMAL);
    		footer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 startLoadMore();
				}
			});
    	}
    }
    public void stopRefresh(){
    	if(isrefreshing==true){
    		isrefreshing=false;
    		ResetHeaderHeight();
    	}
    }
    public void stopLoadMore(){
    	if(isloading==true){
    		isloading=false;
    		footer.setState(RefreshListViewFooter.STATE_NORMAL);
    	}
    }
    public void setRefreshtime(String time){
    	hintview.setText(time);
    }
    private void updateHeaderHeight(float delta){
      header.setVisiableHeight((int)delta+header.getVisiableHeight());
      Log.e("headerdelta",String.valueOf(delta));
      Log.e("headervisibleheight",String.valueOf(header.getVisiableHeight()));
      if(enablefresh&&!isrefreshing){
    	  if(header.getVisiableHeight()>headerheight){
    		 header.setState(RefreshListViewHeader.STATE_READY); 
    	  }
    	  else{
    		  header.setState(RefreshListViewHeader.STATE_NORMAL);
    	  }
      }
    }
    public void ResetHeaderHeight(){
      int height = header.getVisiableHeight();
      if(height==0)return;
      if(isrefreshing&&height<=headerheight)return;
      int lastheight=0;
      if(isrefreshing&&height>headerheight){
    	  lastheight=headerheight;
      }
      scrollback=scrollbackheader;
      Log.e("lastheight",String.valueOf(lastheight));
      Log.e("height",String.valueOf(height));
      onScroller.startScroll(0, height, 0, lastheight-height, scrolldelta);
      invalidate();
      
    }
    public void updateFooterheight(float delta){
    	int bottommargin = footer.getBottomMargin()+(int)delta;
    	Log.e("footerdelta",String.valueOf(delta));
    	Log.e("footermargin",String.valueOf(footer.getBottomMargin()));
    	if(enableload&&!isloading){
    		if(bottommargin>loadmoredelta){
    			footer.setState(RefreshListViewFooter.STATE_READY);
    		}
    		else{
    			footer.setState(RefreshListViewFooter.STATE_NORMAL);
    		}
    	}
    	footer.setBottomMargin(bottommargin);
    	
    }
    public void resetFooterheight(){
      int bottommargin = footer.getBottomMargin();
      if(bottommargin>0){
    	  scrollback=scrollbackfooter;
    	  onScroller.startScroll(0, bottommargin, 0, -bottommargin, loadmoredelta);
        invalidate();
      }
    }
    public void startLoadMore(){
      isloading=true;
      footer.setState(RefreshListViewFooter.STATE_LOADING);
      if (listener != null) {
    	  listener.onload();
		}
      
    }
    @Override
    	public boolean onTouchEvent(MotionEvent ev) {
    		// TODO Auto-generated method stub
    	    if(lastY==-1)
    	    	lastY = ev.getRawY();
    	    switch(ev.getAction()){
    	    case MotionEvent.ACTION_DOWN:
    	    	lastY=ev.getRawY();
    	    	break;
    	    case MotionEvent.ACTION_MOVE:
    	    	final float delta = ev.getRawY()-lastY;
    	    	lastY=ev.getRawY();
    	    	//Log.e("move","here");
    	    	Log.e("firstvisble",String.valueOf(getFirstVisiblePosition()));
    	    	Log.e("lastvisble",String.valueOf(getLastVisiblePosition()));
    	    	Log.e("delta",String.valueOf(delta));
    	    	if(getFirstVisiblePosition()==0&&(header.getVisiableHeight()>0||delta>0)){
    	    		Log.e("updateheader","here");
    	    		updateHeaderHeight(delta/ratio);
    	    		
    	    	}
    	    	else if(getLastVisiblePosition()==allitems-1&&(footer.getBottomMargin()>0||delta<0)){
    	    		updateFooterheight(-delta/ratio);
    	    		Log.e("updatefooter","here");
    	    	}
    	    	break;
    	    default:
    	    	lastY=-1;
    	    	if(getFirstVisiblePosition()==0){
    	    		if(enablefresh&&header.getVisiableHeight()>headerheight){
    	    			isrefreshing=true;
    	    			header.setState(RefreshListViewHeader.STATE_REFRESHING);
    	    			if(listener!=null)
    	    				listener.refresh();
    	    		}
    	    		Log.e("resetheader","here");
    	    		ResetHeaderHeight();
    	    	}
    	    	if(getLastVisiblePosition()==allitems-1){
    	    		if(enableload&&footer.getBottomMargin()>loadmoredelta){
    	    			isloading=true;
    	    			footer.setState(RefreshListViewFooter.STATE_LOADING);
    	    			startLoadMore();
    	    		}
    	    		resetFooterheight();
    	    		Log.e("resetfooter","here");
    	    	}
    	    	
    	    	break;
    	    }
    	    
    		return super.onTouchEvent(ev);
    	}
@Override
public void onScrollStateChanged(AbsListView view, int scrollState) {
	// TODO Auto-generated method stub
	if(scrollListener!=null)
		scrollListener.onScrollStateChanged(view, scrollState);
}
@Override
public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	    allitems=totalItemCount;
	    if(scrollListener!=null)
		  scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	// TODO Auto-generated method stub
	
 }
@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		scrollListener=l;
	    super.setOnScrollListener(scrollListener);
	}
public void setRefreshListViewListener(RefreshListViewListener l){
	listener = l;
}
@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
	   if(onScroller.computeScrollOffset()){
		   if(scrollback==scrollbackheader){
			   header.setVisiableHeight(onScroller.getCurrY());
		   }
		   else if(scrollback==scrollbackfooter){
			   footer.setBottomMargin(onScroller.getCurrY());
		   }
	   }
	   postInvalidate();
		super.computeScroll();
	}
   public interface RefreshListViewListener{
	   public void refresh();
	   public void onload();	   
   }
}
