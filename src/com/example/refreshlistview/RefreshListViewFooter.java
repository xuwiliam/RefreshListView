package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RefreshListViewFooter extends LinearLayout{
  public final static int STATE_NORMAL=0;
  public final static int STATE_READY=1;
  public final static int STATE_LOADING=2;
  private Context mcontext;
  private TextView mtv;
  private ProgressBar bar;
  private View contentview;
  public RefreshListViewFooter(Context context){
	  super(context);
	  init(context);
  }
  public RefreshListViewFooter(Context context, AttributeSet attrs){
	  super(context,attrs);
	  init(context);
  }
  public void loading(int state){
	mtv.setVisibility(View.GONE);
	bar.setVisibility(View.VISIBLE);
	
  }
  public void setState(int state){
	 mtv.setVisibility(View.INVISIBLE);
	 bar.setVisibility(View.INVISIBLE);
	 if(state == STATE_READY){
		mtv.setVisibility(View.VISIBLE);
		mtv.setText(R.string.xlistview_footer_hint_ready);
	 }
	 else if(state==STATE_LOADING){
		 bar.setVisibility(View.VISIBLE);
		 
	 }
	 else if(state==STATE_NORMAL){
		 mtv.setVisibility(View.VISIBLE);
		 mtv.setText(R.string.xlistview_footer_hint_normal);
	 }
  }
  public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)contentview.getLayoutParams();
		return lp.bottomMargin;
	}
  public void setBottomMargin(int height) {
		if (height < 0) return ;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)contentview.getLayoutParams();
		lp.bottomMargin = height;
		contentview.setLayoutParams(lp);
	}
  
  public void hide(){
	  LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)contentview.getLayoutParams();
	  lp.height=0;
	  contentview.setLayoutParams(lp);
  }
  public void show(){
	  LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)contentview.getLayoutParams();
	  lp.height=LayoutParams.WRAP_CONTENT;
	  contentview.setLayoutParams(lp);
  }
  public void init(Context context){
	  mcontext=context;
	  LinearLayout parentview=(LinearLayout)LayoutInflater.from(mcontext).inflate(R.layout.footer, null);
	  addView(parentview);
	  parentview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	  contentview = (RelativeLayout)parentview.findViewById(R.id.xlistview_footer_content);
	  mtv = (TextView)parentview.findViewById(R.id.xlistview_footer_hint_textview);
	  bar = (ProgressBar)parentview.findViewById(R.id.xlistview_footer_progressbar);
	  
  }
}
