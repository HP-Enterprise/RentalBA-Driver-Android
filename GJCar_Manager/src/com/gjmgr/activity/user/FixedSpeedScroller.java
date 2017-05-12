package com.gjmgr.activity.user;

import android.annotation.SuppressLint;
import android.content.Context;  
import android.view.animation.Interpolator;  
import android.widget.Scroller;  
  
public class FixedSpeedScroller extends Scroller {  
    private int mDuration = 0;  
  
    public void setScrollDuration(int duration){
        this.mDuration = duration;
    }
    
    public FixedSpeedScroller(Context context) {  
        super(context);  
    }  
  
    public FixedSpeedScroller(Context context, Interpolator interpolator) {  
        super(context, interpolator);  
    }  
  
    @SuppressLint("NewApi")
	public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {  
        super(context, interpolator, flywheel);  
    }  
  
  
    @Override  
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {  
        super.startScroll(startX, startY, dx, dy, mDuration);  
    }  
  
    @Override  
    public void startScroll(int startX, int startY, int dx, int dy) {  
        super.startScroll(startX, startY, dx, dy, mDuration);  
    }  
}