package com.gjmgr.view.widget;


import android.content.Context;  
import android.graphics.Color;
import android.os.Handler;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;  
import android.widget.TextView;
  
public class CustomerScrollview_QQ extends ScrollView {  
  
    Context mContext;  
    private View mView;  
    private float touchY;  
    private int scrollY = 0;  
    private boolean handleStop = false;  
    private int eachStep = 0;  
  
    private static final int MAX_SCROLL_HEIGHT = 300;// 最大滑动距离  
    private static final float SCROLL_RATIO = 1.0f;// 阻尼系数,越小阻力就越大  
  
    private int topMargin = 0;
    
    public CustomerScrollview_QQ(Context context) {  
        super(context);  
        this.mContext = context;  
    }  
  
    public CustomerScrollview_QQ(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.mContext = context;  
        
    }  
  
    public CustomerScrollview_QQ(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.mContext = context;  
    }  
  
    @Override  
    protected void onFinishInflate() {  
        if (getChildCount() > 0) {  
            this.mView = getChildAt(0);  
        }  
//        final LinearLayout headerView = (LinearLayout)mView.findViewById(R.id.lin);
//        headerView.getViewTreeObserver().addOnGlobalLayoutListener(
//				new OnGlobalLayoutListener() {
//
//	
//					public void onGlobalLayout() {
//						// TODO Auto-generated method stub
//						int headerHeight = headerView.getHeight();
//						updateMargin(headerView,-headerHeight);
//						headerView.getViewTreeObserver()
//								.removeGlobalOnLayoutListener(this);
//					}
//				});
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {  
            touchY = arg0.getY();  
        }  
        return super.onInterceptTouchEvent(arg0);  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        if (mView == null) {  
            return super.onTouchEvent(ev);  
        } else {  
            commonOnTouchEvent(ev);  
        }  
        return super.onTouchEvent(ev);  
    }  
  
    private void commonOnTouchEvent(MotionEvent ev) {  
        int action = ev.getAction();  
        switch (action) {  
        case MotionEvent.ACTION_UP:  
            if (mView.getScrollY() != 0) {  
                handleStop = true;  
                animation();  
            }  
            break;  
        case MotionEvent.ACTION_MOVE:  
            float nowY = ev.getY();  
            int deltaY = (int) (touchY - nowY);  
            touchY = nowY;  
            if (isNeedMove()) {  
                int offset = mView.getScrollY();  
                if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {  
                    mView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));  
                    handleStop = false;  
                }  
            }  
              
            break;  
        default:  
            break;  
        }  
    }  
  
    private boolean isNeedMove() {  
        int viewHight = mView.getMeasuredHeight();  
        int srollHight = getHeight();  
        int offset = viewHight - srollHight;  
        int scrollY = getScrollY();  
        if (scrollY == 0 || scrollY == offset) {  
            return true;  
        }  
        return false;  
    }  
      
    private void animation() {  
        scrollY = mView.getScrollY();  
        eachStep = scrollY / 10;  
        resetPositionHandler.sendEmptyMessage(0);  
    }  
  
    Handler resetPositionHandler = new Handler() {  
        public void handleMessage(android.os.Message msg) {  
            if (scrollY != 0 && handleStop) {  
                scrollY -= eachStep;  
                if ((eachStep < 0 && scrollY > 0) ||  (eachStep > 0 && scrollY < 0)) {  
                    scrollY = 0;  
                }  
                mView.scrollTo(0, scrollY);  
                this.sendEmptyMessageDelayed(0, 5);  
            }  
        };  
    };  
    
    /**
	 * 更新header的margin
	 * @param margin
	 */
	public void updateMargin(ViewGroup view,int margin) {
		//这里用Linearlayout的原因是Headerview的父控件是scrollcontainer是一个linearlayout 
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.topMargin = margin;
		topMargin = margin;
		view.setLayoutParams(params);
	}
      
}  